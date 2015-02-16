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

public class Graph extends Grafica {

	private Context context;
	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	double maxejex;
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

	public void ejeY(ConcurrentLinkedQueue<AccelData> sensorDatas) {

		// se utiliza para el giroscopio, acelerometro y magnetometro, no hace
		// el maximo y el minimo del eje Y, es decir centra la grafica.

		double max = renderer.getYAxisMax();
		double min = renderer.getYAxisMin();
		for (AccelData data : sensorDatas) {
			if (data.getX() > max)
				max = data.getX();
			if (data.getY() > max)
				max = data.getY();
			if (data.getZ() > max)
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

		// se utiliza para el sensor de luz y el sensor de proximidad, centra la
		// grafica en el max y min del eje y.

		double max = renderer.getYAxisMax();
		double min = renderer.getYAxisMin();
		for (AccelData2 data : sensorDatas) {
			if (data.getX() > max)
				max = data.getX();
			if (data.getX() < min)
				min = data.getModulo();
		}
		renderer.setYAxisMax(max + 1);
		renderer.setYAxisMin(min - 1);
	}

	public void ejeX(ConcurrentLinkedQueue<AccelData> sensorDatas) {

		// recoge el tiempo del vector sensorDatas giroscopio, acelerometro y
		// magnetometro y fija el maximo del eje x.

		double t = sensorDatas.peek().getTimestamp();
		for (AccelData data : sensorDatas) {
			double tiempo = (data.getTimestamp() - t) / 1000;
			maxejex = tiempo;
		}
		if (maxejex <= 5) {
			renderer.setXAxisMax(5);
		} else {
			renderer.setXAxisMin(maxejex - 5);
			renderer.setXAxisMax(maxejex);
		}
	}

	public void ejeX2(ConcurrentLinkedQueue<AccelData2> sensorDatas) {

		// recoge el tiempo del vector sensorDatas sensor proximidad y sensor
		// luz
		// y fija el maximo del eje y.

		double t = sensorDatas.peek().getTimestamp();
		for (AccelData2 data : sensorDatas) {
			double tiempo = (data.getTimestamp() - t) / 1000;
			maxejex = tiempo;
		}
		if (maxejex <= 5) {
			renderer.setXAxisMax(5);
		} else {
			renderer.setXAxisMin(maxejex - 5);
			renderer.setXAxisMax(maxejex);
		}
	}

	public void initData(ConcurrentLinkedQueue<AccelData> sensorDatas) {

		// recoge los valores de los sensores de sensor datas y los añade a
		// XYMULTISERIES (acelerometro, magnetometro y giroscopio)

		double t = sensorDatas.peek().getTimestamp();
		XYSeries xSeries = new XYSeries("X");
		XYSeries ySeries = new XYSeries("Y");
		XYSeries zSeries = new XYSeries("Z");
		XYSeries modulo = new XYSeries("Modulo");
		for (AccelData data : sensorDatas) {
			double tiempo = (data.getTimestamp() - t) / 1000;
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

	public void initData2(ConcurrentLinkedQueue<AccelData2> sensorDatas) {

		// recoge los valores de los sensores de sensor datas y los añade a
		// XYMULTISERIES (sensor luz y proximidad)

		double t = sensorDatas.peek().getTimestamp();
		XYSeries xSeries = new XYSeries("X");
		XYSeries modulo = new XYSeries("Modulo");
		for (AccelData2 data : sensorDatas) {
			double tiempo = (data.getTimestamp() - t) / 1000;
			xSeries.add(tiempo, data.getX());
			modulo.add(tiempo, data.getModulo());
		}
		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(modulo);
	}

	public void setProperties(boolean click[], String titulo, String calidad,
			String tamano) {

		// añade las propiedades de la grafica

		double[] limites = { 0, maxejex + 5, ejeymin - 20, ejeymax + 20 };// limites
																			// de
																			// la
																			// grafica.
		int[] margenes = { 50, 50, 50, 50 };
		int[] margenesbaja = { 30, 30, 30, 30 };
		XYSeriesRenderer valoresX = new XYSeriesRenderer();
		if (click[0] == true) { // checkboxX
			valoresX.setColor(Color.RED);
			renderer.addSeriesRenderer(valoresX);
		} else {
			valoresX.setColor(0);
			renderer.addSeriesRenderer(valoresX);
		}
		XYSeriesRenderer valoresY = new XYSeriesRenderer();
		if (click[1] == true) { // checkboxY
			valoresY.setColor(Color.GREEN);
			renderer.addSeriesRenderer(valoresY);
		} else {
			valoresY.setColor(0);
			renderer.addSeriesRenderer(valoresY);
		}
		XYSeriesRenderer valoresZ = new XYSeriesRenderer();
		if (click[2] == true) { // checkboxZ
			valoresZ.setColor(Color.WHITE);
			renderer.addSeriesRenderer(valoresZ);
		} else {
			valoresZ.setColor(0);
			renderer.addSeriesRenderer(valoresZ);
		}
		XYSeriesRenderer modulo = new XYSeriesRenderer();
		if (click[3] == true) { // checkbox modulo
			modulo.setColor(Color.MAGENTA);
			renderer.addSeriesRenderer(modulo);
		} else {
			modulo.setColor(0);
			renderer.addSeriesRenderer(modulo);
		}
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setMarginsColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);
		if (tamano.equalsIgnoreCase("grande")) {
			renderer.setMargins(margenes);
		}
		if (tamano.equalsIgnoreCase("pequena")) {
			renderer.setMargins(margenes);
		}
		if (tamano.equalsIgnoreCase("normal")) {
			renderer.setMargins(margenesbaja);
		}
		if (tamano.equalsIgnoreCase("extra")) {
			renderer.setMargins(margenes);
		}
		if (calidad.equalsIgnoreCase("alta")) {
			renderer.setLabelsTextSize(30);
			renderer.setLabelsTextSize(30);
			renderer.setAxisTitleTextSize(30);
			renderer.setChartTitleTextSize(30);

			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
		} else if (calidad.equalsIgnoreCase("media")) {
			renderer.setLabelsTextSize(15);
			renderer.setLabelsTextSize(15);
			renderer.setAxisTitleTextSize(15);
			renderer.setChartTitleTextSize(15);

			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
		} else if (calidad.equalsIgnoreCase("baja")) {
			renderer.setLabelsTextSize(10);
			renderer.setLabelsTextSize(10);
			renderer.setAxisTitleTextSize(10);
			renderer.setChartTitleTextSize(10);

			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
		} else if (calidad.equalsIgnoreCase("xhigh")) {
			renderer.setLabelsTextSize(20);
			renderer.setLabelsTextSize(20);
			renderer.setAxisTitleTextSize(30);
			renderer.setChartTitleTextSize(30);

			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
		} else if (calidad.equalsIgnoreCase("xxhigh")) {
			renderer.setLabelsTextSize(35);
			renderer.setLabelsTextSize(35);
			renderer.setAxisTitleTextSize(35);
			renderer.setChartTitleTextSize(35);

			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
		} else if (calidad.equalsIgnoreCase("xxxhigh")) {
			renderer.setLabelsTextSize(40);
			renderer.setLabelsTextSize(40);
			renderer.setAxisTitleTextSize(40);
			renderer.setChartTitleTextSize(40);

			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
		}
		renderer.setChartTitle(titulo);
		renderer.setGridColor(Color.DKGRAY);
		renderer.setShowGrid(true);
		renderer.setYTitle(titulo);
		renderer.setXTitle("Tiempo (s)");
		renderer.setXLabels(5);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setPanLimits(limites); // añade los limites de la grafica
		renderer.setYLabelsAlign(Paint.Align.RIGHT);
		renderer.setAxesColor(Color.WHITE);
		renderer.setLabelsColor(Color.YELLOW);
	}

	public void setProperties2(boolean click[], String titulo) {

		// añade las propiedades de la grafica sensor de luz y sensor proximidad

		double[] limites = { 0, maxejex + 5, ejeymin - 20, ejeymax + 20 };
		XYSeriesRenderer valoresX = new XYSeriesRenderer();
		if (click[0] == true) {
			valoresX.setColor(Color.RED);
			renderer.addSeriesRenderer(valoresX);
		} else {
			valoresX.setColor(0);
			renderer.addSeriesRenderer(valoresX);
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
			maxejex = tiempo;
		}

		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(ySeries);
		dataset.addSeries(zSeries);
		dataset.addSeries(modulo);

	}

	public void propiedades(String titulo) {
		double[] limites = { 0, maxejex + 1, ejeymin - 20, ejeymax + 20 };
		XYSeriesRenderer valoresX = new XYSeriesRenderer();
		valoresX.setColor(Color.RED);
		valoresX.setLineWidth(1);
		valoresX.setDisplayChartValues(false);

		renderer.addSeriesRenderer(valoresX);

		XYSeriesRenderer valoresY = new XYSeriesRenderer();

		valoresY.setColor(Color.GREEN);
		renderer.addSeriesRenderer(valoresY);

		XYSeriesRenderer valoresZ = new XYSeriesRenderer();
		valoresZ.setColor(Color.WHITE);
		renderer.addSeriesRenderer(valoresZ);

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
			maxejex = tiempo;
		}

		dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(xSeries);
		dataset.addSeries(modulo);

	}

	public void propiedades2(String titulo) {
		double[] limites = { 0, maxejex + 1, ejeymin - 20, ejeymax + 20 };
		XYSeriesRenderer valoresX = new XYSeriesRenderer();
		valoresX.setColor(Color.RED);
		valoresX.setLineWidth(1);
		valoresX.setDisplayChartValues(false);
		renderer.addSeriesRenderer(valoresX);

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

	public GraphicalView getGraph() {

		return ChartFactory.getLineChartView(context, dataset, renderer);

	}
}