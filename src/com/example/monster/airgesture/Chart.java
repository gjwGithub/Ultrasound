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
 * ���۶�Ա�ͼ��
 */
public class Chart extends AbstractDemoChart {
	/**
	 * ��ȡͼ������
	 * 
	 * @return ͼ������
	 */
	public String getName() {
		return "���۶�Ա�";
	}

	/**
	 * ��ȡͼ���������Ϣ
	 * 
	 * @return ����ͼ�����ϸ��Ϣ
	 */
	public String getDesc() {
		return "2���ڵ��¶����۶չ (������ ����ͼ �� ���ͼ)";
	}

	/**
	 * ��ȡͼ��
	 * 
	 * @param context �����Ķ���
	 * @return the built intent
	 */
	public Intent execute(Context context) {
		String[] titles = new String[] { "2008 �����۶�", "2007 �����۶�",
		"2008�����۶���2007��Ա�" };

		/* ��ʼ�����ݼ� */
		List<double[]> values = new ArrayList<double[]>();
		/* 2008�����۶� */
		values.add(new double[] { 14230, 12300, 14240, 15244, 14900, 12200, 11030, 12000, 12500, 15500,
				14600, 15000 });
		/* 2007�����۶� */
		values.add(new double[] { 10230, 10900, 11240, 12540, 13500, 14200, 12530, 11200, 10500, 12500,
				11600, 13500 });

		/* ������������۶�ĶԱȲ� 2008�� ��ȥ 2007�� */
		int length = values.get(0).length;
		double[] diff = new double[length];
		for (int i = 0; i < length; i++) {
			diff[i] = values.get(0)[i] - values.get(1)[i];
		}
		values.add(diff);

		/* ��һ���� ��ɫ 08�����۶�, �ڶ����� ����ɫ 07�����۶�, ���������ͼ ��ɫ �������۶�Ա� */
		int[] colors = new int[] { Color.BLUE, Color.CYAN, Color.GREEN };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT, PointStyle.POINT };

		/* ����ͼ����Ⱦ�� */
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		setChartSettings(renderer,  /* ��Ⱦ�� */
				"�����ڵ��¶����۶�", 	/* ͼ����� */
				"�·�", 				/* x����� */
				"���۵�λ", 			/* y����� */
				0.75,				/* x����Сֵ */
				12.25, 				/* x�����ֵ */
				-5000, 				/* y����Сֵ */
				19000, 				/* y�����ֵ */
				Color.GRAY, 		/* ��������ɫ */
				Color.LTGRAY);		/* ��ǩ��ɫ ��ǩ�� ͼ����� xy����� */

		renderer.setXLabels(12);								/* ���� x ��̶ȸ��� */
		renderer.setYLabels(10);								/* ���� y ��̶ȸ��� */
		renderer.setChartTitleTextSize(20);						/* ���ñ����������С */
		renderer.setTextTypeface("sans_serif", Typeface.BOLD);	/* �������� */
		renderer.setLabelsTextSize(14f);						/*  */
		renderer.setAxisTitleTextSize(15);
		renderer.setLegendTextSize(15);
		length = renderer.getSeriesRendererCount();

		for (int i = 0; i < length; i++) {
			/* ��ȡ����� ��Ⱦ�� */
			XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
			if (i == length - 1) {
				/* ���������ͼ��Ⱦ���������� */
				FillOutsideLine fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ALL);
				fill.setColor(Color.GREEN);
				seriesRenderer.addFillOutsideLine(fill);
			}

			/* ��������ͼ��Ⱦ�� */
			seriesRenderer.setLineWidth(2.5f);
			seriesRenderer.setDisplayChartValues(true);
			seriesRenderer.setChartValuesTextSize(10f);
		}
		return ChartFactory.getCubicLineChartIntent(context, buildBarDataset(titles, values), renderer,
				0.5f);
	}
}
