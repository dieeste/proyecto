package com.example.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
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

public class Simulacion extends Activity implements SensorEventListener, OnClickListener {

	SensorManager mSensorManager;
	TextView detecta;
	TextView acelerometro;
	TextView gravedad;
	TextView giro;
	TextView orientacion;
	TextView magnetico;
	TextView proximidad;
	TextView luminosidad;
	TextView temperatura;
	TextView presion;
	Button gravity;
	Button acelerometer;
	Button magnetic;

	double x = 0, y = 0, z = 0;
	ArrayList<Double> vector = new ArrayList<Double>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simulacion);

		detecta = (TextView) findViewById(R.id.detecta);
		acelerometro = (TextView) findViewById(R.id.acelerometro);
		gravedad = (TextView) findViewById(R.id.gravedad);
		giro = (TextView) findViewById(R.id.giro);
		orientacion = (TextView) findViewById(R.id.orientacion);
		magnetico = (TextView) findViewById(R.id.magnetico);
		proximidad = (TextView) findViewById(R.id.proximidad);
		luminosidad = (TextView) findViewById(R.id.luminosidad);
		temperatura = (TextView) findViewById(R.id.temperatura);
		presion = (TextView) findViewById(R.id.presion);
		gravity = (Button) findViewById(R.id.gravity);
		acelerometer = (Button) findViewById(R.id.acelerometer);
		magnetic = (Button) findViewById(R.id.magnetic);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		//Escucha de los botones
		acelerometer.setOnClickListener(this);
		gravity.setOnClickListener(this);
		magnetic.setOnClickListener(this);
	}
	@Override
	public void onClick(View boton){
		// TODO Auto-generated method stub
		switch(boton.getId()){
			case R.id.acelerometer:
				Intent acelerometro = new Intent(this,Acelerometro.class);
				startActivity(acelerometro);
				break;
			case R.id.gravity:
				/*Intent gravedad = new Intent(Simulacion.this,Acelerometro.class);
				startActivity(gravedad);*/
				break;
		}
	}

	//Iniciamos cada uno de los sensores que vamos a usar
	protected void Iniciar_sensores() {
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
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

		mSensorManager.registerListener(this, mSensorManager
				.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	//Paramos todos los sensores
	@SuppressWarnings("deprecation")
	protected void Parar_sensores() {
		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));

		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE));
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		String txt = "\n\nSensor: ";
		synchronized (this) {
			Log.d("sensor", event.sensor.getName());

			switch (event.sensor.getType()) {
			
			case Sensor.TYPE_ACCELEROMETER:

				txt += "Acelerómetro\n";
				txt += "\n x: " + event.values[0];
				txt += "\n y: " + event.values[1];
				txt += "\n z: " + event.values[2];
				acelerometro.setText(txt);
				break;
			
			case Sensor.TYPE_GRAVITY:

				txt += "Gravedad\n";
				txt += "\n x: " + event.values[0];
				txt += "\n y: " + event.values[1];
				txt += "\n z: " + event.values[2];
				gravedad.setText(txt);
				break;

			case Sensor.TYPE_MAGNETIC_FIELD:
				txt += "Campo magnético\n";
				txt += "\n" + event.values[0] + " uT";

				magnetico.setText(txt);

				break;
			}

		}

	}protected void onStop() {

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
