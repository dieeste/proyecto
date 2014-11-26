package com.example.app;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class Grafica extends Activity implements SensorEventListener {

	// Conjunto de datos principal que incluye todas las series de la gráfica
	XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	// Render que incluye el renderer personalizado de la gráfica
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	// Renderer creado más reciente, personalizando las series actuales
	XYSeriesRenderer mRendererActual;
	// Gráfica que mostrará los datos
	GraphicalView mVistaGrafica;
	// Datos
	long timestamp;
	double x;
	double y;
	double z;
	int tipo;
	// Array
	ArrayList<Double> datosSensor;
	// Declaración del layout
	LinearLayout layout;
	// Maneja los sensores
	SensorManager sensorManager;
	Button parar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grafica);
		// Declaramos objetos
		layout = (LinearLayout) findViewById(R.id.chart);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		datosSensor = new ArrayList<Double>();
		parar = (Button) findViewById(R.id.parar);

		// Declaramos la escucha del botón parar
		parar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Parar_sensores();
			}
		});

		// Recogemos el tipo de frecuencia que hemos pasado desde la actividad
		// de acelerómetro
		Bundle graficas = getIntent().getExtras();
		tipo = graficas.getInt("tipo");

		// Propiedades de la gráfica
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
		mRenderer.setAxisTitleTextSize(16);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setLabelsTextSize(15);
		mRenderer.setLegendTextSize(15);
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setPointSize(5);

		Iniciar_sensores();
	}

	protected void Iniciar_sensores() {
		sensorManager
				.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), tipo);
	}

	protected void Parar_sensores() {
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		synchronized (this) {
			switch (event.sensor.getType()) {

			case Sensor.TYPE_ACCELEROMETER:
				// Series añadidas recientemente
				XYSeries xSeries = new XYSeries("X");
				;
				double x = event.values[0];
				double y = event.values[1];
				double z = event.values[2];
				long timestamp = System.currentTimeMillis();

				xSeries.add(timestamp, x);
				mDataset.addSeries(xSeries);
				/*
				 * mDataset.addSeries(ySeries); mDataset.addSeries(zSeries);
				 */
				XYSeriesRenderer xRenderer = new XYSeriesRenderer();
				xRenderer.setColor(Color.RED);
				xRenderer.setPointStyle(PointStyle.CIRCLE);
				xRenderer.setFillPoints(true);
				xRenderer.setLineWidth(1);
				xRenderer.setDisplayChartValues(false);

			

				mRenderer.addSeriesRenderer(xRenderer);

				mVistaGrafica = ChartFactory.getLineChartView(this,
						mDataset, mRenderer);
				
				mVistaGrafica.repaint();

				// Adding the Line Chart to the LinearLayout
				layout.addView(mVistaGrafica);
			}
		}
	}

}
