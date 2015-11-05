package com.example.monster.airgesture;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class Chart extends Activity {

	private Timer timer = new Timer();
	private GraphicalView chart;
	private TimerTask task;
	private int addY = -1;
	private long addX;
	/** 曲线数量 */
	private static final int SERIES_NR = 1;
	private static final String TAG = "message";
	private TimeSeries series1;
	private XYMultipleSeriesDataset dataset1;
	private Handler handler;
	/** 时间数据 */
	double[] xcache = new double[4096];
	/** 数据 */
	int[] ycache = new int[4096];

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
		timer.schedule(task, 1000, 1000);
	}

	private void updateChart() {
//		// 设定长度为4096
//		int length = series1.getItemCount();
//		if (length >= 4096)
//			length = 4096;
//		addY = random.nextInt() % 10;
//		addX = new Date().getTime();
//
//		// 将前面的点放入缓存
//		for (int i = 0; i < length; i++) {
//			xcache[i] = new Date((long) series1.getX(i));
//			ycache[i] = (int) series1.getY(i);
//		}
//		series1.clear();
//		series1.add(new Date(addX), addY);
//		for (int k = 0; k < length; k++) {
//			series1.add(xcache[k], ycache[k]);
//		}
//		// 在数据集中添加新的点集
//		dataset1.removeSeries(series1);
//		dataset1.addSeries(series1);

		String[] titles = new String[] { "Chart" };
		List<double[]> values = new ArrayList<double[]>();
		double[] temp = new double[4096];
		for (int i = 0; i < 4096; i++) {
			temp[i] = (double) MainActivity.rec[i];
		}
		values.add(temp);
		dataset1 = buildBarDataset(titles, values);
		// 曲线更新
		chart.invalidate();
	}

	protected XYMultipleSeriesDataset buildBarDataset(String[] titles,
			List<double[]> values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
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
				4096, /* x轴最大值 */
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
		dataset1 = new XYMultipleSeriesDataset();
		return dataset1;
	}

	@Override
	public void onDestroy() {
		// 当结束程序时关掉Timer
		timer.cancel();
		super.onDestroy();
	};
}
