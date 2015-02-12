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
import android.os.Bundle;
import android.widget.TextView;

public class Graph extends Grafica {
	private Context context;
	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	double greater;
	double ejeymax;
	double ejeymin;
	public static final int ZOOM_AXIS_X = 1;
	public static final int ZOOM_AXIS_Y = 2;
	

	public Graph(Context context) {
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		float textSize = new TextView(this).getTextSize();
		float upscale = textSize / renderer.getLegendTextSize();
		renderer.setLabelsTextSize(textSize);
		renderer.setLegendTextSize(textSize);
		renderer.setChartTitleTextSize(textSize);
		renderer.setAxisTitleTextSize(textSize);
		renderer.setFitLegend(true);
		int[] margins = renderer.getMargins();
		margins[0] *= upscale;
		margins[1] *= upscale;
		margins[2] = (int) (2 * renderer.getLegendTextSize());
		renderer.setMargins(margins);
	}

	public void ejeY(ConcurrentLinkedQueue<AccelData> sensorDatas) {
		double max = renderer.getYAxisMax();
		double min = renderer.getYAxisMin();
		for (AccelData data : sensorDatas) {
			if (data.getX() > max)
				max = data.getX();
			if (data.getY() > max)
				max = data.getY();
			if (data.getX() > max)
				max = data.getZ();
			if (data.getModulo() > max)
				max = data.getModulo();

			if (data.getX() < min)
				min = data.getX();
			if (data.getY() < min)
				min = data.getY();
			if (data.getZ() < min)
				min = data.getZ();
		}
		renderer.setYAxisMax(max + 1);
		renderer.setYAxisMin(min - 1);
	}

	public void ejeY2(ConcurrentLinkedQueue<AccelData2> sensorDatas) {
		double max = renderer.getYAxisMax();
		double min = renderer.getYAxisMin();
		for (AccelData2 data : sensorDatas) {
			if (data.getX() > max)
				max = data.getX();
			if (data.getModulo() > max)
				max = data.getModulo();
			if (data.getModulo() < min)
				min = data.getModulo();
		}
		renderer.setYAxisMax(max + 1);
		renderer.setYAxisMin(min - 1);
	}

	public void ejeX(ConcurrentLinkedQueue<AccelData> sensorDatas) {
		double t = sensorDatas.peek().getTimestamp();
		for (AccelData data : sensorDatas) {

			double tiempo = (data.getTimestamp() - t) / 1000;

			greater = tiempo;

		}
		if (greater <= 5) {
			renderer.setXAxisMax(5);
			// renderer.setRange(limites2);
		} else {
			renderer.setXAxisMin(greater - 5);
			renderer.setXAxisMax(greater);
			// renderer.setRange(limites3);
		}
	}

	public void ejeX2(ConcurrentLinkedQueue<AccelData2> sensorDatas) {
		double t = sensorDatas.peek().getTimestamp();
		for (AccelData2 data : sensorDatas) {

			double tiempo = (data.getTimestamp() - t) / 1000;

			greater = tiempo;

		}
		if (greater <= 5) {
			renderer.setXAxisMax(5);
			// renderer.setRange(limites2);
		} else {
			renderer.setXAxisMin(greater - 5);
			renderer.setXAxisMax(greater);
			// renderer.setRange(limites3);
		}
	}

	public void initData2(ConcurrentLinkedQueue<AccelData2> sensorDatas) {
		double t = sensorDatas.peek().getTimestamp();

		XYSeries xSeries = new XYSeries("X");
		XYSeries modulo = new XYSeries("Modulo");

		for (AccelData2 data : sensorDatas) {

			double tiempo = (data.getTimestamp() - t) / 1000;

			// Log.d("tiempo", "timestamp: " + tiempo);
			xSeries.add(tiempo, data.getX());
			modulo.add(tiempo, data.getModulo());
		}

		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(modulo);

	}

	public void initData(ConcurrentLinkedQueue<AccelData> sensorDatas) {
		double t = sensorDatas.peek().getTimestamp();
		XYSeries xSeries = new XYSeries("X");
		XYSeries ySeries = new XYSeries("Y");
		XYSeries zSeries = new XYSeries("Z");
		XYSeries modulo = new XYSeries("Modulo");

		for (AccelData data : sensorDatas) {
			double tiempo = (data.getTimestamp() - t) / 1000;

			// Log.d("tiempo", "timestamp: " + tiempo);
			xSeries.add(tiempo, data.getX());
			ySeries.add(tiempo, data.getY());
			zSeries.add(tiempo, data.getZ());
			modulo.add(tiempo, data.getModulo());

		}

		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(ySeries);
		dataset.addSeries(zSeries);
		dataset.addSeries(modulo);

	}

	public void setProperties(boolean click[], String titulo) {
		double[] limites = { 0, greater+1, ejeymin-20, ejeymax+20};
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
			renderer3.setColor(Color.WHITE);
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

		/*
		 * if (densidad.equalsIgnoreCase("alta")){
		 * renderer.setLabelsTextSize(30); renderer.setLabelsTextSize(30); }else
		 * if(densidad.equalsIgnoreCase("media")) {
		 * renderer.setLabelsTextSize(20); renderer.setLabelsTextSize(20); }
		 * else if (densidad.equalsIgnoreCase("baja")){
		 * renderer.setLabelsTextSize(5); renderer.setLabelsTextSize(5); }
		 */
		// renderer.setXAxisMin(0.0);

		// float upscale = textSize / renderer.getLegendTextSize();
		renderer.setChartTitle(titulo);
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
		renderer.setZoomEnabled(true);
		// renderer.setz
		renderer.setZoomButtonsVisible(true);
	}

	public void setProperties2(boolean click[], String titulo) {
		double[] limites = { 0, greater+1, ejeymin-20, ejeymax+20};
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
		renderer.setChartTitle(titulo);
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
		renderer.setZoomEnabled(true);
	}

	public void iniciar(ConcurrentLinkedQueue<AccelData> sensorDatas) {
		XYSeries xSeries = new XYSeries("X");
		XYSeries ySeries = new XYSeries("Y");
		XYSeries zSeries = new XYSeries("Z");
		XYSeries modulo = new XYSeries("Modulo");

		for (AccelData data : sensorDatas) {

			double tiempo = (data.getTimestamp());

			xSeries.add(tiempo, data.getX());
			ySeries.add(tiempo, data.getY());
			zSeries.add(tiempo, data.getZ());
			modulo.add(tiempo, data.getModulo());
			greater=tiempo;
		}

		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(ySeries);
		dataset.addSeries(zSeries);
		dataset.addSeries(modulo);

	}

	public void propiedades(String titulo) {
		double[] limites = { 0, greater+1, ejeymin-20, ejeymax+20};
		XYSeriesRenderer renderer1 = new XYSeriesRenderer();
		renderer1.setColor(Color.RED);
		renderer1.setLineWidth(1);
		renderer1.setDisplayChartValues(false);

		renderer.addSeriesRenderer(renderer1);

		XYSeriesRenderer renderer2 = new XYSeriesRenderer();

		renderer2.setColor(Color.GREEN);
		renderer.addSeriesRenderer(renderer2);

		XYSeriesRenderer renderer3 = new XYSeriesRenderer();
		renderer3.setColor(Color.WHITE);
		renderer.addSeriesRenderer(renderer3);

		XYSeriesRenderer modulo = new XYSeriesRenderer();
		modulo.setColor(Color.MAGENTA);
		renderer.addSeriesRenderer(modulo);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setMarginsColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);

		// renderer.setXAxisMin(0.0);
		renderer.setChartTitle(titulo);
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

	public void iniciar2(ConcurrentLinkedQueue<AccelData2> sensorDatas) {
		XYSeries xSeries = new XYSeries("X");
		XYSeries modulo = new XYSeries("Modulo");

		for (AccelData2 data : sensorDatas) {

			double tiempo = (data.getTimestamp());

			xSeries.add(tiempo, data.getX());
			modulo.add(tiempo, data.getModulo());
			greater=tiempo;
		}

		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(modulo);

	}

	public void propiedades2(String titulo) {
		double[] limites = { 0, greater+1, ejeymin-20, ejeymax+20};
		XYSeriesRenderer renderer1 = new XYSeriesRenderer();
		renderer1.setColor(Color.RED);
		renderer1.setLineWidth(1);
		renderer1.setDisplayChartValues(false);
		renderer.addSeriesRenderer(renderer1);

		XYSeriesRenderer modulo = new XYSeriesRenderer();
		modulo.setColor(Color.MAGENTA);
		renderer.addSeriesRenderer(modulo);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setMarginsColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);
		// renderer.setXAxisMin(0.0);
		renderer.setChartTitle(titulo);
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

	public void propiedadesParado(ConcurrentLinkedQueue<AccelData> sensor,
			boolean click[], String titulo) {
		
		
		double[] limites = { 0, 1000000, -2000, 2000 };
		XYSeriesRenderer renderer1 = new XYSeriesRenderer();
		if (click[0] == true) {
			XYSeries xSeries = new XYSeries("X");
			for (AccelData data : sensor) {
				double tiempo = (data.getTimestamp());
				xSeries.add(tiempo, data.getX());
			}
			dataset.addSeries(xSeries);
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
			XYSeries ySeries = new XYSeries("Y");
			for (AccelData data : sensor) {
				double tiempo = (data.getTimestamp());
				ySeries.add(tiempo, data.getY());
			}
			dataset.addSeries(ySeries);
			renderer2.setColor(Color.GREEN);
			renderer.addSeriesRenderer(renderer2);
		} else {
			renderer2.setColor(0);
			renderer.addSeriesRenderer(renderer2);
		}

		XYSeriesRenderer renderer3 = new XYSeriesRenderer();
		if (click[2] == true) {
			XYSeries zSeries = new XYSeries("Z");
			for (AccelData data : sensor) {
				double tiempo = (data.getTimestamp());
				zSeries.add(tiempo, data.getZ());
			}
			dataset.addSeries(zSeries);
			renderer3.setColor(Color.WHITE);
			renderer.addSeriesRenderer(renderer3);
		} else {
			renderer3.setColor(0);
			renderer.addSeriesRenderer(renderer3);
		}

		XYSeriesRenderer modulo = new XYSeriesRenderer();
		if (click[3] == true) {
			XYSeries xmodulo = new XYSeries("Modulo");
			for (AccelData data : sensor) {
				double tiempo = (data.getTimestamp());
				xmodulo.add(tiempo, data.getModulo());
			}
			dataset.addSeries(xmodulo);
			modulo.setColor(Color.MAGENTA);
			renderer.addSeriesRenderer(modulo);
		} else {
			modulo.setColor(0);
			renderer.addSeriesRenderer(modulo);
		}

		

		dataset = new XYMultipleSeriesDataset();
		
		
		
		
		
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setMarginsColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);

		// renderer.setXAxisMin(0.0);

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
		//renderer.setZoomEnabled(true);
		//renderer.apply(ZOOM_AXIS_Y);
		renderer.setZoomButtonsVisible(true);
	}

	public GraphicalView getGraph() {

		return ChartFactory.getLineChartView(context, dataset, renderer);

	}
}