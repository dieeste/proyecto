package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Simulacion extends Activity implements SensorEventListener,
		OnClickListener {

	SensorManager mSensorManager;
	TextView detecta;
	TextView acelerometro;
	TextView giroscopo;
	TextView magnetico;
	TextView proximidad;
	TextView luminosidad;
	Button acelerometer;
	Button giroscopio;
	Button magnetic;

	/*double x = 0, y = 0, z = 0;
	ArrayList<Double> vector = new ArrayList<Double>();*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simulacion);

		detecta = (TextView) findViewById(R.id.detecta);
		acelerometro = (TextView) findViewById(R.id.acelerometro);
		giroscopo = (TextView) findViewById(R.id.giroscopo);
		magnetico = (TextView) findViewById(R.id.magnetico);
		proximidad = (TextView) findViewById(R.id.proximidad);
		luminosidad = (TextView) findViewById(R.id.luminosidad);
		acelerometer = (Button) findViewById(R.id.acelerometer);
		giroscopio = (Button) findViewById(R.id.giroscopio);
		magnetic = (Button) findViewById(R.id.magnetic);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Escucha de los botones
		acelerometer.setOnClickListener(this);
		giroscopio.setOnClickListener(this);
		magnetic.setOnClickListener(this);
	}

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		switch (boton.getId()) {
		case R.id.acelerometer:
			Intent acelerometro = new Intent(this, Acelerometro.class);
			startActivity(acelerometro);
			break;
		case R.id.giroscopio:
			/*
			 * Intent gravedad = new Intent(Simulacion.this,Acelerometro.class);
			 * startActivity(gravedad);
			 */
			break;
		case R.id.magnetic:
			/*
			 * Intent gravedad = new Intent(Simulacion.this,Acelerometro.class);
			 * startActivity(gravedad);
			 */
			break;
		}
	}

	// Iniciamos cada uno de los sensores que vamos a usar
	protected void Iniciar_sensores() {
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
				SensorManager.SENSOR_DELAY_NORMAL);

	}

	// Paramos todos los sensores
	protected void Parar_sensores() {
		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));

	}

	//Acciones cuando los sensores van cambiando, en este caso los mostramos
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		String txt = "\n\nSensor: ";
		synchronized (this) {
			Log.d("sensor", event.sensor.getName());
			
			switch (event.sensor.getType()) {

			case Sensor.TYPE_ACCELEROMETER:

				txt += "Acelerómetro\n";
				txt += "\n x: " + event.values[0] + " m/s2";
				txt += "\n y: " + event.values[1] + " m/s2";
				txt += "\n z: " + event.values[2] + " m/s2";
				acelerometro.setText(txt);
				break;

			case Sensor.TYPE_GYROSCOPE:
				txt += "Giroscopio\n";
				txt += "\n x: " + event.values[0] + " rad/s";
				txt += "\n y: " + event.values[1] + " rad/s";
				txt += "\n z: " + event.values[2] + " rad/s";
				giroscopo.setText(txt);
				break;

			case Sensor.TYPE_MAGNETIC_FIELD:
				txt += "Campo magnético\n";
				txt += "\n" + event.values[0] + " µT";

				magnetico.setText(txt);

				break;
			case Sensor.TYPE_PROXIMITY:
				txt += "Proximidad\n";
				txt += "\n" + event.values[0] + " cm";

				proximidad.setText(txt);

				// Si detecta 0 lo represento
				if (event.values[0] == 0) {

					detecta.setBackgroundColor(Color.parseColor("#cf091c"));
					detecta.setText("Proximidad Detectada");
				}

				break;
			case Sensor.TYPE_LIGHT:
				txt += "Luminosidad\n";
				txt += "\n" + event.values[0] + " Lux";

				luminosidad.setText(txt);

				break;
			}

		}

	}

	protected void onStop() {

		Parar_sensores();

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		Parar_sensores();

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		Parar_sensores();

		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub

		Iniciar_sensores();

		super.onRestart();

	}

	@Override
	protected void onResume() {
		super.onResume();

		Iniciar_sensores();

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

}
