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
		// 生成图表
		chart = ChartFactory.getCubeLineChartView(this, getDateDemoDataset(),
				getDemoRenderer(), 0.5f);
		layout.addView(chart, new LayoutParams(LayoutParams.WRAP_CONTENT, 380));
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 刷新图表
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
			// 保证长度为2的幂次数
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
		// 将 buffer 内容取出，进行平方和运算
		for (int i = 0; i < MainActivity.rec.length; i++) {
			v += MainActivity.rec[i] * MainActivity.rec[i];
		}
		// 平方和除以数据总长度，得到音量大小。
		double mean = v / (double) MainActivity.length;
		double volume = 10 * Math.log10(mean);
		TextView textView = (TextView) findViewById(R.id.volume);
		textView.setText("Volume: " + volume);

		buildBarDataset(temp);
		// 曲线更新
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
		setChartSettings(renderer, /* 渲染器 */
				"Record", /* 图表标题 */
				"Index", /* x轴标题 */
				"Value", /* y轴标题 */
				0, /* x轴最小值 */
				length, /* x轴最大值 */
				-32768, /* y轴最小值 */
				32767, /* y轴最大值 */
				Color.GRAY, /* 坐标轴颜色 */
				Color.LTGRAY); /* 标签颜色 标签即 图表标题 xy轴标题 */

		renderer.setXLabels(12); /* 设置 x 轴刻度个数 */
		renderer.setYLabels(10); /* 设置 y 轴刻度个数 */
		renderer.setChartTitleTextSize(20); /* 设置表格标题字体大小 */
		renderer.setTextTypeface("sans_serif", Typeface.BOLD); /* 设置字体 */
		renderer.setLabelsTextSize(14f); /* 设置字体大小 */
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
		// 当结束程序时关掉Timer
		timer.cancel();
		super.onDestroy();
	};

	/**
	 * 向上取最接近iint的2的幂次数.比如iint=320时,返回256
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

	// 快速傅里叶变换
	public void fft(Complex[] xin, int N) {
		int f, m, N2, nm, i, k, j, L;// L:运算级数
		float p;
		int e2, le, B, ip;
		Complex w = new Complex();
		Complex t = new Complex();
		N2 = N / 2;// 每一级中蝶形的个数,同时也代表m位二进制数最高位的十进制权值
		f = N;// f是为了求流程的级数而设立的
		for (m = 1; (f = f / 2) != 1; m++)
			; // 得到流程图的共几级
		nm = N - 2;
		j = N2;
		/****** 倒序运算——雷德算法 ******/
		for (i = 1; i <= nm; i++) {
			if (i < j)// 防止重复交换
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
		/****** 蝶形图计算部分 ******/
		for (L = 1; L <= m; L++) // 从第1级到第m级
		{
			e2 = (int) Math.pow(2, L);
			// e2=(int)2.pow(L);
			le = e2 + 1;
			B = e2 / 2;
			for (j = 0; j < B; j++) // j从0到2^(L-1)-1
			{
				p = 2 * pi / e2;
				w.real = Math.cos(p * j);
				// w.real=Math.cos((double)p*j); //系数W
				w.image = Math.sin(p * j) * -1;
				// w.imag = -sin(p*j);
				for (i = j; i < N; i = i + e2) // 计算具有相同系数的数据
				{
					ip = i + B; // 对应蝶形的数据间隔为2^(L-1)
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

	// 三个构造函数
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

	// 乘法
	public Complex cc(Complex complex) {
		Complex tmpComplex = new Complex();
		tmpComplex.real = this.real * complex.real - this.image * complex.image;
		tmpComplex.image = this.real * complex.image + this.image
				* complex.real;
		return tmpComplex;
	}

	// 加法
	public Complex sum(Complex complex) {
		Complex tmpComplex = new Complex();
		tmpComplex.real = this.real + complex.real;
		tmpComplex.image = this.image + complex.image;
		return tmpComplex;
	}

	// 减法
	public Complex cut(Complex complex) {
		Complex tmpComplex = new Complex();
		tmpComplex.real = this.real - complex.real;
		tmpComplex.image = this.image - complex.image;
		return tmpComplex;
	}

	// 获得一个复数的值
	public double getDoubleValue() {
		double ret = 0;
		ret = (double) Math.round(Math.sqrt(this.real * this.real + this.image
				* this.image));
		return ret;
	}
}
