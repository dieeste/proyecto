package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.androidplot.xy.XYPlot;

public class Acelerometro extends Activity implements SensorEventListener {

	EditText contador;
	TextView cuenta;
	TextView mostrar;
	Button empezar;
	Button parar;
	Button enviar;
	Chronometer cronometro;
	XYPlot mySimleXYPlot;
	SensorManager mSensorManager;

	int tiempoParada;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acelerometro);
		
		cuenta = (TextView) findViewById(R.id.cuenta);
		empezar = (Button) findViewById(R.id.empezar);
		parar = (Button) findViewById(R.id.parar);
		enviar = (Button) findViewById(R.id.enviar);
		contador = (EditText) findViewById(R.id.contador);
		mostrar = (TextView) findViewById(R.id.mostrar);
		cronometro = (Chronometer) findViewById(R.id.cronometro);
		mySimleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		
		cronometro.setOnChronometerTickListener(new OnChronometerTickListener() {

					@Override
					public void onChronometerTick(Chronometer chronometer) {
						// TODO Auto-generated method stub

						long tiempoTranscurrido= SystemClock.elapsedRealtime()
								- chronometer.getBase();
						int tiempoSeg = (int) tiempoTranscurrido / 1000;
						if (tiempoSeg == tiempoParada) {
							// finish();
							cronometro.stop();
							onStop();
						}
						mostrar.setText("Tiempo hasta parada: "
								+ (tiempoParada - tiempoSeg) + " seg");
					}
				});

		empezar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// String resultado = "";
				// int cont = Integer.parseInt(contador.getText().toString());
				tiempoParada = Integer.parseInt(contador.getText()
						.toString());
				// String ini = "";
				// TODO Auto-generated method stub
				cronometro.setBase(SystemClock.elapsedRealtime());
				cronometro.start();
				// Log.i("cronometro","vemos como cambia: " +
				// String.valueOf(SystemClock.elapsedRealtime()));
				// Log.i("tiempo","time now: " +
				// String.valueOf(cronometro.getBase()));
				// Log.i("tiempo","time now3: " +
				// String.valueOf(SystemClock.elapsedRealtime()/1000));
				// if (SystemClock.elapsedRealtime()/1000>conta*10000){
				/*
				 * if (currentThreadTimeMillis ()==conta*1000){ //resultado +=
				 * String.valueOf(i); cronometro.stop(); onStop();
				 * //onRestart(); }
				 */
				// mostrar.setText(resultado+"\n");*/

			}
		});

		parar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPause();
				cronometro.stop();
			}
		});
		enviar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent send = new Intent(Intent.ACTION_SEND);
				send.setType("text/html");
				startActivity(send);
				
			}
		});
	}

	protected void Iniciar_sensores() {
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void Parar_sensores() {
		mSensorManager.unregisterListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
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
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}
