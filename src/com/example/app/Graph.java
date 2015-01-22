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
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;

public class Graph extends Grafica {
	private Context context;
	XYMultipleSeriesDataset dataset;
	XYMultipleSeriesRenderer renderer;
	double greater;
	double ejeymax;
	double ejeymin;

	public Graph(Context context) {
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public void initData2(ConcurrentLinkedQueue<AccelData2> sensorDatas) {
		long t = sensorDatas.peek().getTimestamp();

		XYSeries xSeries = new XYSeries("X");
		XYSeries modulo = new XYSeries("Modulo");

		for (AccelData2 data : sensorDatas) {
		
			long f = (data.getTimestamp() - t) / 1000;
			double d = ((data.getTimestamp() - t) % 1000) * 0.001;
			double fin = f + d;
			Log.d("tiempo", "timestamp: " + fin);
			xSeries.add(fin, data.getX());
			modulo.add(fin, data.getModulo());

			greater = fin;
		}

		// Log.d("sensordatas", "greaterr: "+ greater);
		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(modulo);

		renderer = new XYMultipleSeriesRenderer();
	}
	public void initData(ConcurrentLinkedQueue<AccelData> sensorDatas) {
		long t = sensorDatas.peek().getTimestamp();

		XYSeries xSeries = new XYSeries("X");
		XYSeries ySeries = new XYSeries("Y");
		XYSeries zSeries = new XYSeries("Z");
		XYSeries modulo = new XYSeries("Modulo");

		for (AccelData data : sensorDatas) {
			long f = (data.getTimestamp() - t) / 1000;
			double d = ((data.getTimestamp() - t) % 1000) * 0.001;
			double fin = f + d;
			Log.d("tiempo", "timestamp: " + fin);
			xSeries.add(fin, data.getX());
			ySeries.add(fin, data.getY());
			zSeries.add(fin, data.getZ());
			modulo.add(fin, data.getModulo());

			greater = fin;
			
		}

		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(ySeries);
		dataset.addSeries(zSeries);
		dataset.addSeries(modulo);

		renderer = new XYMultipleSeriesRenderer();
	}


	public void setProperties(boolean click[], String titulo) {
		double[] limites ={0,1000000,-20,20};
		double[] limites2 ={0,greater,-15,15};
		double[] limites3 ={greater-5,greater,-15,15};
		XYSeriesRenderer renderer1 = new XYSeriesRenderer();
		if (click[0] == true) {
			renderer1.setColor(Color.RED);
			renderer1.setLineWidth(1);
			renderer1.setDisplayChartValues(false);
			renderer.addSeriesRenderer(renderer1);
		} else {
			renderer1.setColor(0);
			renderer.addSeriesRenderer(renderer1);
		}
		
		XYSeriesRenderer renderer2 = new XYSeriesRenderer();

		if (click[1] == true) {
			renderer2.setColor(Color.GREEN);
			renderer.addSeriesRenderer(renderer2);
		} else {
			renderer2.setColor(0);
			renderer.addSeriesRenderer(renderer2);
		}
		
		XYSeriesRenderer renderer3 = new XYSeriesRenderer();
		if (click[2] == true) {
			renderer3.setColor(Color.BLUE);
			renderer.addSeriesRenderer(renderer3);
		} else {
			renderer3.setColor(0);
			renderer.addSeriesRenderer(renderer3);
		}
		
		XYSeriesRenderer modulo = new XYSeriesRenderer();
		if (click[3] == true) {
			modulo.setColor(Color.MAGENTA);
			renderer.addSeriesRenderer(modulo);
		} else {
			modulo.setColor(0);
			renderer.addSeriesRenderer(modulo);
		}
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setMarginsColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);
		// renderer.setXAxisMin(0.0);
		if (greater <= 5) {
			//renderer.setXAxisMax(5);
			renderer.setRange(limites2);
		} else {
			//renderer.setXAxisMin(greater - 5);
			//renderer.setXAxisMax(greater);
			renderer.setRange(limites3);
		}
		renderer.setGridColor(Color.DKGRAY);
		renderer.setShowGrid(true);
		renderer.setYTitle(titulo);
		renderer.setXTitle("Tiempo (segundos)");
		renderer.setXLabels(5);
		
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setPanLimits(limites);
		renderer.setYLabelsAlign(Paint.Align.RIGHT);
		renderer.setAxesColor(Color.WHITE);
		renderer.setLabelsColor(Color.RED);
	}

	public void setProperties2(boolean click[], String titulo) {
		double[] limites ={0,1000000,-5000,5000};
		XYSeriesRenderer renderer1 = new XYSeriesRenderer();
		if (click[0] == true) {
			renderer1.setColor(Color.RED);
			renderer1.setLineWidth(1);
			renderer1.setDisplayChartValues(false);
			renderer.addSeriesRenderer(renderer1);
		} else {
			renderer1.setColor(0);
			renderer.addSeriesRenderer(renderer1);
		}
		
		XYSeriesRenderer modulo = new XYSeriesRenderer();

		if (click[3] == true) {
			modulo.setColor(Color.MAGENTA);
			renderer.addSeriesRenderer(modulo);
		} else {
			modulo.setColor(0);
			renderer.addSeriesRenderer(modulo);
		}
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setMarginsColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);
		// renderer.setXAxisMin(0.0);
		if (greater <= 5) {
			renderer.setXAxisMax(5);
		} else {
			renderer.setXAxisMin(greater - 5);
			renderer.setXAxisMax(greater);
		}
		renderer.setGridColor(Color.DKGRAY);
		renderer.setShowGrid(true);
		renderer.setYTitle(titulo);
		renderer.setXTitle("Tiempo (segundos)");
		renderer.setXLabels(5);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setPanLimits(limites);
		renderer.setYLabelsAlign(Paint.Align.RIGHT);
		renderer.setAxesColor(Color.WHITE);
		renderer.setRange(limites);
		renderer.setLabelsColor(Color.RED);
	}

	public GraphicalView getGraph() {

		return ChartFactory.getLineChartView(context, dataset, renderer);

	}
}