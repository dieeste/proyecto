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

	public void setProperties(boolean ex, boolean ey, boolean ez, boolean em,
			String titulografica, String tituloejey, String calidad,
			String tamano) {
		// añade las propiedades de la grafica
		double[] limites = { 0, maxejex + 5, ejeymin - 200, ejeymax + 200 };// limites
		// utilizamos diferentes márgenes para las diferentes pantallas
		int[] margenes = { 70, 80, 70, 60 };
		int[] margenesnormal = { 50, 100, 70, 40 };
		int[] margenespeque = { 30, 40, 30, 20 };
		int[] margenesextra = { 70, 80, 70, 60 };
		XYSeriesRenderer valoresX = new XYSeriesRenderer();
		if (ex == true) { // checkboxX
			valoresX.setColor(Color.RED);
			renderer.addSeriesRenderer(valoresX);
		} else {
			valoresX.setColor(0);
			renderer.addSeriesRenderer(valoresX);
		}
		XYSeriesRenderer valoresY = new XYSeriesRenderer();
		if (ey == true) { // checkboxY
			valoresY.setColor(Color.GREEN);
			renderer.addSeriesRenderer(valoresY);
		} else {
			valoresY.setColor(0);
			renderer.addSeriesRenderer(valoresY);
		}
		XYSeriesRenderer valoresZ = new XYSeriesRenderer();
		if (ez == true) { // checkboxZ
			valoresZ.setColor(Color.WHITE);
			renderer.addSeriesRenderer(valoresZ);
		} else {
			valoresZ.setColor(0);
			renderer.addSeriesRenderer(valoresZ);
		}
		XYSeriesRenderer modulo = new XYSeriesRenderer();
		if (em == true) { // checkbox modulo
			modulo.setColor(Color.MAGENTA);
			renderer.addSeriesRenderer(modulo);
		} else {
			modulo.setColor(0);
			renderer.addSeriesRenderer(modulo);
		}
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setMarginsColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);
		// recogemos la calidad de pantalla para escribir el grosor de las
		// líneas
		if (calidad.equalsIgnoreCase("alta")) {
			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
			renderer.setMargins(margenesnormal);
			renderer.setLabelsTextSize(30);
			renderer.setLabelsTextSize(30);
			renderer.setAxisTitleTextSize(30);
			renderer.setChartTitleTextSize(30);
			renderer.setLegendTextSize(30);
			renderer.setShowLegend(false);
		} else if (calidad.equalsIgnoreCase("media")) {
			valoresX.setLineWidth(2);
			valoresY.setLineWidth(2);
			valoresZ.setLineWidth(2);
			modulo.setLineWidth(2);
			renderer.setMargins(margenespeque);
			renderer.setLabelsTextSize(20);
			renderer.setLabelsTextSize(20);
			renderer.setAxisTitleTextSize(20);
			renderer.setChartTitleTextSize(20);
			renderer.setShowLegend(false);
		} else if (calidad.equalsIgnoreCase("baja")) {
			valoresX.setLineWidth(2);
			valoresY.setLineWidth(2);
			valoresZ.setLineWidth(2);
			modulo.setLineWidth(2);
			renderer.setMargins(margenespeque);
			renderer.setLabelsTextSize(15);
			renderer.setLabelsTextSize(15);
			renderer.setAxisTitleTextSize(15);
			renderer.setChartTitleTextSize(15);
			renderer.setLegendTextSize(15);
			renderer.setShowLegend(false);
		} else if (calidad.equalsIgnoreCase("xhigh")) {
			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
			renderer.setMargins(margenes);
			renderer.setLabelsTextSize(25);
			renderer.setLabelsTextSize(25);
			renderer.setAxisTitleTextSize(25);
			renderer.setChartTitleTextSize(25);
			renderer.setLegendTextSize(25);
			renderer.setShowLegend(false);
		} else if (calidad.equalsIgnoreCase("xxhigh")) {
			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
			renderer.setMargins(margenesextra);
			renderer.setLabelsTextSize(40);
			renderer.setLabelsTextSize(40);
			renderer.setAxisTitleTextSize(40);
			renderer.setChartTitleTextSize(40);
			renderer.setLegendTextSize(40);
			renderer.setShowLegend(false);
		} else if (calidad.equalsIgnoreCase("xxxhigh")) {
			valoresX.setLineWidth(5);
			valoresY.setLineWidth(5);
			valoresZ.setLineWidth(5);
			modulo.setLineWidth(5);
			renderer.setMargins(margenesextra);
			renderer.setLabelsTextSize(40);
			renderer.setLabelsTextSize(40);
			renderer.setAxisTitleTextSize(40);
			renderer.setChartTitleTextSize(40);
			renderer.setLegendTextSize(40);
			renderer.setShowLegend(false);
		}
		
		renderer.setChartTitle(titulografica);
		renderer.setGridColor(Color.DKGRAY);
		renderer.setShowGrid(true);
		renderer.setYTitle(tituloejey);
		renderer.setXTitle("Tiempo (s)");
		renderer.setXLabels(5);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setPanLimits(limites); // añade los limites de la grafica
		renderer.setYLabelsAlign(Paint.Align.RIGHT);
		renderer.setAxesColor(Color.WHITE);
		renderer.setLabelsColor(Color.YELLOW);
	}

	public void setProperties2(boolean ex, boolean em, String titulografica,
			String tituloejey, String calidad, String tamano) {
		// añade las propiedades de la grafica sensor de luz y sensor proximidad
		double[] limites = { 0, maxejex + 5, ejeymin - 20, ejeymax + 20 };
		// utilizamos diferentes márgenes para las diferentes pantallas
		int[] margenes = { 55, 55, 55, 55 };
		int[] margenesnormal = { 40, 55, 50, 40 };
		int[] margenespeque = { 30, 35, 30, 25 };
		int[] margenesextra = { 60, 60, 60, 60 };
		XYSeriesRenderer valoresX = new XYSeriesRenderer();
		if (ex == true) { // checkbox x
			valoresX.setColor(Color.RED);
			renderer.addSeriesRenderer(valoresX);
		} else {
			valoresX.setColor(0);
			renderer.addSeriesRenderer(valoresX);
		}
		XYSeriesRenderer modulo = new XYSeriesRenderer();
		if (em == true) { // checkbox modulo
			modulo.setColor(Color.MAGENTA);
			renderer.addSeriesRenderer(modulo);
		} else {
			modulo.setColor(0);
			renderer.addSeriesRenderer(modulo);
		}
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setMarginsColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);
		// recogemos la calidad de pantalla para escribir el grosor de las
		// líneas
		if (calidad.equalsIgnoreCase("alta")) {
			valoresX.setLineWidth(5);
			modulo.setLineWidth(5);
			renderer.setMargins(margenesnormal);
			renderer.setLabelsTextSize(30);
			renderer.setLabelsTextSize(30);
			renderer.setAxisTitleTextSize(30);
			renderer.setChartTitleTextSize(30);
			renderer.setLegendTextSize(30);
			renderer.setShowLegend(false);
			renderer.isExternalZoomEnabled();
		} else if (calidad.equalsIgnoreCase("media")) {
			valoresX.setLineWidth(3);
			modulo.setLineWidth(3);
			renderer.setMargins(margenespeque);
			renderer.setLabelsTextSize(20);
			renderer.setLabelsTextSize(20);
			renderer.setAxisTitleTextSize(20);
			renderer.setChartTitleTextSize(20);
			renderer.setLegendTextSize(20);
			renderer.setShowLegend(false);
		} else if (calidad.equalsIgnoreCase("baja")) {
			valoresX.setLineWidth(3);
			modulo.setLineWidth(3);
			renderer.setMargins(margenespeque);
			renderer.setLabelsTextSize(15);
			renderer.setLabelsTextSize(15);
			renderer.setAxisTitleTextSize(15);
			renderer.setChartTitleTextSize(15);
			renderer.setLegendTextSize(15);
			renderer.setShowLegend(false);
		} else if (calidad.equalsIgnoreCase("xhigh")) {
			valoresX.setLineWidth(5);
			modulo.setLineWidth(5);
			renderer.setMargins(margenes);
			renderer.setLabelsTextSize(25);
			renderer.setLabelsTextSize(25);
			renderer.setAxisTitleTextSize(25);
			renderer.setChartTitleTextSize(25);
			renderer.setLegendTextSize(25);
			renderer.setShowLegend(false);
		} else if (calidad.equalsIgnoreCase("xxhigh")) {
			valoresX.setLineWidth(5);
			modulo.setLineWidth(5);
			renderer.setMargins(margenesextra);
			renderer.setLabelsTextSize(40);
			renderer.setLabelsTextSize(40);
			renderer.setAxisTitleTextSize(40);
			renderer.setChartTitleTextSize(40);
			renderer.setLegendTextSize(40);
			renderer.setShowLegend(false);
		} else if (calidad.equalsIgnoreCase("xxxhigh")) {
			valoresX.setLineWidth(5);
			modulo.setLineWidth(5);
			renderer.setMargins(margenesextra);
			renderer.setLabelsTextSize(40);
			renderer.setLabelsTextSize(40);
			renderer.setAxisTitleTextSize(40);
			renderer.setChartTitleTextSize(40);
			renderer.setLegendTextSize(40);
			renderer.setShowLegend(false);
		}
		renderer.setChartTitle(titulografica);
		renderer.setGridColor(Color.DKGRAY);
		renderer.setShowGrid(true);
		renderer.setYTitle(tituloejey);
		renderer.setXTitle("Tiempo (s)");
		renderer.setXLabels(5);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setPanLimits(limites); // añade los limites de la grafica
		renderer.setYLabelsAlign(Paint.Align.RIGHT);
		renderer.setAxesColor(Color.WHITE);
		renderer.setLabelsColor(Color.YELLOW);
	}

	// función donde recogemos los datos cargados de los ficheros para los
	// sensores acelerómtro, giroscopio y magnetómetro
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

	// función donde recogemos los datos cargados de los ficheros para los
	// sensores de proximidad y sensor de luz
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

	public GraphicalView getGraph() {

		return ChartFactory.getLineChartView(context, dataset, renderer);

	}
}