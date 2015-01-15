package com.example.app;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Grafica extends Activity implements OnClickListener,
		SensorEventListener {

	// Declaración de la grafica

	// Declaración de las series que usamos en la representación de la gráfica
	public boolean init = false;

	private ConcurrentLinkedQueue<AccelData> sensorDatas;

	// Declaración del layout donde irá la gráfica
	LinearLayout layout;

	// Controlador de los sensores
	SensorManager sensorManager;

	// Declaramos los botones
	Button parar;
	Button iniciar;
	Button reiniciar;
	private static final int DATA_R = 3;
	private boolean[] mGraphs = { true, true, true, true };

	// Declaramos los checkbox
	CheckBox ejex;
	CheckBox ejey;
	CheckBox ejez;
	CheckBox modulo;

	// Declaramos los temporizadores tanto para empezar a tomar datos como para
	// detener la toma de medidas
	CountDownTimer temporizador, tiempo;
	int frecuencia;
	int tiempoInicio;
	int tiempoParada;
	int vector;
	long previoustime;
	long firstTime;

	GraphicalView view;
	Graph mGraph;
	ArrayList<Double> x, y, z;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Mantenemos la pantalla encendida
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		requestWindowFeature(Window.FEATURE_PROGRESS);

		// Elegimos el layout a mostrar en esta clase
		setContentView(R.layout.grafica);

		sensorDatas = new ConcurrentLinkedQueue<AccelData>();

		// Declaramos objetos
		layout = (LinearLayout) findViewById(R.id.chart);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		parar = (Button) findViewById(R.id.parar);
		iniciar = (Button) findViewById(R.id.inicio);
		reiniciar = (Button) findViewById(R.id.reiniciar);

		// Escuchamos los botones
		parar.setOnClickListener(this);
		iniciar.setOnClickListener(this);
		reiniciar.setOnClickListener(this);

		// Escuchamos los checkbox
		/*
		 * ejex = (CheckBox) findViewById(R.id.ejex); ejey = (CheckBox)
		 * findViewById(R.id.ejey); ejez = (CheckBox) findViewById(R.id.ejez);
		 * modulo = (CheckBox) findViewById(R.id.modulo);
		 */

		// Recogemos el tipo de frecuencia (normal, ui, game, fastest) que hemos
		// pasado desde la actividad de acelerómetro y los tiempos

		Bundle graficas = getIntent().getExtras();

		// Frecuencia es los distintos tipos de frecuencia que recogemos de la
		// actividad anteerior que a su vez es recogido de la configuración
		frecuencia = graficas.getInt("tipo");
		Log.d("tiem", "tifercuencia " + frecuencia);
		// tiempoParada y tiempoInicio es el tiempo que recogemos de la
		// actividad anterior y que será el tiempo durante el que vamos a medir
		// los sensores y el tiempo que pasará antes de inciar los sensores
		tiempoInicio = graficas.getInt("temporizador");
		Log.d("tiempo", "tiempoInicio " + tiempoInicio);
		tiempoParada = graficas.getInt("tiempo");
		Log.d("tiempo", "tiempoParada " + tiempoParada);

		// si el tiempo de inicio es mayor que cero vamos a contadores si no lo
		// dejamos como está
		if (tiempoInicio > 0) {
			contadores();
		}
		CheckBox[] checkboxes = new CheckBox[4];
		checkboxes[SensorManager.DATA_X] = (CheckBox) findViewById(R.id.ejex);
		checkboxes[SensorManager.DATA_Y] = (CheckBox) findViewById(R.id.ejey);
		checkboxes[SensorManager.DATA_Z] = (CheckBox) findViewById(R.id.ejez);
		checkboxes[DATA_R] = (CheckBox) findViewById(R.id.modulo);
		for (int i = 0; i < 4; i++) {
			if (mGraphs[i]) {
				checkboxes[i].setChecked(true);
			}

			checkboxes[i]
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							switch (buttonView.getId()) {
							case R.id.ejex:
								mGraphs[SensorManager.DATA_X] = isChecked;
								break;
							case R.id.ejey:
								mGraphs[SensorManager.DATA_Y] = isChecked;
								break;
							case R.id.ejez:
								mGraphs[SensorManager.DATA_Z] = isChecked;
								break;
							case R.id.modulo:
								mGraphs[DATA_R] = isChecked;
								break;
							}
						}
					});
		}
	}

	private void contadores() {
		// Con este temporizador medimos el tiempo antes de iniciar los sensores
		new CountDownTimer(tiempoInicio, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

				Log.d("tiempo", "inciamos por tiempo");

				// Con este temporizador medimos el tiempo de medida de los
				// sensores
				// Empezamos el otro temporizador
				if (tiempoParada == 0) {
					Iniciar_sensores();
					iniciar.setEnabled(false);
					parar.setEnabled(true);
					reiniciar.setEnabled(false);

				} else {

					Iniciar_sensores();
					iniciar.setEnabled(false);
					parar.setEnabled(true);
					reiniciar.setEnabled(false);

					new CountDownTimer(tiempoParada, 1000) {
						@Override
						public void onTick(long millisUntilFinished) {
							// TODO Auto-generated method stub
						}

						// Cuando llega al tiempo especificado paramos los
						// sensores
						@Override
						public void onFinish() { // TODO Auto-generated method
													// stub
							Log.d("tiempo", "Paramos por tiempo");
							onStop();
							reiniciar.setEnabled(true);
							iniciar.setEnabled(true);
							parar.setEnabled(false);
						}
					}.start();
				}
			}
		}.start();
	}

	private void contadores2() {
		// Con este temporizador medimos el tiempo antes de iniciar los sensores
		new CountDownTimer(tiempoParada, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
			}

			// Cuando llega al tiempo especificado paramos los sensores
			@Override
			public void onFinish() { // TODO Auto-generated method stub
				Log.d("tiempo", "Paramos por tiempo");
				onStop();
				reiniciar.setEnabled(true);
				iniciar.setEnabled(false);
				parar.setEnabled(false);
			}
		}.start();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		double x = event.values[0];
		double y = event.values[1];
		double z = event.values[2];
		long timestamp = System.currentTimeMillis();

		AccelData data = new AccelData(timestamp, x, y, z);
		sensorDatas.add(data);
		Log.d("sensordatassss", "sensor: " + data);
		mGraph = new Graph(this);
		mGraph.initData(sensorDatas);
		mGraph.setProperties(mGraphs);
		if (!init) {
			view = mGraph.getGraph();
			layout.addView(view);
			init = true;
		} else {
			layout.removeView(view);
			view = mGraph.getGraph();
			layout.addView(view);
		}

		// long deltaTime = currentTime - previoustime;

		/*
		 * switch (event.sensor.getType()) { case Sensor.TYPE_ACCELEROMETER:
		 * xSeries.add(currentTime, event.values[0]); Log.d("sensorchanged",
		 * "xxx " + " " + xSeries);
		 * 
		 * synchronized (this) { } sensorData.addSeries(xSeries);
		 * XYSeriesRenderer xRenderer = new XYSeriesRenderer();
		 * xRenderer.setColor(Color.CYAN);
		 * xRenderer.setPointStyle(PointStyle.CIRCLE);
		 * xRenderer.setDisplayChartValues(true); xRenderer.setLineWidth(2);
		 * xRenderer.setFillPoints(true);
		 * mRenderer.addSeriesRenderer(xRenderer); break; }
		 */

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Parar_sensores();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
	}

	protected void Iniciar_sensores() {
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				frecuencia);
	}

	private class SaveThread extends Thread {

		@Override
		public void run() {
			StringBuilder csvData = new StringBuilder();
			csvData.append("tiempo");
			csvData.append(",");
			csvData.append("X");
			csvData.append(",");
			csvData.append("Y");
			csvData.append(",");
			csvData.append("Z");
			csvData.append("\n");
			for (AccelData values : sensorDatas) {
				csvData.append(String.valueOf(values.getTimestamp()));
				csvData.append(",");
				csvData.append(String.valueOf(values.getTimestamp()));
				csvData.append(",");
				csvData.append(String.valueOf(values.getX()));
				csvData.append(",");
				csvData.append(String.valueOf(values.getY()));
				csvData.append(",");
				csvData.append(String.valueOf(values.getZ()));
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

	// Función para guardar los datos obtenidos de los sensores
	private void saveHistory() {
		String stadoSD = Environment.getExternalStorageState();
		// Comprobamos si podemos acceder a la memoria sd, si no se puede lanza
		// este mensaje y solo se podrían compartir los datos
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

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		switch (boton.getId()) {
		case (R.id.parar):
			iniciar.setEnabled(true);
			parar.setEnabled(false);
			onStop();
			// openChart();
			break;
		case (R.id.inicio):
			parar.setEnabled(true);
			iniciar.setEnabled(false);
			// sensorDatas = new ArrayList<AccelData>();
			Iniciar_sensores();
			reiniciar.setEnabled(true);
			if (tiempoParada > 0)
				contadores2();
			break;
		case (R.id.reiniciar):
			break;
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
			saveHistory();
			break;
		case (R.id.enviar):
			// new Exportar(this).execute(sensorDatas);
			break;
		/*
		 * case (R.id.configurate): Log.d("algo", "boton conf"); Intent i = new
		 * Intent(this, PrefGrafica.class); // Iniciamos la actividad y
		 * esperamos respuesta con los datos startActivityForResult(i, 0);
		 * break;
		 */
		}
		return true;
		/** true -> consumimos el item, no se propaga */
	}

	/*
	 * @SuppressLint("NewApi") @Override protected void onActivityResult(int
	 * requestCode, int resultCode, Intent data) { // TODO Auto-generated method
	 * stub super.onActivityResult(requestCode, resultCode, data);
	 * SharedPreferences pref = PreferenceManager
	 * .getDefaultSharedPreferences(this); boolean v =
	 * pref.getBoolean("sensores", false); Log.d("vec", "este es: " + v);
	 * 
	 * }
	 */
}
