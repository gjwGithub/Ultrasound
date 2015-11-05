package com.example.monster.airgesture;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
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
public class ChartOld extends AbstractDemoChart {
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

	private short[] data;
	public ChartOld(short[] data){
		this.data=data;
	}
	
	/**
	 * ��ȡͼ��
	 * 
	 * @param context �����Ķ���
	 * @return the built intent
	 */
	public Intent execute(Context context) {
		String[] titles = new String[] { "Chart"};

//		/* ��ʼ�����ݼ� */
//		List<double[]> values = new ArrayList<double[]>();
//		/* 2008�����۶� */
//		values.add(new double[] { 14230, 12300, 14240, 15244, 14900, 12200, 11030, 12000, 12500, 15500,
//				14600, 15000 });
//		/* 2007�����۶� */
//		values.add(new double[] { 10230, 10900, 11240, 12540, 13500, 14200, 12530, 11200, 10500, 12500,
//				11600, 13500 });
//
//		/* ������������۶�ĶԱȲ� 2008�� ��ȥ 2007�� */
//		int length = values.get(0).length;
//		double[] diff = new double[length];
//		for (int i = 0; i < length; i++) {
//			diff[i] = values.get(0)[i] - values.get(1)[i];
//		}
//		values.add(diff);

		List<double[]> values = new ArrayList<double[]>();
		double[] temp=new double[4096];
		for(int i=0;i<4096;i++){
			temp[i]=(double)data[i];
		}
		values.add(temp);
		
		/* ��һ���� ��ɫ 08�����۶�, �ڶ����� ����ɫ 07�����۶�, ���������ͼ ��ɫ �������۶�Ա� */
		int[] colors = new int[] { Color.BLUE };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT };

		/* ����ͼ����Ⱦ�� */
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		setChartSettings(renderer,  /* ��Ⱦ�� */
				"Record", 	        /* ͼ����� */
				"Index", 			/* x����� */
				"Value", 			/* y����� */
				0,				    /* x����Сֵ */
				4096, 				/* x�����ֵ */
				-32768, 			/* y����Сֵ */
				32767, 				/* y�����ֵ */
				Color.GRAY, 		/* ��������ɫ */
				Color.LTGRAY);		/* ��ǩ��ɫ ��ǩ�� ͼ����� xy����� */

		renderer.setXLabels(12);								/* ���� x ��̶ȸ��� */
		renderer.setYLabels(10);								/* ���� y ��̶ȸ��� */
		renderer.setChartTitleTextSize(20);						/* ���ñ����������С */
		renderer.setTextTypeface("sans_serif", Typeface.BOLD);	/* �������� */
		renderer.setLabelsTextSize(14f);						/* ���������С */
		renderer.setAxisTitleTextSize(15);
		renderer.setLegendTextSize(15);
		int length = renderer.getSeriesRendererCount();

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
