package com.example.app;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Grafica extends Activity implements SensorEventListener,
		OnClickListener {
	private static final String TAG = "Acelerometro aplicacion";
	
	// declarar grafica

	private GraphicalView chartView;
	private XYMultipleSeriesDataset sensorData;
	private XYMultipleSeriesRenderer mRenderer;
	XYSeries xSeries = new XYSeries("X");
	XYSeries ySeries = new XYSeries("Y");
	XYSeries zSeries = new XYSeries("Z");
	XYSeries series[];
	public static final int SAMPLERATE = 10;

	
	long timestamp;
	int xTick = 0;
	// Int tipo es la frecuencia que recogemos de la actividad anterior
	int tipo;
	// Hacemos una cola FIFO con listas enlazadas
	ConcurrentLinkedQueue<float[]> datosSensor = new ConcurrentLinkedQueue<float[]>();
	
	// Aquí guardamos los valores de x,y,z en un array que luego irán el la cola
	float[] xyz = new float[3];

	// Declaración del layout
	LinearLayout layout;
	// Maneja los sensores
	SensorManager sensorManager;
	// Declaramos los botones
	Button parar;
	Button guardar;
	Button enviar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grafica);
		// Declaramos objetos
		layout = (LinearLayout) findViewById(R.id.chart);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		parar = (Button) findViewById(R.id.parar);
		guardar = (Button) findViewById(R.id.guardar);
		enviar = (Button) findViewById(R.id.enviar);
		
		//Escuchamos los botones
		parar.setOnClickListener(this);
		guardar.setOnClickListener(this);
		enviar.setOnClickListener(this);

		//grafica
		/*mRenderer.setGridColor(Color.DKGRAY);
		mRenderer.setShowGrid(true);
		mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
		//mRenderer.setXAxisMin(0.0);
		mRenderer.setXTitle("xyz");
		mRenderer.setXAxisMax(10); // 10 seconds wide
		mRenderer.setXLabels(10); // 1 second per DIV
		mRenderer.setChartTitle(" ");
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
		chartView = ChartFactory.getLineChartView(this, sensorData, mRenderer);*/
		sensorData = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.setGridColor(Color.DKGRAY);
		mRenderer.setShowGrid(true);
		mRenderer.setXAxisMin(0.0);
		mRenderer.setXTitle(getString(R.string.samplerate, 1000 / SAMPLERATE));
		mRenderer.setXAxisMax(10000 / (1000 / SAMPLERATE)); // 10 seconds wide
		mRenderer.setXLabels(10); // 1 second per DIV
		mRenderer.setChartTitle(" ");
		mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
		chartView = ChartFactory.getLineChartView(this, sensorData, mRenderer);
		float textSize = new TextView(this).getTextSize();
		float upscale = textSize / mRenderer.getLegendTextSize();
		mRenderer.setLabelsTextSize(textSize);
		mRenderer.setLegendTextSize(textSize);
		mRenderer.setChartTitleTextSize(textSize);
		mRenderer.setAxisTitleTextSize(textSize);
		mRenderer.setFitLegend(true);
		int[] margins = mRenderer.getMargins();
		margins[0] *= upscale;
		margins[1] *= upscale;
		margins[2] = (int) (2 * mRenderer.getLegendTextSize());
		mRenderer.setMargins(margins);
		layout.addView(chartView);
		// Recogemos el tipo de frecuencia que hemos pasado desde la actividad
		// de acelerómetro
		Bundle graficas = getIntent().getExtras();
		tipo = graficas.getInt("tipo");
		onStart();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		 super.onResume();
		 Iniciar_sensores();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Parar_sensores();
		super.onStop();
	}

	private void saveHistory() {
		// Paramos los sensores
		onStop();

	
		SaveThread thread = new SaveThread();
		thread.start();
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
		//timestamp = System.currentTimeMillis();
		switch (event.sensor.getType()) {

		case Sensor.TYPE_ACCELEROMETER:
			
			for (int i = 0; i < 3; i++) {
				float valor = event.values[i];
				xyz[i] = valor;
				Log.d("hola","xxx "+i + " "+xyz[i] +"");
			}

			synchronized (this) {
				datosSensor.add(xyz.clone());
				//configure(datosSensor);
				
				}
			
			break;
		}
	}
	protected void onTick(SensorEvent currentEvent) {

		if (xTick == 0) {
			// Dirty, but we only learn a few things after getting the first event.
			configure(currentEvent);
			layout.addView(chartView);
		}
		for (int i = 0; i < series.length; i++) {
			if (series[i] != null) {
				series[i].add(xTick, currentEvent.values[i]);
			}
		}
		xTick++;
		chartView.repaint();
	}
	private void configure(SensorEvent event){
		String[] channelNames = new String[event.values.length];
		series = new XYSeries[event.values.length];
			for (int i = 0; i < channelNames.length; i++) {
				channelNames[i] = getString(R.string.channel_default) + i;
			}
			
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER: {
				channelNames[0] = getString(R.string.channel_x_axis);
				channelNames[1] = getString(R.string.channel_y_axis);
				channelNames[2] = getString(R.string.channel_z_axis);
				mRenderer.setYTitle(getString(R.string.unit_acceleration));
				break;
				
			}
			}
			int[] colors = {
					Color.RED,
					Color.YELLOW,
					Color.BLUE,
					Color.GREEN,
					Color.MAGENTA,
					Color.CYAN 
					};
			
			for (int i = 0; i < series.length; i++) {
				series[i] = new XYSeries(channelNames[i]);
				sensorData.addSeries(series[i]);
				XYSeriesRenderer r = new XYSeriesRenderer();
				r.setColor(colors[i % colors.length]);
				mRenderer.addSeriesRenderer(r);
				}
		
		}
	


	private class SaveThread extends Thread {
		@Override
		public void run() {
			
			StringBuilder csvData = new StringBuilder();
			Iterator<float[]> iterator = datosSensor.iterator();
			while (iterator.hasNext()) {
				float[] values = iterator.next();
				for (int angle = 0; angle < 3; angle++) {
					csvData.append(String.valueOf(values[angle]));
					if (angle < 3) {
						csvData.append(",");
					}
				}
				csvData.append("\n");
			}

			
			Message msg = new Message();
			Bundle bundle = new Bundle();

			try {
				
				String appName = getResources().getString(R.string.app_name);
				String dirPath = Environment.getExternalStorageDirectory().toString() + "/" + appName;
				File dir = new File(dirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				
				String fileName = DateFormat
						.format("yyyy-MM-dd-kk:mm:ss", System.currentTimeMillis())
						.toString().concat(".csv");

				
				File file = new File(dirPath, fileName);
				if (file.createNewFile()) {
					FileOutputStream fileOutputStream = new FileOutputStream(
							file);
					
					fileOutputStream.write(csvData.toString().getBytes());
					fileOutputStream.close();
				}

			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			
				bundle.putBoolean("success", false);
			}

			
			msg.setData(bundle);

		}
	}

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		switch (boton.getId()) {
		case (R.id.parar):
			Log.d("Parada","boton parar");
			onStop();
			break;
		case (R.id.guardar):
			Log.d("algo","boton guardar");
			saveHistory();
			break;
		case (R.id.enviar):
			break;
		}
	}



}
