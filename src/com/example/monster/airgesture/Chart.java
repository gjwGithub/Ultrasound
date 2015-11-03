package com.example.monster.airgesture;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.chartdemo.demo.chart.AbstractDemoChart;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer.FillOutsideLine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

/**
 * 销售额对比图表
 */
public class Chart extends AbstractDemoChart {
	/**
	 * 获取图表名称
	 * 
	 * @return 图表名称
	 */
	public String getName() {
		return "销售额对比";
	}

	/**
	 * 获取图表的描述信息
	 * 
	 * @return 返回图表的详细信息
	 */
	public String getDesc() {
		return "2年内的月度销售额发展 (插入了 折线图 和 面积图)";
	}

	/**
	 * 获取图表
	 * 
	 * @param context 上下文对象
	 * @return the built intent
	 */
	public Intent execute(Context context) {
		String[] titles = new String[] { "2008 年销售额", "2007 年销售额",
		"2008年销售额与2007年对比" };

		/* 初始化数据集 */
		List<double[]> values = new ArrayList<double[]>();
		/* 2008年销售额 */
		values.add(new double[] { 14230, 12300, 14240, 15244, 14900, 12200, 11030, 12000, 12500, 15500,
				14600, 15000 });
		/* 2007年销售额 */
		values.add(new double[] { 10230, 10900, 11240, 12540, 13500, 14200, 12530, 11200, 10500, 12500,
				11600, 13500 });

		/* 计算出两年销售额的对比差 2008年 减去 2007年 */
		int length = values.get(0).length;
		double[] diff = new double[length];
		for (int i = 0; i < length; i++) {
			diff[i] = values.get(0)[i] - values.get(1)[i];
		}
		values.add(diff);

		/* 第一条线 蓝色 08年销售额, 第二条线 蓝绿色 07年销售额, 第三个面积图 绿色 两年销售额对比 */
		int[] colors = new int[] { Color.BLUE, Color.CYAN, Color.GREEN };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT, PointStyle.POINT };

		/* 创建图表渲染器 */
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		setChartSettings(renderer,  /* 渲染器 */
				"两年内的月度销售额", 	/* 图表标题 */
				"月份", 				/* x轴标题 */
				"销售单位", 			/* y轴标题 */
				0.75,				/* x轴最小值 */
				12.25, 				/* x轴最大值 */
				-5000, 				/* y轴最小值 */
				19000, 				/* y轴最大值 */
				Color.GRAY, 		/* 坐标轴颜色 */
				Color.LTGRAY);		/* 标签颜色 标签即 图表标题 xy轴标题 */

		renderer.setXLabels(12);								/* 设置 x 轴刻度个数 */
		renderer.setYLabels(10);								/* 设置 y 轴刻度个数 */
		renderer.setChartTitleTextSize(20);						/* 设置表格标题字体大小 */
		renderer.setTextTypeface("sans_serif", Typeface.BOLD);	/* 设置字体 */
		renderer.setLabelsTextSize(14f);						/*  */
		renderer.setAxisTitleTextSize(15);
		renderer.setLegendTextSize(15);
		length = renderer.getSeriesRendererCount();

		for (int i = 0; i < length; i++) {
			/* 获取具体的 渲染器 */
			XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
			if (i == length - 1) {
				/* 单独对面积图渲染器进行设置 */
				FillOutsideLine fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ALL);
				fill.setColor(Color.GREEN);
				seriesRenderer.addFillOutsideLine(fill);
			}

			/* 设置折线图渲染器 */
			seriesRenderer.setLineWidth(2.5f);
			seriesRenderer.setDisplayChartValues(true);
			seriesRenderer.setChartValuesTextSize(10f);
		}
		return ChartFactory.getCubicLineChartIntent(context, buildBarDataset(titles, values), renderer,
				0.5f);
	}
}
