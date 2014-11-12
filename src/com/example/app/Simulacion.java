package com.example.app;
import android.app.Activity;
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


public class Simulacion extends Activity implements SensorEventListener{

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
	TextView cuenta;
	TextView mostrar;
	Button empezar;
	Button parar;
	EditText contador;
	Chronometer cronometro;
	int tempoAutodestrucion; 
	
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
		cuenta = (TextView) findViewById(R.id.cuenta);
		empezar = (Button) findViewById(R.id.empezar);
		parar = (Button) findViewById(R.id.parar);
		contador = (EditText) findViewById(R.id.contador);
		mostrar = (TextView) findViewById(R.id.mostrar);
		cronometro = (Chronometer) findViewById(R.id.cronometro);
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	
		
		cronometro.setOnChronometerTickListener(new OnChronometerTickListener() {
			
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				// TODO Auto-generated method stub

                long tempoPasado = SystemClock.elapsedRealtime() - chronometer.getBase();
                int tempoSeg = (int) tempoPasado / 1000;
                if (tempoSeg == tempoAutodestrucion){
                        //finish();
                	cronometro.stop();
                	onStop();
			}
                mostrar.setText("Autodestrucción en: " + (tempoAutodestrucion - tempoSeg) + " seg");
			}
		});
		empezar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//String resultado = "";
			//	int cont = Integer.parseInt(contador.getText().toString());
				tempoAutodestrucion = Integer.parseInt(contador.getText().toString());
				//String ini = "";	
				// TODO Auto-generated method stub
				cronometro.setBase(SystemClock.elapsedRealtime());
                cronometro.start();
                Log.i("cronometro","vemos como cambia: " + String.valueOf(SystemClock.elapsedRealtime()));
              //  Log.i("tiempo","time now: " + String.valueOf(cronometro.getBase()));
               // Log.i("tiempo","time now3: " + String.valueOf(SystemClock.elapsedRealtime()/1000));
				//if (SystemClock.elapsedRealtime()/1000>conta*10000){
           /*     if (currentThreadTimeMillis ()==conta*1000){
				 //resultado += String.valueOf(i);
				 cronometro.stop();
				 onStop();
				//onRestart();
					}	*/
				//mostrar.setText(resultado+"\n");*/
				
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
	
	
	}

	
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

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
				SensorManager.SENSOR_DELAY_NORMAL);
	}
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
			case Sensor.TYPE_GRAVITY:
				txt += "Gravedad\n";
				txt += "\n x: " + event.values[0];
				txt += "\n y: " + event.values[1];
				txt += "\n z: " + event.values[2];

				gravedad.setText(txt);

				break;

			case Sensor.TYPE_MAGNETIC_FIELD:
				txt += "magnetic field\n";
				txt += "\n" + event.values[0] + " uT";

				magnetico.setText(txt);

				break;
			}

		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
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
}
