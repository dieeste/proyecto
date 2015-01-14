package com.example.app;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Graph {
	private Context context;
	XYMultipleSeriesDataset dataset;
	XYMultipleSeriesRenderer renderer;
	double greater;
	public static boolean ClickEnabled = true;

	public Graph(Context context) {
		this.context = context;
	}

	public void initData(ConcurrentLinkedQueue<AccelData> sensorDatas) {
		long t = sensorDatas.peek().getTimestamp();

		XYSeries xSeries = new XYSeries("X");
		XYSeries ySeries = new XYSeries("Y");
		XYSeries zSeries = new XYSeries("Z");
		XYSeries modulo = new XYSeries("Modulo");

		for (AccelData data : sensorDatas) {
			// Log.d("sensordatas", "sensor: "+sensorDatas);
			Double dReal = new Double(Math.abs(Math.sqrt(Math.pow(
					data.getX(), 2)
					+ Math.pow(data.getY(), 2)
					+ Math.pow(data.getZ(), 2))));
			float fReal = dReal.floatValue();
			long f = (data.getTimestamp() - t) / 1000;
			double d = ((data.getTimestamp() - t) % 1000) * 0.001;
			double fin = f + d;
			Log.d("tiempo", "timestamp: " + fin);
			xSeries.add(fin, data.getX());
			ySeries.add(fin, data.getY());
			zSeries.add(fin, data.getZ());
			modulo.add(fin, fReal);

			greater = fin;
		}

		// Log.d("sensordatas", "greaterr: "+ greater);
		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(ySeries);
		dataset.addSeries(zSeries);
		dataset.addSeries(modulo);

		renderer = new XYMultipleSeriesRenderer();
	}

	public void setProperties() {
		// renderer.setClickEnabled(ClickEnabled);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setMarginsColor(Color.WHITE);
		renderer.setApplyBackgroundColor(true);
		// renderer.setXAxisMin(0.0);
		if (greater <= 5) {
			renderer.setXAxisMax(5);
		} else {
			renderer.setXAxisMin(greater - 5);
			renderer.setXAxisMax(greater);
		}
		renderer.setChartTitle("AccelerometerData ");
		renderer.setGridColor(Color.DKGRAY);
		renderer.setShowGrid(true);
		renderer.setYTitle("Acelerómetro");
		renderer.setXTitle("Tiempo (segundos)");
		renderer.setXLabels(5);
		renderer.setBackgroundColor(Color.BLACK);

		renderer.setYLabelsAlign(Paint.Align.RIGHT);
		renderer.setAxesColor(Color.BLACK);
		renderer.setLabelsColor(Color.RED);
		renderer.setZoomButtonsVisible(true);

		XYSeriesRenderer renderer1 = new XYSeriesRenderer();
		renderer1.setColor(Color.RED);

		// renderer1.setPointStyle(PointStyle.CIRCLE);
		// renderer1.setFillPoints(true);
		renderer1.setLineWidth(1);
		renderer1.setDisplayChartValues(false);
		renderer.addSeriesRenderer(renderer1);

		XYSeriesRenderer renderer2 = new XYSeriesRenderer();
		renderer2.setColor(Color.GREEN);
		renderer.addSeriesRenderer(renderer2);
		XYSeriesRenderer renderer3 = new XYSeriesRenderer();
		renderer3.setColor(Color.BLUE);
		renderer.addSeriesRenderer(renderer3);
		XYSeriesRenderer modulo = new XYSeriesRenderer();
		modulo.setColor(Color.MAGENTA);
		renderer.addSeriesRenderer(modulo);
	}

	public GraphicalView getGraph() {

		return ChartFactory.getLineChartView(context, dataset, renderer);

	}
}