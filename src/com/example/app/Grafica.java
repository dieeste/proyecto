package com.example.app;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.GraphicalView;

import com.example.app.R.id;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Grafica extends Activity implements OnClickListener,
		SensorEventListener, OnCheckedChangeListener {

	// init y funciona se usan para representar si está parada o corriendo la
	// gráfica
	public boolean init = false;
	public boolean funciona = false;

	// Declaración de las colas que usamos para recoger los datos de todos los
	// sensores
	private ConcurrentLinkedQueue<AccelData> sensorDatas;
	private ConcurrentLinkedQueue<AccelData> sensorGiroscopio;
	private ConcurrentLinkedQueue<AccelData> sensorMagnetico;
	private ConcurrentLinkedQueue<AccelData2> sensorLuz;
	private ConcurrentLinkedQueue<AccelData2> sensorProximidad;
	private ConcurrentLinkedQueue<GpsDatos> gpsdatos;

	// Declaración del layout donde irá la gráfica
	LinearLayout layout;

	// Controlador de los sensores
	SensorManager sensorManager;

	// Declaramos los botones
	Button parar;
	Button iniciar;
	Button reiniciar;
	Button continuar;
	ImageButton lupa;

	// los cuatro checkbox que usamos para ver o quitar los datos de la x, y, z
	// y módulo
	boolean parado;
	// Declaramos los checkbox
	CheckBox ejex;
	CheckBox ejey;
	CheckBox ejez;
	CheckBox moduloc;
	boolean checkx = true;
	boolean checky = true;
	boolean checkz = true;
	boolean checkmodulo = true;
	public boolean vercargar = false;
	// Declaramos los temporizadores tanto para empezar a tomar datos como para
	// detener la toma de medidas, la frecuencia de recogida
	CountDownTimer temporizador, tiempo;
	int frecuencia;
	int tiempoInicio;
	int tiempoParada;
	// sensor que estamos representando que nos viene de la actividad anterior
	int sensor;
	// detectamos que sensores están marcados para guardas sus datos
	boolean acce;
	boolean giro;
	boolean magne;
	boolean luz;
	boolean proxi;
	boolean g;
	// vista donde colocamos la gráfica
	GraphicalView view;
	// clase donde representamos los datos
	Graph mGraph;
	// calidad de pantalla, tamaño de pantalla
	String calidad;
	String tamano;
	// nos indica si un sensor está grabando y si el gps está encendido
	TextView graba;
	TextView graba2;
	TextView gps;
	// latitud y longitud del gps
	double longitud;
	double latitud;
	// manejan la localización del gps
	LocationManager milocManager;
	LocationListener milocListener;
	String nombresensor = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Mantenemos la pantalla encendida
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		requestWindowFeature(Window.FEATURE_PROGRESS);
		// Elegimos el layout a mostrar en esta clase
		setContentView(R.layout.grafica);

		// escucha de los vectores declarados arriba
		sensorDatas = new ConcurrentLinkedQueue<AccelData>();
		sensorGiroscopio = new ConcurrentLinkedQueue<AccelData>();
		sensorMagnetico = new ConcurrentLinkedQueue<AccelData>();
		sensorLuz = new ConcurrentLinkedQueue<AccelData2>();
		sensorProximidad = new ConcurrentLinkedQueue<AccelData2>();
		gpsdatos = new ConcurrentLinkedQueue<GpsDatos>();

		// Declaramos objetos
		layout = (LinearLayout) findViewById(R.id.chart);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		parar = (Button) findViewById(R.id.parar);
		iniciar = (Button) findViewById(R.id.inicio);
		reiniciar = (Button) findViewById(R.id.reiniciar);
		continuar = (Button) findViewById(R.id.continuar);
		lupa = (ImageButton) findViewById(R.id.lupa);

		// Escuchamos los botones
		parar.setOnClickListener(this);
		iniciar.setOnClickListener(this);
		reiniciar.setOnClickListener(this);
		continuar.setOnClickListener(this);
		lupa.setOnClickListener(this);

		graba = (TextView) findViewById(R.id.grabando);
		graba2 = (TextView) findViewById(R.id.grabando2);
		gps = (TextView) findViewById(R.id.gpsestado);
		// Recogemos datos de la actividad anterior
		Bundle graficas = getIntent().getExtras();

		// Frecuencia es los distintos tipos de frecuencia que recogemos de la
		// actividad anteerior que a su vez es recogido de la configuración
		frecuencia = graficas.getInt("tipo");
		// tiempoParada y tiempoInicio es el tiempo que recogemos de la
		// actividad anterior y que será el tiempo durante el que vamos a medir
		// los sensores y el tiempo que pasará antes de inciar los sensores
		tiempoInicio = graficas.getInt("temporizador");
		tiempoParada = graficas.getInt("tiempo");

		// también recogemos el tipo de sensor y si están seleccionados más de
		// un sensor para recoger los datos
		sensor = graficas.getInt("sensor");
		acce = graficas.getBoolean("acelerometro");
		giro = graficas.getBoolean("giroscopio");
		magne = graficas.getBoolean("magnetometro");
		luz = graficas.getBoolean("luz");
		proxi = graficas.getBoolean("proximo");
		g = graficas.getBoolean("gps");

		// si el tiempo de inicio es mayor que cero vamos a contadores si no lo
		// dejamos como está
		if (tiempoInicio > 0) {
			contadores();
		}

		// escucha de los checkbox
		ejex = (CheckBox) findViewById(R.id.ejex);
		ejey = (CheckBox) findViewById(R.id.ejey);
		ejez = (CheckBox) findViewById(R.id.ejez);
		moduloc = (CheckBox) findViewById(R.id.modulo);

		ejex.setOnCheckedChangeListener(this);
		ejey.setOnCheckedChangeListener(this);
		ejez.setOnCheckedChangeListener(this);
		moduloc.setOnCheckedChangeListener(this);

		// declaramos el gps y sus escuchas
		milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		milocListener = new MiLocationListener();
		milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				milocListener);
		if (latitud == 0 && longitud == 0) {
			gps.setText(getResources().getString(R.string.gpsbuscando));
		}
		// medimos la calidad de la pantalla y su tamaño para poder estableces
		// el tamaño y los márgenes de la gráfica
		float scale = getApplicationContext().getResources()
				.getDisplayMetrics().density;
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			tamano = "grande";
		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			tamano = "normal";
		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			tamano = "pequena";
		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			tamano = "extra";
		}
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int dips = 40;

		// TRATAR DE ENCONTRAR EL MISMO RESULTADO
		float pixelBoton = 0;
		float scaleDensity = 0;

		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_XXXHIGH:
			scaleDensity = scale * 640;
			pixelBoton = dips * (scaleDensity / 640);
			calidad = "xxxhigh";
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			scaleDensity = scale * 480;
			pixelBoton = dips * (scaleDensity / 480);
			calidad = "xxhigh";
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			scaleDensity = scale * 320;
			pixelBoton = dips * (scaleDensity / 320);
			calidad = "xhigh";
			break;
		case DisplayMetrics.DENSITY_HIGH: // HDPI
			scaleDensity = scale * 240;
			pixelBoton = dips * (scaleDensity / 240);
			calidad = "alta";
			break;
		case DisplayMetrics.DENSITY_MEDIUM: // MDPI
			scaleDensity = scale * 160;
			pixelBoton = dips * (scaleDensity / 160);
			calidad = "media";
			break;

		case DisplayMetrics.DENSITY_LOW: // LDPI
			scaleDensity = scale * 120;
			pixelBoton = dips * (scaleDensity / 120);
			calidad = "baja";
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		milocManager.removeUpdates(milocListener);
	}

	public class MiLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			gps.setText(getResources().getString(R.string.gpssignal));

			latitud = loc.getLatitude();
			longitud = loc.getLongitude();
			/*
			 * double timestampgps = System.currentTimeMillis(); GpsDatos datos
			 * = new GpsDatos(timestampgps, latitud, longitud);
			 * gpsdatos.add(datos);
			 */
		}

		public void onProviderDisabled(String provider) {
			gps.setText(getResources().getString(R.string.gpsoff));
		}

		public void onProviderEnabled(String provider) {
			gps.setText(getResources().getString(R.string.gpsbuscando));
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	private void contadores() {
		iniciar.setEnabled(false);
		// Con este temporizador medimos el tiempo antes de iniciar los sensores
		new CountDownTimer(tiempoInicio, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

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
					parar.setEnabled(false);
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
							onStop();
							reiniciar.setEnabled(true);
							iniciar.setEnabled(false);
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
		synchronized (this) {
			double timestampgps = System.currentTimeMillis();
			GpsDatos datos = new GpsDatos(timestampgps, latitud, longitud);
			gpsdatos.add(datos);
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				double x = event.values[0];
				double y = event.values[1];
				double z = event.values[2];
				double modulo = Double.valueOf(Math.abs(Math.sqrt(Math
						.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2))));
				double timestamp = System.currentTimeMillis();
				AccelData data = new AccelData(timestamp, x, y, z, modulo);
				sensorDatas.add(data);
				if (sensor == Sensor.TYPE_ACCELEROMETER) {
					ejex.setVisibility(CheckBox.VISIBLE);
					ejey.setVisibility(CheckBox.VISIBLE);
					ejez.setVisibility(CheckBox.VISIBLE);
					moduloc.setVisibility(CheckBox.VISIBLE);
					nombresensor = getString(R.string.acelerometro);
					setTitle(nombresensor);
					if (acce == true) {
						graba.setText("REC");
						graba.setVisibility(TextView.VISIBLE);
						graba2.setVisibility(TextView.INVISIBLE);
					} else {
						graba2.setText("REC");
						graba2.setVisibility(TextView.VISIBLE);
						graba.setVisibility(TextView.INVISIBLE);
					}
					mGraph = new Graph(this);
					mGraph.ejeY(sensorDatas);
					mGraph.ejeX(sensorDatas);
					mGraph.initData(sensorDatas);
					mGraph.setProperties(checkx, checky, checkz, checkmodulo,
							"", "a (" + getString(R.string.unidad_acelerometro)
									+ ")", calidad, tamano);
					if (!init) {
						view = mGraph.getGraph();
						layout.addView(view);
						init = true;
					} else {
						layout.removeView(view);
						view = mGraph.getGraph();
						layout.addView(view);
					}
				}
				break;
			case Sensor.TYPE_GYROSCOPE:
				double x2 = event.values[0];
				double y2 = event.values[1];
				double z2 = event.values[2];
				double modulo2 = Double.valueOf(Math.abs(Math.sqrt(Math.pow(x2,
						2) + Math.pow(y2, 2) + Math.pow(z2, 2))));
				double timestamp2 = System.currentTimeMillis();
				AccelData data2 = new AccelData(timestamp2, x2, y2, z2, modulo2);
				sensorGiroscopio.add(data2);
				if (sensor == Sensor.TYPE_GYROSCOPE) {
					ejex.setVisibility(CheckBox.VISIBLE);
					ejey.setVisibility(CheckBox.VISIBLE);
					ejez.setVisibility(CheckBox.VISIBLE);
					moduloc.setVisibility(CheckBox.VISIBLE);
					nombresensor = getString(R.string.giroscopio);
					setTitle(nombresensor);
					if (giro == true) {
						graba.setText("REC");
						graba.setVisibility(TextView.VISIBLE);
						graba2.setVisibility(TextView.INVISIBLE);
					} else {
						graba2.setText("REC");
						graba2.setVisibility(TextView.VISIBLE);
						graba.setVisibility(TextView.INVISIBLE);
					}
					mGraph = new Graph(this);
					mGraph.ejeY(sensorGiroscopio);
					mGraph.ejeX(sensorGiroscopio);
					mGraph.initData(sensorGiroscopio);
					mGraph.setProperties(checkx, checky, checkz, checkmodulo,
							"", "ω (" + getString(R.string.unidad_giroscopio)
									+ ")", calidad, tamano);
					if (!init) {
						view = mGraph.getGraph();
						layout.addView(view);
						init = true;
					} else {
						layout.removeView(view);
						view = mGraph.getGraph();
						layout.addView(view);
					}
				}
				break;
			case Sensor.TYPE_LIGHT:
				double x3 = event.values[0];
				double timestamp3 = System.currentTimeMillis();

				AccelData2 data3 = new AccelData2(timestamp3, x3);
				sensorLuz.add(data3);
				if (sensor == Sensor.TYPE_LIGHT) {
					ejex.setVisibility(CheckBox.VISIBLE);
					ejey.setVisibility(CheckBox.GONE);
					ejez.setVisibility(CheckBox.GONE);
					moduloc.setVisibility(CheckBox.GONE);
					nombresensor = getString(R.string.luminosidad);
					setTitle(nombresensor);
					if (luz == true) {
						graba.setText("REC");
						graba.setVisibility(TextView.VISIBLE);
						graba2.setVisibility(TextView.INVISIBLE);
					} else {
						graba2.setText("REC");
						graba2.setVisibility(TextView.VISIBLE);
						graba.setVisibility(TextView.INVISIBLE);
					}
					mGraph = new Graph(this);
					mGraph.ejeY2(sensorLuz);
					mGraph.ejeX2(sensorLuz);
					mGraph.initData2(sensorLuz);
					mGraph.setProperties2(checkx, "", "E ("
							+ getString(R.string.unidad_luz) + ")", calidad,
							tamano);
					if (!init) {
						view = mGraph.getGraph();
						layout.addView(view);
						init = true;
					} else {
						layout.removeView(view);
						view = mGraph.getGraph();
						layout.addView(view);
					}
				}
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				double x4 = event.values[0];
				double y4 = event.values[1];
				double z4 = event.values[2];
				double modulo4 = Double.valueOf(Math.abs(Math.sqrt(Math.pow(x4,
						2) + Math.pow(y4, 2) + Math.pow(z4, 2))));
				double timestamp4 = System.currentTimeMillis();

				AccelData data4 = new AccelData(timestamp4, x4, y4, z4, modulo4);
				sensorMagnetico.add(data4);
				if (sensor == Sensor.TYPE_MAGNETIC_FIELD) {
					ejex.setVisibility(CheckBox.VISIBLE);
					ejey.setVisibility(CheckBox.VISIBLE);
					ejez.setVisibility(CheckBox.VISIBLE);
					moduloc.setVisibility(CheckBox.VISIBLE);
					nombresensor = getString(R.string.magnetico);
					setTitle(nombresensor);
					if (magne == true) {
						graba.setText("REC");
						graba.setVisibility(TextView.VISIBLE);
						graba2.setVisibility(TextView.INVISIBLE);
					} else {
						graba2.setText("REC");
						graba2.setVisibility(TextView.VISIBLE);
						graba.setVisibility(TextView.INVISIBLE);
					}
					mGraph = new Graph(this);
					mGraph.ejeY(sensorMagnetico);
					mGraph.ejeX(sensorMagnetico);
					mGraph.initData(sensorMagnetico);
					mGraph.setProperties(checkx, checky, checkz, checkmodulo,
							"",
							"B (" + getString(R.string.unidad_campo_magnetico)
									+ ")", calidad, tamano);
					if (!init) {
						view = mGraph.getGraph();
						layout.addView(view);
						init = true;
					} else {
						layout.removeView(view);
						view = mGraph.getGraph();
						layout.addView(view);
					}
				}
				break;
			case Sensor.TYPE_PROXIMITY:
				double x5 = event.values[0];
				double timestamp5 = System.currentTimeMillis();

				AccelData2 data5 = new AccelData2(timestamp5, x5);
				sensorProximidad.add(data5);
				if (sensor == Sensor.TYPE_PROXIMITY) {
					ejex.setVisibility(CheckBox.VISIBLE);
					ejey.setVisibility(CheckBox.GONE);
					ejez.setVisibility(CheckBox.GONE);
					moduloc.setVisibility(CheckBox.GONE);
					nombresensor = getString(R.string.proximidad);
					setTitle(nombresensor);
					if (proxi == true) {
						graba.setText("REC");
						graba.setVisibility(TextView.VISIBLE);
						graba2.setVisibility(TextView.INVISIBLE);
					} else {
						graba2.setText("REC");
						graba2.setVisibility(TextView.VISIBLE);
						graba.setVisibility(TextView.INVISIBLE);
					}
					mGraph = new Graph(this);
					mGraph.ejeY2(sensorProximidad);
					mGraph.ejeX2(sensorProximidad);
					mGraph.initData2(sensorProximidad);
					mGraph.setProperties2(checkx, "", "d ("
							+ getString(R.string.unidad_proximidad) + ")",
							calidad, tamano);
					if (!init) {
						view = mGraph.getGraph();
						layout.addView(view);
						init = true;
					} else {
						layout.removeView(view);
						view = mGraph.getGraph();
						layout.addView(view);
					}
				}
				break;
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

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
		funciona = false;
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));

	}

	protected void Iniciar_sensores() {
		funciona = true;

		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				frecuencia);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				frecuencia);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), frecuencia);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				frecuencia);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
				frecuencia);
	}

	private class SaveThread extends Thread {

		DecimalFormat formateador = new DecimalFormat("0.00##");

		@Override
		public void run() {
			if (g == true) {
				double t = gpsdatos.peek().getTimestamp();
				StringBuilder csvData = new StringBuilder();
				csvData.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvData.append("t (s);Latitud;Longitud\n");
				for (GpsDatos values : gpsdatos) {
					double tiempo = (values.getTimestamp() - t) / 1000;
					csvData.append(String.valueOf(tiempo) + ";"
							+ String.valueOf(values.getLatitud()) + ";"
							+ String.valueOf(values.getLongitud()) + "\n");
				}

				Bundle bundle = new Bundle();
				Message msg = new Message();

				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = "GPS "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvData.toString().getBytes());
						fileOutputStream.close();

					}

					// Si se ha guardado con éxito enviamos un mensaje al
					// controlador de mensajes y lo muestra
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.guardado_gps));
				} catch (Exception e) {
					// Si no se ha podido guardar entonces nos envía un
					// mensaje
					// diciendo que no se ha guardado
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.error_guardar));
				}
				msg.setData(bundle);
				// Envía el mensaje al controlador
				mensajeria.sendMessage(msg);
			}
			if (acce == true) {
				double t = sensorDatas.peek().getTimestamp();

				StringBuilder csvData = new StringBuilder();
				csvData.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvData.append("t (s);X;Y;Z;Modulo;Unidad sensor: "
						+ getResources()
								.getString(R.string.unidad_acelerometro) + "\n");
				for (AccelData values : sensorDatas) {
					double tiempo = (values.getTimestamp() - t) / 1000;
					csvData.append(String.valueOf(tiempo)
							+ ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ ";"
							+ String.valueOf(formateador.format(values.getY()))
							+ ";"
							+ String.valueOf(formateador.format(values.getZ()))
							+ ";"
							+ String.valueOf(formateador.format(values
									.getModulo())) + "\n");
				}

				Bundle bundle = new Bundle();
				Message msg = new Message();

				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.acelerometro)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvData.toString().getBytes());
						fileOutputStream.close();

					}

					// Si se ha guardado con éxito enviamos un mensaje al
					// controlador de mensajes y lo muestra
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.guardado_acelerometro));
				} catch (Exception e) {
					// Si no se ha podido guardar entonces nos envía un mensaje
					// diciendo que no se ha guardado
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.error_guardar));
				}
				msg.setData(bundle);
				// Envía el mensaje al controlador
				mensajeria.sendMessage(msg);
			}

			if (giro == true) {
				double t = sensorGiroscopio.peek().getTimestamp();

				StringBuilder csvData = new StringBuilder();
				csvData.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvData.append("t (s);X;Y;Z;Modulo;Unidad sensor: "
						+ getResources().getString(R.string.unidad_giroscopio)
						+ "\n");
				for (AccelData values : sensorGiroscopio) {
					double tiempo = (values.getTimestamp() - t) / 1000;

					csvData.append(String.valueOf(tiempo)
							+ ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ ";"
							+ String.valueOf(formateador.format(values.getY()))
							+ ";"
							+ String.valueOf(formateador.format(values.getZ()))
							+ ";"
							+ String.valueOf(formateador.format(values
									.getModulo())) + "\n");
				}

				Bundle bundle = new Bundle();
				Message msg = new Message();

				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.giroscopio)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvData.toString().getBytes());
						fileOutputStream.close();

					}

					// Si se ha guardado con éxito enviamos un mensaje al
					// controlador de mensajes y lo muestra
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.guardado_giroscopio));
				} catch (Exception e) {
					// Si no se ha podido guardar entonces nos envía un mensaje
					// diciendo que no se ha guardado
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.error_guardar));
				}
				msg.setData(bundle);
				// Envía el mensaje al controlador
				mensajeria.sendMessage(msg);

			}
			if (magne == true) {
				double t = sensorMagnetico.peek().getTimestamp();

				StringBuilder csvData = new StringBuilder();
				csvData.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvData.append("t (s);X;Y;Z;Modulo;Unidad sensor: "
						+ getResources().getString(
								R.string.unidad_campo_magnetico) + "\n");
				for (AccelData values : sensorMagnetico) {
					double tiempo = (values.getTimestamp() - t) / 1000;

					csvData.append(String.valueOf(tiempo)
							+ ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ ";"
							+ String.valueOf(formateador.format(values.getY()))
							+ ";"
							+ String.valueOf(formateador.format(values.getZ()))
							+ ";"
							+ String.valueOf(formateador.format(values
									.getModulo())) + "\n");
				}

				Bundle bundle = new Bundle();
				Message msg = new Message();

				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.magnetico)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvData.toString().getBytes());
						fileOutputStream.close();

					}

					// Si se ha guardado con éxito enviamos un mensaje al
					// controlador de mensajes y lo muestra
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.guardado_magnetometro));
				} catch (Exception e) {
					// Si no se ha podido guardar entonces nos envía un mensaje
					// diciendo que no se ha guardado
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.error_guardar));
				}
				msg.setData(bundle);
				// Envía el mensaje al controlador
				mensajeria.sendMessage(msg);
			}
			if (luz == true) {
				double t = sensorLuz.peek().getTimestamp();

				StringBuilder csvData = new StringBuilder();
				csvData.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvData.append("t (s);X;Unidad sensor: "
						+ getResources().getString(R.string.unidad_luz) + "\n");
				for (AccelData2 values : sensorLuz) {
					double tiempo = (values.getTimestamp() - t) / 1000;
					csvData.append(String.valueOf(tiempo) + ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ "\n");
				}

				Bundle bundle = new Bundle();
				Message msg = new Message();

				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.luminosidad)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvData.toString().getBytes());
						fileOutputStream.close();

					}

					// Si se ha guardado con éxito enviamos un mensaje al
					// controlador de mensajes y lo muestra
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.guardar_luz));
				} catch (Exception e) {
					// Si no se ha podido guardar entonces nos envía un mensaje
					// diciendo que no se ha guardado
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.error_guardar));
				}
				msg.setData(bundle);
				// Envía el mensaje al controlador
				mensajeria.sendMessage(msg);
			}
			if (proxi == true) {
				double t = sensorProximidad.peek().getTimestamp();

				StringBuilder csvData = new StringBuilder();
				csvData.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvData.append("t (s);X;Modulo;Unidad sensor: "
						+ getResources().getString(R.string.unidad_proximidad)
						+ "\n");
				for (AccelData2 values : sensorProximidad) {
					double tiempo = (values.getTimestamp() - t) / 1000;

					csvData.append(String.valueOf(tiempo) + ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ "\n");
				}

				Bundle bundle = new Bundle();
				Message msg = new Message();

				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.proximidad)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvData.toString().getBytes());
						fileOutputStream.close();

					}

					// Si se ha guardado con éxito enviamos un mensaje al
					// controlador de mensajes y lo muestra
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.guardar_proximidad));
				} catch (Exception e) {
					// Si no se ha podido guardar entonces nos envía un mensaje
					// diciendo que no se ha guardado
					bundle.putString("msg", Grafica.this.getResources()
							.getString(R.string.error_guardar));
				}
				msg.setData(bundle);
				// Envía el mensaje al controlador
				mensajeria.sendMessage(msg);
			}
		}
	}

	// Función para guardar los datos obtenidos de los sensores
	private void saveHistory() {
		String stadoSD = Environment.getExternalStorageState();
		// Comprobamos si podemos acceder a la memoria sd, si no se puede lanza
		// este mensaje y solo se podrían compartir los datos
		if (!stadoSD.equals(Environment.MEDIA_MOUNTED)
				&& !stadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			Toast.makeText(this, "Inserte una tarjeta SD.", Toast.LENGTH_LONG)
					.show();
			return;
		}

		SaveThread thread = new SaveThread();

		thread.start();

	}

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		switch (boton.getId()) {
		case (R.id.lupa):
			if (funciona == false && sensor == Sensor.TYPE_ACCELEROMETER) {
				acelerometro();
			} else if (funciona == false && sensor == Sensor.TYPE_GYROSCOPE) {
				giroscopio();
			} else if (funciona == false
					&& sensor == Sensor.TYPE_MAGNETIC_FIELD) {
				magnetometro();
			} else if (funciona == false && sensor == Sensor.TYPE_PROXIMITY) {
				proximidad();
			} else if (funciona == false && sensor == Sensor.TYPE_LIGHT) {
				luz();
			}
			break;
		case (R.id.parar):
			continuar.setEnabled(true);
			parar.setEnabled(false);
			reiniciar.setEnabled(true);
			lupa.setVisibility(ImageButton.VISIBLE);
			onStop();
			break;
		case (R.id.inicio):
			vercargar = true;
			iniciar.setVisibility(Button.GONE);
			continuar.setVisibility(Button.VISIBLE);
			continuar.setEnabled(false);
			parar.setEnabled(true);
			reiniciar.setEnabled(false);
			Iniciar_sensores();
			if (tiempoParada > 0) {
				contadores2();
				continuar.setEnabled(false);
				parar.setEnabled(false);
				reiniciar.setEnabled(false);
			}
			break;
		case (R.id.continuar):
			parar.setEnabled(true);
			reiniciar.setEnabled(false);
			continuar.setEnabled(false);
			lupa.setVisibility(ImageButton.INVISIBLE);
			Iniciar_sensores();
			break;
		case (R.id.reiniciar):
			lupa.setVisibility(ImageButton.INVISIBLE);
			iniciar.setVisibility(Button.VISIBLE);
			iniciar.setEnabled(true);
			continuar.setVisibility(Button.GONE);
			ejex.setVisibility(CheckBox.GONE);
			ejey.setVisibility(CheckBox.GONE);
			ejez.setVisibility(CheckBox.GONE);
			moduloc.setVisibility(CheckBox.GONE);
			graba.setVisibility(TextView.INVISIBLE);
			graba2.setVisibility(TextView.INVISIBLE);
			parar.setEnabled(false);
			reiniciar.setEnabled(false);
			if (tiempoInicio > 0) {
				contadores();
				iniciar.setEnabled(false);
			}
			layout.removeView(view);
			for (AccelData data : sensorDatas) {
				sensorDatas.remove(data);
			}
			for (AccelData data : sensorGiroscopio) {
				sensorGiroscopio.remove(data);
			}
			for (AccelData data : sensorMagnetico) {
				sensorMagnetico.remove(data);
			}
			for (AccelData2 data : sensorLuz) {
				sensorLuz.remove(data);
			}
			for (AccelData2 data : sensorProximidad) {
				sensorProximidad.remove(data);
			}
			if (gpsdatos.size() > 0) {
				for (GpsDatos data : gpsdatos) {
					gpsdatos.remove(data);
				}
			}
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

	// Comprobamos si hemos empezado a usar los sensores. Si las colas están
	// vacías entonces en las opciones no aparecen esos sensores para que no
	// aparezca un error y finalice la aplicación.

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (sensorDatas.size() > 0) {
			MenuItem item = menu.findItem(R.id.acele);
			item.setVisible(true);
		} else {
			MenuItem item = menu.findItem(R.id.acele);
			item.setVisible(false);
		}
		if (sensorGiroscopio.size() > 0) {
			MenuItem item = menu.findItem(R.id.giro);
			item.setVisible(true);
		} else {
			MenuItem item = menu.findItem(R.id.giro);
			item.setVisible(false);
		}
		if (sensorMagnetico.size() > 0) {
			MenuItem item = menu.findItem(R.id.mag);
			item.setVisible(true);
		} else {
			MenuItem item = menu.findItem(R.id.mag);
			item.setVisible(false);
		}
		if (sensorProximidad.size() > 0) {
			MenuItem item = menu.findItem(R.id.proxi);
			item.setVisible(true);
		} else {
			MenuItem item = menu.findItem(R.id.proxi);
			item.setVisible(false);
		}
		if (sensorLuz.size() > 0) {
			MenuItem item = menu.findItem(R.id.luz);
			item.setVisible(true);
		} else {
			MenuItem item = menu.findItem(R.id.luz);
			item.setVisible(false);
		}
		if (sensorLuz.size() == 0 && sensorProximidad.size() == 0
				&& sensorMagnetico.size() == 0 && sensorGiroscopio.size() == 0
				&& sensorDatas.size() == 0) {
			MenuItem item = menu.findItem(R.id.guardar);
			item.setEnabled(false);
		} else {
			MenuItem item = menu.findItem(R.id.guardar);
			item.setEnabled(true);
		}
		if (sensorLuz.size() == 0 && sensorProximidad.size() == 0
				&& sensorMagnetico.size() == 0 && sensorGiroscopio.size() == 0
				&& sensorDatas.size() == 0) {
			MenuItem item = menu.findItem(R.id.enviar);
			item.setEnabled(false);
		} else {
			MenuItem item = menu.findItem(R.id.enviar);
			item.setEnabled(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Elegimos entre las opciones disponibles en esta pantalla
		switch (item.getItemId()) {
		case R.id.menu_ayuda:
			Intent ayuda = new Intent(this, Ayuda.class);
			final String[] TITLES = { getString(R.string.grafica),
					getString(R.string.inicio), getString(R.string.medicion),
					getString(R.string.cargargraficas),
					getString(R.string.teoria) };
			ayuda.putExtra("TITLES", TITLES);
			startActivity(ayuda);
			break;
		case (R.id.acele):
			sensor = Sensor.TYPE_ACCELEROMETER;
			if (funciona == false) {
				acelerometro();
			}
			break;
		case (R.id.giro):
			sensor = Sensor.TYPE_GYROSCOPE;
			if (funciona == false) {
				giroscopio();
			}
			break;
		case (R.id.mag):
			sensor = Sensor.TYPE_MAGNETIC_FIELD;
			if (funciona == false) {
				magnetometro();
			}
			break;
		case (R.id.proxi):
			sensor = Sensor.TYPE_PROXIMITY;
			if (funciona == false) {
				proximidad();
			}
			break;
		case (R.id.luz):
			sensor = Sensor.TYPE_LIGHT;
			if (funciona == false) {
				luz();
			}
			break;
		case (R.id.guardar):
			Log.d("algo", "boton guardar");
			saveHistory();
			break;
		case (R.id.enviar):
			ArrayList<Uri> ficheros = new ArrayList<Uri>();
			DecimalFormat formateador = new DecimalFormat("0.00##");
			if (g == true) {
				double t = gpsdatos.peek().getTimestamp();
				StringBuilder csvData = new StringBuilder();
				csvData.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvData.append("t (s);Latitud;Longitud\n");
				for (GpsDatos values : gpsdatos) {
					double tiempo = (values.getTimestamp() - t) / 1000;
					csvData.append(String.valueOf(tiempo) + ";"
							+ String.valueOf(values.getLatitud()) + ";"
							+ String.valueOf(values.getLongitud()) + "\n");
				}
				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = "GPS "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvData.toString().getBytes());
						fileOutputStream.close();
						Uri path = Uri.fromFile(file);
						ficheros.add(path);
					}

				} catch (Exception e) {
				}
			}
			if (acce == true) {
				// crear el fichero escribirle
				double t = sensorDatas.peek().getTimestamp();

				StringBuilder csvDataexportar = new StringBuilder();
				csvDataexportar.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvDataexportar
						.append("t (s);X;Y;Z;Modulo;Unidad sensor: "
								+ getResources().getString(
										R.string.unidad_acelerometro) + "\n");
				for (AccelData values : sensorDatas) {
					double tiempo = (values.getTimestamp() - t) / 1000;
					csvDataexportar.append(String.valueOf(tiempo)
							+ ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ ";"
							+ String.valueOf(formateador.format(values.getY()))
							+ ";"
							+ String.valueOf(formateador.format(values.getZ()))
							+ ";"
							+ String.valueOf(formateador.format(values
									.getModulo())) + "\n");
				}
				try {
					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.acelerometro)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dir, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvDataexportar.toString()
								.getBytes());
						fileOutputStream.close();
						Uri path = Uri.fromFile(file);
						ficheros.add(path);

					}
				} catch (Exception e) {

				}
			}
			if (giro == true) {
				double t = sensorGiroscopio.peek().getTimestamp();

				StringBuilder csvDatagiro = new StringBuilder();
				csvDatagiro.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvDatagiro.append("t (s);X;Y;Z;Modulo;Unidad sensor: "
						+ getResources().getString(R.string.unidad_giroscopio)
						+ "\n");
				for (AccelData values : sensorGiroscopio) {
					double tiempo = (values.getTimestamp() - t) / 1000;

					csvDatagiro.append(String.valueOf(tiempo)
							+ ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ ";"
							+ String.valueOf(formateador.format(values.getY()))
							+ ";"
							+ String.valueOf(formateador.format(values.getZ()))
							+ ";"
							+ String.valueOf(formateador.format(values
									.getModulo())) + "\n");
				}
				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.giroscopio)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvDatagiro.toString()
								.getBytes());
						fileOutputStream.close();
						Uri path = Uri.fromFile(file);
						ficheros.add(path);

					}

				} catch (Exception e) {
				}
			}
			if (magne == true) {
				double t = sensorMagnetico.peek().getTimestamp();

				StringBuilder csvDatamagne = new StringBuilder();
				csvDatamagne.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvDatamagne.append("t (s);X;Y;Z;Modulo;Unidad sensor: "
						+ getResources().getString(
								R.string.unidad_campo_magnetico) + "\n");
				for (AccelData values : sensorMagnetico) {
					double tiempo = (values.getTimestamp() - t) / 1000;

					csvDatamagne.append(String.valueOf(tiempo)
							+ ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ ";"
							+ String.valueOf(formateador.format(values.getY()))
							+ ";"
							+ String.valueOf(formateador.format(values.getZ()))
							+ ";"
							+ String.valueOf(formateador.format(values
									.getModulo())) + "\n");
				}

				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.magnetico)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvDatamagne.toString()
								.getBytes());
						fileOutputStream.close();
						Uri path = Uri.fromFile(file);
						ficheros.add(path);

					}

				} catch (Exception e) {
				}
			}
			if (luz == true) {
				double t = sensorLuz.peek().getTimestamp();

				StringBuilder csvDataluz = new StringBuilder();
				csvDataluz.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvDataluz.append("t (s);X;Modulo;Unidad sensor: "
						+ getResources().getString(R.string.unidad_luz) + "\n");
				for (AccelData2 values : sensorLuz) {
					double tiempo = (values.getTimestamp() - t) / 1000;
					csvDataluz.append(String.valueOf(tiempo) + ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ "\n");
				}

				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.luminosidad)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream
								.write(csvDataluz.toString().getBytes());
						fileOutputStream.close();
						Uri path = Uri.fromFile(file);
						ficheros.add(path);

					}

				} catch (Exception e) {
				}
			}
			if (proxi == true) {
				double t = sensorProximidad.peek().getTimestamp();

				StringBuilder csvDataproxi = new StringBuilder();
				csvDataproxi.append("Nombre del dispositivo: "
						+ android.os.Build.MODEL
						+ ";Fecha: "
						+ DateFormat.format("dd/MM/yyyy",
								System.currentTimeMillis()).toString() + "\n");
				csvDataproxi.append("t (s);X;Modulo;Unidad sensor: "
						+ getResources().getString(R.string.unidad_proximidad)
						+ "\n");
				for (AccelData2 values : sensorProximidad) {
					double tiempo = (values.getTimestamp() - t) / 1000;

					csvDataproxi.append(String.valueOf(tiempo) + ";"
							+ String.valueOf(formateador.format(values.getX()))
							+ "\n");
				}

				try {

					String appName = getResources()
							.getString(R.string.app_name);
					String dirPath = Environment.getExternalStorageDirectory()
							.toString() + "/" + appName;
					File dir = new File(dirPath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = getResources().getString(
							R.string.proximidad)
							+ " "
							+ DateFormat
									.format("dd-MM-yyyy kk-mm-ss",
											System.currentTimeMillis())
									.toString().concat(".csv");

					File file = new File(dirPath, fileName);
					if (file.createNewFile()) {
						FileOutputStream fileOutputStream = new FileOutputStream(
								file);

						fileOutputStream.write(csvDataproxi.toString()
								.getBytes());
						fileOutputStream.close();
						Uri path = Uri.fromFile(file);
						ficheros.add(path);

					}
				} catch (Exception e) {
				}
			}
			enviar(ficheros);
			break;

		}

		return true;

		/** true -> consumimos el item, no se propaga */
	}

	protected void enviar(ArrayList<Uri> csvexportar) {
		// TODO Auto-generated method stub
		this.setProgressBarVisibility(false);
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

		sendIntent.setType("file/*");
		sendIntent.putExtra(Intent.EXTRA_STREAM, csvexportar);
		startActivity(sendIntent);
	}

	// comprobamos si los chechbox cambian. Si cambian cuando los sensores están
	// parados entonces llamamos a la función de cada sensor para representar
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.ejex:

			checkx = isChecked;
			if (funciona == false && sensor == Sensor.TYPE_ACCELEROMETER) {
				acelerometro();
			} else if (funciona == false && sensor == Sensor.TYPE_GYROSCOPE) {
				giroscopio();
			} else if (funciona == false
					&& sensor == Sensor.TYPE_MAGNETIC_FIELD) {
				magnetometro();
			} else if (funciona == false && sensor == Sensor.TYPE_PROXIMITY) {
				proximidad();
			} else if (funciona == false && sensor == Sensor.TYPE_LIGHT) {
				luz();
			}
			break;
		case R.id.ejey:

			checky = isChecked;
			if (funciona == false && sensor == Sensor.TYPE_ACCELEROMETER) {
				acelerometro();
			} else if (funciona == false && sensor == Sensor.TYPE_GYROSCOPE) {
				giroscopio();
			} else if (funciona == false
					&& sensor == Sensor.TYPE_MAGNETIC_FIELD) {
				magnetometro();
			} else if (funciona == false && sensor == Sensor.TYPE_PROXIMITY) {
				proximidad();
			} else if (funciona == false && sensor == Sensor.TYPE_LIGHT) {
				luz();
			}
			break;
		case R.id.ejez:
			checkz = isChecked;
			if (funciona == false && sensor == Sensor.TYPE_ACCELEROMETER) {
				acelerometro();
			} else if (funciona == false && sensor == Sensor.TYPE_GYROSCOPE) {
				giroscopio();
			} else if (funciona == false
					&& sensor == Sensor.TYPE_MAGNETIC_FIELD) {
				magnetometro();
			} else if (funciona == false && sensor == Sensor.TYPE_PROXIMITY) {
				proximidad();
			} else if (funciona == false && sensor == Sensor.TYPE_LIGHT) {
				luz();
			}
			break;
		case R.id.modulo:
			checkmodulo = isChecked;
			if (funciona == false && sensor == Sensor.TYPE_ACCELEROMETER) {
				acelerometro();
			} else if (funciona == false && sensor == Sensor.TYPE_GYROSCOPE) {
				giroscopio();
			} else if (funciona == false
					&& sensor == Sensor.TYPE_MAGNETIC_FIELD) {
				magnetometro();
			} else if (funciona == false && sensor == Sensor.TYPE_PROXIMITY) {
				proximidad();
			} else if (funciona == false && sensor == Sensor.TYPE_LIGHT) {
				luz();
			}
			break;
		}

	}

	public void acelerometro() {
		ejex.setVisibility(CheckBox.VISIBLE);
		ejey.setVisibility(CheckBox.VISIBLE);
		ejez.setVisibility(CheckBox.VISIBLE);
		moduloc.setVisibility(CheckBox.VISIBLE);
		nombresensor = getString(R.string.acelerometro);
		setTitle(nombresensor);
		if (acce == true) {
			graba.setText("REC");
			graba.setVisibility(TextView.VISIBLE);
			graba2.setVisibility(TextView.INVISIBLE);
		} else {
			graba2.setText("REC");
			graba2.setVisibility(TextView.VISIBLE);
			graba.setVisibility(TextView.INVISIBLE);
		}
		mGraph = new Graph(this);
		mGraph.ejeY(sensorDatas);
		mGraph.ejeX(sensorDatas);
		mGraph.initData(sensorDatas);
		mGraph.setProperties(checkx, checky, checkz, checkmodulo, "", "a ("
				+ getString(R.string.unidad_acelerometro) + ")", calidad,
				tamano);
		if (!init) {
			view = mGraph.getGraph();
			layout.addView(view);
			init = true;
		} else {
			layout.removeView(view);
			view = mGraph.getGraph();
			layout.addView(view);
		}
	}

	public void giroscopio() {
		ejex.setVisibility(CheckBox.VISIBLE);
		ejey.setVisibility(CheckBox.VISIBLE);
		ejez.setVisibility(CheckBox.VISIBLE);
		moduloc.setVisibility(CheckBox.VISIBLE);
		nombresensor = getString(R.string.giroscopio);
		setTitle(nombresensor);
		if (giro == true) {
			graba.setText("REC");
			graba.setVisibility(TextView.VISIBLE);
			graba2.setVisibility(TextView.INVISIBLE);
		} else {
			graba2.setText("REC");
			graba2.setVisibility(TextView.VISIBLE);
			graba.setVisibility(TextView.INVISIBLE);
		}
		mGraph = new Graph(this);
		mGraph.ejeY(sensorGiroscopio);
		mGraph.ejeX(sensorGiroscopio);
		mGraph.initData(sensorGiroscopio);
		mGraph.setProperties(checkx, checky, checkz, checkmodulo, "", "ω ("
				+ getString(R.string.unidad_giroscopio) + ")", calidad, tamano);
		if (!init) {
			view = mGraph.getGraph();
			layout.addView(view);
			init = true;
		} else {
			layout.removeView(view);
			view = mGraph.getGraph();
			layout.addView(view);
		}
	}

	public void magnetometro() {
		ejex.setVisibility(CheckBox.VISIBLE);
		ejey.setVisibility(CheckBox.VISIBLE);
		ejez.setVisibility(CheckBox.VISIBLE);
		moduloc.setVisibility(CheckBox.VISIBLE);
		nombresensor = getString(R.string.magnetico);
		setTitle(nombresensor);
		if (magne == true) {
			graba.setText("REC");
			graba.setVisibility(TextView.VISIBLE);
			graba2.setVisibility(TextView.INVISIBLE);
		} else {
			graba2.setText("REC");
			graba2.setVisibility(TextView.VISIBLE);
			graba.setVisibility(TextView.INVISIBLE);
		}
		mGraph = new Graph(this);
		mGraph.ejeY(sensorMagnetico);
		mGraph.ejeX(sensorMagnetico);
		mGraph.initData(sensorMagnetico);
		mGraph.setProperties(checkx, checky, checkz, checkmodulo, "", "B ("
				+ getString(R.string.unidad_campo_magnetico) + ")", calidad,
				tamano);
		if (!init) {
			view = mGraph.getGraph();
			layout.addView(view);
			init = true;
		} else {
			layout.removeView(view);
			view = mGraph.getGraph();
			layout.addView(view);
		}
	}

	public void proximidad() {
		ejex.setVisibility(CheckBox.VISIBLE);
		ejey.setVisibility(CheckBox.GONE);
		ejez.setVisibility(CheckBox.GONE);
		moduloc.setVisibility(CheckBox.GONE);
		nombresensor = getString(R.string.proximidad);
		setTitle(nombresensor);
		if (proxi == true) {
			graba.setText("REC");
			graba.setVisibility(TextView.VISIBLE);
			graba2.setVisibility(TextView.INVISIBLE);
		} else {
			graba2.setText("REC");
			graba2.setVisibility(TextView.VISIBLE);
			graba.setVisibility(TextView.INVISIBLE);
		}
		mGraph = new Graph(this);
		mGraph.ejeY2(sensorProximidad);
		mGraph.ejeX2(sensorProximidad);
		mGraph.initData2(sensorProximidad);
		mGraph.setProperties2(checkx, "", "d ("
				+ getString(R.string.unidad_proximidad) + ")", calidad, tamano);
		if (!init) {
			view = mGraph.getGraph();
			layout.addView(view);
			init = true;
		} else {
			layout.removeView(view);
			view = mGraph.getGraph();
			layout.addView(view);
		}
	}

	public void luz() {
		ejex.setVisibility(CheckBox.VISIBLE);
		ejey.setVisibility(CheckBox.GONE);
		ejez.setVisibility(CheckBox.GONE);
		moduloc.setVisibility(CheckBox.GONE);
		nombresensor = getString(R.string.luminosidad);
		setTitle(nombresensor);
		if (luz == true) {
			graba.setText("REC");
			graba.setVisibility(TextView.VISIBLE);
			graba2.setVisibility(TextView.INVISIBLE);
		} else {
			graba2.setText("REC");
			graba2.setVisibility(TextView.VISIBLE);
			graba.setVisibility(TextView.INVISIBLE);
		}
		mGraph = new Graph(this);
		mGraph.ejeY2(sensorLuz);
		mGraph.ejeX2(sensorLuz);
		mGraph.initData2(sensorLuz);
		mGraph.setProperties2(checkx, "", "E ("
				+ getString(R.string.unidad_luz) + ")", calidad, tamano);
		if (!init) {
			view = mGraph.getGraph();
			layout.addView(view);
			init = true;
		} else {
			layout.removeView(view);
			view = mGraph.getGraph();
			layout.addView(view);
		}
	}

}
