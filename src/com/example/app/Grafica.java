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
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Grafica extends Activity implements OnClickListener,
		SensorEventListener {

	// Declaración de la grafica
	private GraphicalView chartView;

	// Declaración de las series que usamos en la representación de la gráfica
	XYMultipleSeriesDataset sensorData;
	XYMultipleSeriesRenderer mRenderer;
	XYSeries series[];
	
	// Número de muestras por segundo
	public static final int SAMPLERATE = 10;

	/**
	 * For moving the viewport of the graph
	 */
	private int lastMinX = 0;
	long timestamp;
	int xTick = 0;
	
	// Int tipo es la frecuencia que recogemos de la actividad anterior
	int tipo;
	// tiempoParada es el tiempo que recogemos de la actividad anterior y que
	// será el tiempo durante el que vamos a medir los sensores
	int tiempoParada,tiempoInicio;

	// Hacemos una cola FIFO con listas enlazadas
	ConcurrentLinkedQueue<float[]> datosSensor = new ConcurrentLinkedQueue<float[]>();

	// Aquí guardamos los valores de x,y,z en un array que luego irán el la cola
	float[] xyz = new float[3];

	// Hilo dónde van guardados los datos de los sensores
	private Thread ticker;

	// Declaración del layout donde irá la gráfica
	LinearLayout layout;

	// Controlador de los sensores
	SensorManager sensorManager;

	// Declaramos los botones
	Button parar;
	Button iniciar;
	Button reiniciar;
	
	// Declaramos los checkbox
	CheckBox ejex;
	CheckBox ejey;
	CheckBox ejez;
	CheckBox modulo;

	// Declaramos los temporizadores tanto para empezar a tomar datos como para
	// detener la toma de medidas
	CountDownTimer temporizador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Mantenemos la pantalla encendida
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		requestWindowFeature(Window.FEATURE_PROGRESS);

		// Elegimos el layout a mostrar en esta clase
		setContentView(R.layout.grafica);

		// Declaramos objetos
		layout = (LinearLayout) findViewById(R.id.chart);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.unregisterListener((SensorEventListener) ticker);
		sensorManager.unregisterListener(this);
		parar = (Button) findViewById(R.id.parar);
		iniciar = (Button) findViewById(R.id.inicio);
		reiniciar = (Button) findViewById(R.id.reiniciar);

		// Escuchamos los botones
		parar.setOnClickListener(this);
		iniciar.setOnClickListener(this);
		reiniciar.setOnClickListener(this);
		
		// Escuchamos los checkbox
		
		sensorData = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();

		// Propiedades de la gráfica
		mRenderer.setApplyBackgroundColor(true); //fondo
		mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50)); //color fondo
		mRenderer.setGridColor(Color.DKGRAY); //color lineas
		mRenderer.setShowGrid(true); //lineas
		mRenderer.setXAxisMin(0.0); //valor minimo de la x
		mRenderer.setXTitle("Tiempo"); // titulo del eje x
		mRenderer.setXAxisMax(10000/(1000/ SAMPLERATE)); // maximo 10
		mRenderer.setXLabels(10); // 1 second per DIV
		mRenderer.setChartTitle(" "); //titulo de la grafica
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

		// Recogemos el tipo de frecuencia (normal, ui, game, fastest) que hemos
		// pasado desde la actividad de acelerómetro y los tiempos
		//Bundle graficas = getIntent().getExtras();
		/*tipo = graficas.getInt("tipo");
		// Cuando recogemos los tiempos los pasamos a milisegundos
		int tiempo = graficas.getInt("tiempo");
		tiempoParada = tiempo * 1000;
		Log.d("tiempo", "tiempoParada2 " + tiempoParada);*/

		// Temporizador con el que paramos los sensore al llegar al tiempoParada
		/*new CountDownTimer(tiempoParada, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
			}

			// Cuando llega al tiempo especificado paramos los sensores
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Parar_sensores();

			}
		}.start();*/
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
		// Lo que hacemos aquí es bloquear la pantalla una vez iniciada la
		// gráfica. Es decir que si la pantalla está en vertical o en horizontal
		// al inciar la actividad se mantendrá en esa posición todo el proceso
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

		
		Iniciar_sensores();
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Parar_sensores();
		super.onStop();
	}

	// Controlador de mensajería. Recogemos el mensaje enviado desde el la
	// función de guardado
	private Handler mensajeria = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle data = msg.getData();
			Toast.makeText(Grafica.this, data.getString("msg"),
					Toast.LENGTH_SHORT).show();
			super.handleMessage(msg);
		}

	};

	// Función para guardar los datos obtenidos de los sensores
	

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
		sensorManager
				.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
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

			Bundle bundle = new Bundle();
			Message msg = new Message();

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
				// Si se ha guardado con éxito enviamos un mensaje al
				// controlador de mensajes y lo muestra
				bundle.putString(
						"msg",
						Grafica.this.getResources().getString(
								R.string.save_complate));
			} catch (Exception e) {
				// Si no se ha podido guardar entonces nos envía un mensaje
				// diciendo que no se ha guardado
				bundle.putString(
						"msg",
						Grafica.this.getResources().getString(
								R.string.save_imcomplate));
			}
			msg.setData(bundle);
			// Envía el mensaje al controlador
			mensajeria.sendMessage(msg);
		}

	}

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		switch (boton.getId()) {
		case (R.id.parar):
			Log.d("Parada", "boton parar");
			iniciar.setEnabled(true);
			parar.setEnabled(true);
			onStop();
			break;
		case (R.id.inicio):
			parar.setEnabled(true);
			onStart();
			break;
		case (R.id.reiniciar):
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
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++) {
				float valor = event.values[i];
				xyz[i] = valor;
				 Log.d("sensorchanged", "xxx "+i+" "+xyz[i]);
			}
			break;
		}
		synchronized (this) {
			datosSensor.add(xyz.clone());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Cargamos las opciones que vamos a usar en esta pantalla

		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menugrafica, menu);
		return true;
		/** true -> el menú ya está visible */
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Elegimos entre las opciones disponibles en esta pantalla
		switch (item.getItemId()) {
		case (R.id.guardar):
			Log.d("algo", "boton guardar");
			//saveHistory();
			break;
		case (R.id.enviar):
			new Exportar(this).execute(datosSensor);
			break;
		}
		return true;
		/** true -> consumimos el item, no se propaga */
	}
}
