package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	Button proximity;
	Button luminosity;
	Sensor giroscope;
	Sensor aceleromete;
	Sensor magnetometro;
	Sensor luces;
	Sensor proximo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simulacion);
		
		// Declarar textView y botones
		
		detecta = (TextView) findViewById(R.id.detecta);
		acelerometro = (TextView) findViewById(R.id.acelerometro);
		giroscopo = (TextView) findViewById(R.id.giroscopo);
		magnetico = (TextView) findViewById(R.id.magnetico);
		proximidad = (TextView) findViewById(R.id.proximidad);
		luminosidad = (TextView) findViewById(R.id.luminosidad);
		acelerometer = (Button) findViewById(R.id.acelerometer);
		giroscopio = (Button) findViewById(R.id.giroscopio);
		magnetic = (Button) findViewById(R.id.magnetic);
		proximity = (Button) findViewById(R.id.proximity);
		luminosity = (Button) findViewById(R.id.luminosity);
		
		
		// declarar los sensores como variables
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		giroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		magnetometro = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		luces = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		proximo = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		aceleromete = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		// Escucha de los botones
		acelerometer.setOnClickListener(this);
		giroscopio.setOnClickListener(this);
		magnetic.setOnClickListener(this);
		proximity.setOnClickListener(this);
		luminosity.setOnClickListener(this);
	}

	@Override
	public void onClick(View boton) {
		// al pulsar los botones nos lleva a la explicacion del sensor.
		switch (boton.getId()) {
		case R.id.acelerometer:
			Intent acelerometro = new Intent(this, DefinicionAcelerometro.class);
			startActivity(acelerometro);
			break;
		case R.id.giroscopio:
			
			Intent gravedad = new Intent(Simulacion.this,DefinicionGiroscopio.class);
			startActivity(gravedad);
			
			break;
		case R.id.magnetic:
			
			 Intent magnetico = new Intent(Simulacion.this,DefinicionMagnetico.class);
			 startActivity(magnetico);
			
			break;
		case R.id.luminosity:
			
			 Intent luz = new Intent(Simulacion.this, DefinicionLuz.class);
			 startActivity(luz);
			
			break;
		case R.id.proximity:
			
			 Intent proximidad = new Intent(Simulacion.this, DefinicionProximidad.class);
			 startActivity(proximidad);
			
			break;
		}
	}

	// Iniciamos cada uno de los sensores que vamos a usar
	
	protected void Iniciar_sensores() {
		//iniciar acelerometro
		if (aceleromete== null) {
			acelerometer.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
		} else{
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		}
		//iniciar giroscopio
		if (giroscope== null) {
			giroscopo.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
		} else{
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_NORMAL);
	    }	
		//iniciar magnetometro
		if (magnetometro== null) {
			magnetic.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
		} else{
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);
		}
		//iniciar proximidad
		if (proximo== null) {
			proximity.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
		} else{
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
				SensorManager.SENSOR_DELAY_NORMAL);
		}
		//iniciar luces
		if (luces== null) {
			luminosidad.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
		} else{
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
				SensorManager.SENSOR_DELAY_NORMAL);
		}
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
		String txt = "\nSensor: ";
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
		// parar sensores
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
	protected void onResume() {
		super.onResume();
		Iniciar_sensores();
		SharedPreferences pref= getSharedPreferences("com.example.app.Preferencias", MODE_PRIVATE);
		int temporizador,tiempo;
		String Tipo;
		
		String prefTiempo = pref.getString("tiempo", "0");
	
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
	                
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Llamamos al menusimulacion
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menusimulacion, menu);
		return true;
		/** true -> el menú ya está visible */
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Llamamos a la clase Preferencias
		switch (item.getItemId()) {
		case R.id.configurate:
			Intent i = new Intent(this, Preferencias.class);
			startActivity(i);
			break;
		}
		return true;
		/** true -> consumimos el item, no se propaga */
	}
	
	
}
