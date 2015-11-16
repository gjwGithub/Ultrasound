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

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Chart extends Activity {

	private Timer timer = new Timer();
	private GraphicalView chart;
	private TimerTask task;
	private CategorySeries series1;
	private XYMultipleSeriesDataset dataset1;
	private Handler handler;

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
		double[] temp = new double[4096];
		for (int i = 0; i < 4096; i++) {
			temp[i] = (double) MainActivity.rec[i];
		}
		long v = 0;
        // 将 buffer 内容取出，进行平方和运算
        for (int i = 0; i < MainActivity.rec.length; i++) {
          v += MainActivity.rec[i] * MainActivity.rec[i];
        }
        // 平方和除以数据总长度，得到音量大小。
        double mean = v / (double) MainActivity.length;
        double volume = 10 * Math.log10(mean);
        TextView textView=(TextView)findViewById(R.id.volume);
        textView.setText("Volume: "+volume);
        
		buildBarDataset(temp);
		// 曲线更新
		chart.invalidate();
	}

	protected void buildBarDataset(double[] values) {
		dataset1.clear();
		String title = new String("Chart");
		series1=new CategorySeries(title);
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
}
