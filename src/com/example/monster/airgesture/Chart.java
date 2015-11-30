package com.example.monster.airgesture;

import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.R.integer;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Chart extends Activity {

	private Timer timer = new Timer();
	private GraphicalView chart;
	private TimerTask task;
	private CategorySeries series1;
	private XYMultipleSeriesDataset dataset1;
	private static Handler handler;
	public static final float pi = 3.1415926f;
	int length = 4096;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
		// ����ͼ��
		chart = ChartFactory.getCubeLineChartView(this, getDateDemoDataset(),
				getDemoRenderer(), 0.5f);
		layout.addView(chart, new LayoutParams(LayoutParams.WRAP_CONTENT, 380));
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// ˢ��ͼ��
				updateChart();
				super.handleMessage(msg);
			}
		};
		task = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 200;
				handler.sendMessage(message);
			}
		};
		timer.schedule(task, 2000, 500);
	}

	private void updateChart() {
		RadioButton raw = (RadioButton) findViewById(R.id.raw);
		RadioButton fft = (RadioButton) findViewById(R.id.fft);
		boolean rawChecked = raw.isChecked();
		boolean fftChecked = fft.isChecked();

		double[] temp = new double[length];
		if (rawChecked) {
			for (int i = 0; i < length; i++) {
				temp[i] = (double) MainActivity.rec[i];
			}
		}

		if (fftChecked) {
			// ��֤����Ϊ2���ݴ���
			// int length = up2int(MainActivity.length);
			
			Complex[] complexs = new Complex[length];
			for (int i = 0; i < length; i++) {
				complexs[i] = new Complex((double) MainActivity.rec[i]);
			}
			fft(complexs, length);
			for (int i = 0; i < length / 2; i++) {
				temp[i] = complexs[i].getDoubleValue();
			}
		}

		long v = 0;
		// �� buffer ����ȡ��������ƽ��������
		for (int i = 0; i < MainActivity.rec.length; i++) {
			v += MainActivity.rec[i] * MainActivity.rec[i];
		}
		// ƽ���ͳ��������ܳ��ȣ��õ�������С��
		double mean = v / (double) MainActivity.length;
		double volume = 10 * Math.log10(mean);
		TextView textView = (TextView) findViewById(R.id.volume);
		textView.setText("Volume: " + volume);

		buildBarDataset(temp);
		// ���߸���
		chart.invalidate();
	}

	protected void buildBarDataset(double[] values) {
		dataset1.clear();
		String title = new String("Chart");
		series1 = new CategorySeries(title);
		int seriesLength = values.length;
		for (int k = 0; k < seriesLength; k++) {
			series1.add(values[k]);
		}
		dataset1.addSeries(series1.toXYSeries());
	}

	private XYMultipleSeriesRenderer getDemoRenderer() {
		int[] colors = new int[] { Color.BLUE };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		setChartSettings(renderer, /* ��Ⱦ�� */
				"Record", /* ͼ����� */
				"Index", /* x����� */
				"Value", /* y����� */
				0, /* x����Сֵ */
				length, /* x�����ֵ */
				-32768, /* y����Сֵ */
				32767, /* y�����ֵ */
				Color.GRAY, /* ��������ɫ */
				Color.LTGRAY); /* ��ǩ��ɫ ��ǩ�� ͼ����� xy����� */

		renderer.setXLabels(12); /* ���� x ��̶ȸ��� */
		renderer.setYLabels(10); /* ���� y ��̶ȸ��� */
		renderer.setChartTitleTextSize(20); /* ���ñ����������С */
		renderer.setTextTypeface("sans_serif", Typeface.BOLD); /* �������� */
		renderer.setLabelsTextSize(14f); /* ���������С */
		renderer.setAxisTitleTextSize(15);
		renderer.setLegendTextSize(15);
		return renderer;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}

	private XYMultipleSeriesDataset getDateDemoDataset() {
		String title = new String("Chart");
		series1 = new CategorySeries(title);
		dataset1 = new XYMultipleSeriesDataset();
		dataset1.addSeries(series1.toXYSeries());
		return dataset1;
	}

	@Override
	public void onDestroy() {
		// ����������ʱ�ص�Timer
		timer.cancel();
		super.onDestroy();
	};

	/**
	 * ����ȡ��ӽ�iint��2���ݴ���.����iint=320ʱ,����256
	 * 
	 * @param iint
	 * @return
	 */
	private int up2int(int iint) {
		int ret = 1;
		while (ret <= iint) {
			ret = ret << 1;
		}
		return ret >> 1;
	}

	// ���ٸ���Ҷ�任
	public void fft(Complex[] xin, int N) {
		int f, m, N2, nm, i, k, j, L;// L:���㼶��
		float p;
		int e2, le, B, ip;
		Complex w = new Complex();
		Complex t = new Complex();
		N2 = N / 2;// ÿһ���е��εĸ���,ͬʱҲ����mλ�����������λ��ʮ����Ȩֵ
		f = N;// f��Ϊ�������̵ļ�����������
		for (m = 1; (f = f / 2) != 1; m++)
			; // �õ�����ͼ�Ĺ�����
		nm = N - 2;
		j = N2;
		/****** �������㡪���׵��㷨 ******/
		for (i = 1; i <= nm; i++) {
			if (i < j)// ��ֹ�ظ�����
			{
				t = xin[j];
				xin[j] = xin[i];
				xin[i] = t;
			}
			k = N2;
			while (j >= k) {
				j = j - k;
				k = k / 2;
			}
			j = j + k;
		}
		/****** ����ͼ���㲿�� ******/
		for (L = 1; L <= m; L++) // �ӵ�1������m��
		{
			e2 = (int) Math.pow(2, L);
			// e2=(int)2.pow(L);
			le = e2 + 1;
			B = e2 / 2;
			for (j = 0; j < B; j++) // j��0��2^(L-1)-1
			{
				p = 2 * pi / e2;
				w.real = Math.cos(p * j);
				// w.real=Math.cos((double)p*j); //ϵ��W
				w.image = Math.sin(p * j) * -1;
				// w.imag = -sin(p*j);
				for (i = j; i < N; i = i + e2) // ���������ͬϵ��������
				{
					ip = i + B; // ��Ӧ���ε����ݼ��Ϊ2^(L-1)
					t = xin[ip].cc(w);
					xin[ip] = xin[i].cut(t);
					xin[i] = xin[i].sum(t);
				}
			}
		}
	}
}

class Complex {
	public double real;
	public double image;

	// �������캯��
	public Complex() {
		// TODO Auto-generated constructor stub
		this.real = 0;
		this.image = 0;
	}

	public Complex(double real, double image) {
		this.real = real;
		this.image = image;
	}

	public Complex(int real, int image) {
		Integer integer = real;
		this.real = integer.floatValue();
		integer = image;
		this.image = integer.floatValue();
	}

	public Complex(double real) {
		this.real = real;
		this.image = 0;
	}

	// �˷�
	public Complex cc(Complex complex) {
		Complex tmpComplex = new Complex();
		tmpComplex.real = this.real * complex.real - this.image * complex.image;
		tmpComplex.image = this.real * complex.image + this.image
				* complex.real;
		return tmpComplex;
	}

	// �ӷ�
	public Complex sum(Complex complex) {
		Complex tmpComplex = new Complex();
		tmpComplex.real = this.real + complex.real;
		tmpComplex.image = this.image + complex.image;
		return tmpComplex;
	}

	// ����
	public Complex cut(Complex complex) {
		Complex tmpComplex = new Complex();
		tmpComplex.real = this.real - complex.real;
		tmpComplex.image = this.image - complex.image;
		return tmpComplex;
	}

	// ���һ��������ֵ
	public double getDoubleValue() {
		double ret = 0;
		ret = (double) Math.round(Math.sqrt(this.real * this.real + this.image
				* this.image));
		return ret;
	}
}
