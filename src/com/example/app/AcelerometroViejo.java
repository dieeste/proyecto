package com.example.app;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

public class AcelerometroViejo extends Activity implements SensorEventListener {

	EditText contador;
	TextView cuenta;
	TextView mostrar;
	//TextView mostrar2;
	Button empezar;
	Button parar;
	Button enviar;
	Chronometer cronometro;
	XYPlot mySimleXYPlot;
	SensorManager mSensorManager;

	int tiempoParada;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acelerometroviejo);
		

		cuenta = (TextView) findViewById(R.id.cuenta);
		empezar = (Button) findViewById(R.id.empezar);
		parar = (Button) findViewById(R.id.parar);
		enviar = (Button) findViewById(R.id.enviar);
		contador = (EditText) findViewById(R.id.contador);
		mostrar = (TextView) findViewById(R.id.mostrar);
		//mostrar2 = (TextView) findViewById(R.id.mostrar2);
		cronometro = (Chronometer) findViewById(R.id.cronometro);
		mySimleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Función para controlar el cronómetro y parar al llegar al tiempo
		// indicado
		cronometro
				.setOnChronometerTickListener(new OnChronometerTickListener() {

					@Override
					public void onChronometerTick(Chronometer chronometer) {
						// TODO Auto-generated method stub

						long tiempoTranscurrido = SystemClock.elapsedRealtime()
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
		//Función para coger el tiempo que queremos y parar al llegar a ese tiempo
		empezar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// String resultado = "";
				// int cont = Integer.parseInt(contador.getText().toString());
				onStart();
				tiempoParada = Integer.parseInt(contador.getText().toString());
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
				onDestroy();
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
		//onStop();
		// TODO Auto-generated method stub
		//String txt = "\n\nSensor: ";
		synchronized (this) {
			Log.d("sensor", event.sensor.getName());
			switch (event.sensor.getType()) {

			case Sensor.TYPE_ACCELEROMETER:
				/*txt += "Acelerómetro\n";
				txt += "\n x: " + event.values[0];
				txt += "\n y: " + event.values[1];
				txt += "\n z: " + event.values[2];*/
				//mostrar2.setText(txt);

				Number[] valoresX = {event.values[0],(SystemClock.elapsedRealtime()- cronometro.getBase())/1000};
				Number[] valoresY = {event.values[1],(SystemClock.elapsedRealtime()- cronometro.getBase())/1000};
				Number[] valoresZ = {event.values[2],(SystemClock.elapsedRealtime()- cronometro.getBase())/1000};

				XYSeries series1 = new SimpleXYSeries(Arrays.asList(valoresX), // Array
																				// de
																				// datos
						SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, // Sólo valores
																// verticales
						"X");

				XYSeries series2 = new SimpleXYSeries(Arrays.asList(valoresY), // Array
																				// de
																				// datos
						SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, // Sólo valores
																// verticales
						"Y");
				XYSeries series3 = new SimpleXYSeries(Arrays.asList(valoresZ), // Array
																				// de
																				// datos
						SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, // Sólo valores
																// verticales
						"Z");
				//(SystemClock.elapsedRealtime()- cronometro.getBase())/1000
				LineAndPointFormatter series2Format = new LineAndPointFormatter(
						Color.rgb(100, 200, 100), Color.rgb(100, 200, 100),
						null, null);
				LineAndPointFormatter series3Format = new LineAndPointFormatter(
						Color.rgb(200, 100, 100), Color.rgb(200, 100, 100),
						null, null);
				LineAndPointFormatter series1Format = new LineAndPointFormatter(
						Color.rgb(100, 100, 200), // Color de la línea
						Color.rgb(100, 100, 200), // Color del punto
						null, null); // Relleno
				mySimleXYPlot.setRangeBoundaries(-10, 20, BoundaryMode.FIXED);
				mySimleXYPlot.setDomainBoundaries(0, 20, BoundaryMode.FIXED);
				mySimleXYPlot.setDomainStepValue(5);
				mySimleXYPlot.setTicksPerRangeLabel(3);

				/*
				 * XYSeries series1 = new SimpleXYSeries("Acelerómetro");
				 * series1(Arrays.asList(valoresX),
				 * SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
				 */
				//mySimleXYPlot.clear();
				mySimleXYPlot.addSeries(series1, series1Format);
				mySimleXYPlot.addSeries(series2, series2Format);
				mySimleXYPlot.addSeries(series3, series3Format);
				mySimleXYPlot.redraw();
				break;
			}

		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}
