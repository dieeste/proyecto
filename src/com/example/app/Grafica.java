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
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Grafica extends Activity implements OnClickListener, SensorEventListener {
	private static final String TAG = "Acelerometro aplicacion";

	// declarar grafica

	private GraphicalView chartView;
	XYMultipleSeriesDataset sensorData;
	XYMultipleSeriesRenderer mRenderer;
	XYSeries series[];
	public static final int SAMPLERATE = 10;

	/**
	 * For moving the viewport of the graph
	 */
	private int lastMinX = 0;

	long timestamp;
	int xTick = 0;
	// Int tipo es la frecuencia que recogemos de la actividad anterior
	int tipo;
	int tiempoParada;
	// Hacemos una cola FIFO con listas enlazadas
	ConcurrentLinkedQueue<float[]> datosSensor = new ConcurrentLinkedQueue<float[]>();

	// Aquí guardamos los valores de x,y,z en un array que luego irán el la cola
	float[] xyz = new float[3];

	private Thread ticker;
	// Declaración del layout
	LinearLayout layout;
	// Maneja los sensores
	SensorManager sensorManager;
	// Declaramos los botones
	Button parar;
	Button guardar;
	Button enviar;
	CountDownTimer temporizador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.grafica);
		// Declaramos objetos
		layout = (LinearLayout) findViewById(R.id.chart);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		parar = (Button) findViewById(R.id.parar);
		guardar = (Button) findViewById(R.id.guardar);
		enviar = (Button) findViewById(R.id.enviar);

		// Escuchamos los botones
		parar.setOnClickListener(this);
		guardar.setOnClickListener(this);
		enviar.setOnClickListener(this);
		sensorData = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();

		// grafica
		// chartView = ChartFactory.getLineChartView(this, sensorData,
		// mRenderer);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
		mRenderer.setGridColor(Color.DKGRAY);
		mRenderer.setShowGrid(true);
		mRenderer.setXAxisMin(0.0);
		mRenderer.setXTitle("Tiempo");
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

		// chartView.repaint();
		// layout.addView(chartView);
		// Recogemos el tipo de frecuencia que hemos pasado desde la actividad
		// de acelerómetro y el tiempo
		Bundle graficas = getIntent().getExtras();
		tipo = graficas.getInt("tipo");
		int tiempo= graficas.getInt("tiempo");
		tiempoParada = tiempo*1000;
		Log.d("tiempo", "tiempoParada2 "+tiempoParada);
		
		new CountDownTimer(tiempoParada,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Parar_sensores();
				
			}
		}.start();
		Iniciar_sensores();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		switch (getResources().getConfiguration().orientation) {
		case Configuration.ORIENTATION_PORTRAIT: {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			break;
		}
		case Configuration.ORIENTATION_LANDSCAPE: {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
		}
		}

		if (xTick == 0) {
			ticker = new Ticker(this);
			ticker.start();
			sensorManager.registerListener((SensorEventListener) ticker,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					tipo);
		}
	}

	protected void onTick(SensorEvent currentEvent) {

		if (xTick == 0) {
			// Dirty, but we only learn a few things after getting the first
			// event.
			configure(currentEvent);
			layout.addView(chartView);
		}

		if (xTick > mRenderer.getXAxisMax()) {
			mRenderer.setXAxisMax(xTick);
			mRenderer.setXAxisMin(++lastMinX);
		}

		fitYAxis(currentEvent);

		for (int i = 0; i < series.length; i++) {
			if (series[i] != null) {
				series[i].add(xTick, currentEvent.values[i]);
			}
		}
		xTick++;

		
		chartView.repaint();
	}

	private void fitYAxis(SensorEvent event) {
		double min = mRenderer.getYAxisMin(), max = mRenderer.getYAxisMax();
		for (int i = 0; i < series.length; i++) {
			if (event.values[i] < min) {
				min = event.values[i];
			}
			if (event.values[i] > max) {
				max = event.values[i];
			}
		}
		float sum = 0;
		for (int i = 0; i < event.values.length; i++) {
			sum += event.values[i];
		}
		double half = 0;
		if (xTick == 0 && sum == event.values[0] * event.values.length) {
			// If the plot flatlines on the first event, we can't grade the Y
			// axis.
			// This is especially bad if the sensor does not change without a
			// stimulus. the graph will then flatline on the x-axis where it is
			// impossible to be seen.
			half = event.values[0] * 0.5 + 1;
		}
		mRenderer.setYAxisMax(max + half);
		mRenderer.setYAxisMin(min - half);
	}

	private void configure(SensorEvent event) {
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

		int[] colors = { Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN,
				Color.MAGENTA, Color.CYAN };
		for (int i = 0; i < series.length; i++) {
			series[i] = new XYSeries(channelNames[i]);
			sensorData.addSeries(series[i]);
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i % colors.length]);
			mRenderer.addSeriesRenderer(r);

		}
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
		String stadoSD = Environment.getExternalStorageState();
		if (!stadoSD.equals(Environment.MEDIA_MOUNTED)
				&& !stadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			Toast.makeText(
					this,
					"No puedo leer en la memoria externa, solo se puede exportar",
					Toast.LENGTH_LONG).show();
			return;
		}
		SaveThread thread = new SaveThread();
		thread.start();
	}

	protected void Parar_sensores() {
		try {
			sensorManager.unregisterListener((SensorEventListener) ticker);
			sensorManager.unregisterListener(this,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
			ticker.interrupt();
			ticker.join();
			ticker = null;
			Toast.makeText(this, "Parado", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
		}
	}
	protected void Iniciar_sensores() {
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				tipo);
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
				String dirPath = Environment.getExternalStorageDirectory()
						.toString() + "/" + appName;
				File dir = new File(dirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				String fileName = DateFormat
						.format("yyyy-MM-dd-kk mm ss",
								System.currentTimeMillis()).toString()
						.concat(".csv");

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
			Log.d("Parada", "boton parar");
			onStop();
			break;
		case (R.id.guardar):
			Log.d("algo", "boton guardar");
			saveHistory();
			break;
		case (R.id.enviar):
			new Exportar(this).execute(datosSensor);
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		switch (event.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++) {
				float valor = event.values[i];
				xyz[i] = valor;
				Log.d("hola", "xxx " + i + " " + xyz[i] + "");
			}
			break;
		}
		synchronized (this) {
			datosSensor.add(xyz.clone());
			//timestamp = System.currentTimeMillis();
			// configure(datosSensor);
		}
	}

}
