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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class Simulacion extends Activity implements SensorEventListener,
		OnClickListener, OnCheckedChangeListener {

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
	Button grafAcelerometro;
	Button grafGiroscopio;
	Button grafMagnetico;
	Button grafProximidad;
	Button grafLuz;
	CheckBox acelero;
	CheckBox giro;
	CheckBox magneto;
	CheckBox luz;
	CheckBox prox;
	boolean acce = false;
	boolean gi = false;
	boolean mag = false;
	boolean proxi = false;
	boolean lu = false;
	

	// Recogemos las preferencias
	int tiempoInicio, tiempoParada;
	int tipo;

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
		grafAcelerometro = (Button) findViewById(R.id.graficaAcelerometro);
		grafGiroscopio = (Button) findViewById(R.id.graficaGiroscopio);
		grafMagnetico = (Button) findViewById(R.id.graficaMagnetico);
		grafProximidad = (Button) findViewById(R.id.graficaProximidad);
		grafLuz = (Button) findViewById(R.id.graficaLuminosidad);

		// declarar los sensores como variables

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		giroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		magnetometro = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		luces = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		proximo = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		aceleromete = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		// Escucha de los botones
		acelerometer.setOnClickListener(this);
		giroscopio.setOnClickListener(this);
		magnetic.setOnClickListener(this);
		proximity.setOnClickListener(this);
		luminosity.setOnClickListener(this);
		grafAcelerometro.setOnClickListener(this);
		grafGiroscopio.setOnClickListener(this);
		grafMagnetico.setOnClickListener(this);
		grafProximidad.setOnClickListener(this);
		grafLuz.setOnClickListener(this);

		tipo = SensorManager.SENSOR_DELAY_NORMAL;
		Log.d("tiempo", "inicio: " + tipo);

		acelero = (CheckBox) findViewById(R.id.checkAcelerometro);
		giro = (CheckBox) findViewById(R.id.checkGiroscopio);
		magneto = (CheckBox) findViewById(R.id.checkMagetico);
		prox = (CheckBox) findViewById(R.id.checkProximidad);
		luz = (CheckBox) findViewById(R.id.checkLuz);
		

		acelero.setOnCheckedChangeListener(this);
		giro.setOnCheckedChangeListener(this);
		magneto.setOnCheckedChangeListener(this);
		prox.setOnCheckedChangeListener(this);
		luz.setOnCheckedChangeListener(this);
		

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

			Intent gravedad = new Intent(Simulacion.this,
					DefinicionGiroscopio.class);
			startActivity(gravedad);

			break;
		case R.id.magnetic:

			Intent magnetico = new Intent(Simulacion.this,
					DefinicionMagnetico.class);
			startActivity(magnetico);

			break;
		case R.id.luminosity:

			Intent luz = new Intent(Simulacion.this, DefinicionLuz.class);
			startActivity(luz);

			break;
		case R.id.proximity:

			Intent proximidad = new Intent(Simulacion.this,
					DefinicionProximidad.class);
			startActivity(proximidad);

			break;
		case R.id.graficaAcelerometro:
			Intent grafica = new Intent(Simulacion.this, Grafica.class);
			grafica.putExtra("tipo", tipo);
			grafica.putExtra("tiempo", tiempoParada);
			grafica.putExtra("temporizador", tiempoInicio);
			grafica.putExtra("sensor", Sensor.TYPE_ACCELEROMETER);
			grafica.putExtra("acelerometro", acce);
			grafica.putExtra("giroscopio", gi);
			grafica.putExtra("magnetometro", mag);
			grafica.putExtra("proximo", proxi);
			grafica.putExtra("luz", lu);
			startActivity(grafica);
			break;
		case R.id.graficaGiroscopio:
			Intent graficaGir = new Intent(Simulacion.this, Grafica.class);
			graficaGir.putExtra("tipo", tipo);
			graficaGir.putExtra("tiempo", tiempoParada);
			graficaGir.putExtra("temporizador", tiempoInicio);
			graficaGir.putExtra("sensor", Sensor.TYPE_GYROSCOPE);
			graficaGir.putExtra("acelerometro", acce);
			graficaGir.putExtra("giroscopio", gi);
			graficaGir.putExtra("magnetometro", mag);
			graficaGir.putExtra("proximo", proxi);
			graficaGir.putExtra("luz", lu);
			startActivity(graficaGir);
			break;
		case R.id.graficaLuminosidad:
			Intent graficaLu = new Intent(Simulacion.this, Grafica.class);
			graficaLu.putExtra("tipo", tipo);
			graficaLu.putExtra("tiempo", tiempoParada);
			graficaLu.putExtra("temporizador", tiempoInicio);
			graficaLu.putExtra("sensor", Sensor.TYPE_LIGHT);
			graficaLu.putExtra("acelerometro", acce);
			graficaLu.putExtra("giroscopio", gi);
			graficaLu.putExtra("magnetometro", mag);
			graficaLu.putExtra("proximo", proxi);
			graficaLu.putExtra("luz", lu);
			startActivity(graficaLu);
			break;
		case R.id.graficaMagnetico:
			Intent graficaMa = new Intent(Simulacion.this, Grafica.class);
			graficaMa.putExtra("tipo", tipo);
			graficaMa.putExtra("tiempo", tiempoParada);
			graficaMa.putExtra("temporizador", tiempoInicio);
			graficaMa.putExtra("sensor", Sensor.TYPE_MAGNETIC_FIELD);
			graficaMa.putExtra("acelerometro", acce);
			graficaMa.putExtra("giroscopio", gi);
			graficaMa.putExtra("magnetometro", mag);
			graficaMa.putExtra("proximo", proxi);
			graficaMa.putExtra("luz", lu);
			startActivity(graficaMa);
			break;
		case R.id.graficaProximidad:
			Intent graficaPr = new Intent(Simulacion.this, Grafica.class);
			graficaPr.putExtra("tipo", tipo);
			graficaPr.putExtra("tiempo", tiempoParada);
			graficaPr.putExtra("temporizador", tiempoInicio);
			graficaPr.putExtra("sensor", Sensor.TYPE_PROXIMITY);
			graficaPr.putExtra("acelerometro", acce);
			graficaPr.putExtra("giroscopio", gi);
			graficaPr.putExtra("magnetometro", mag);
			graficaPr.putExtra("proximo", proxi);
			graficaPr.putExtra("luz", lu);
			startActivity(graficaPr);
			break;
		}
	}

	// Iniciamos cada uno de los sensores que vamos a usar

	protected void Iniciar_sensores() {
		// iniciar acelerometro
		if (aceleromete == null) {
			acelerometro.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
			acelerometro.setTextColor(Color.RED);
			grafAcelerometro.setVisibility(Button.GONE);
			acelero.setVisibility(CheckBox.GONE);

		} else {
			mSensorManager.registerListener(this,
					mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					tipo);
		}
		// iniciar giroscopio
		if (giroscope == null) {
			giroscopo.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
			giroscopo.setTextColor(Color.RED);
			grafGiroscopio.setVisibility(Button.GONE);
			giro.setVisibility(CheckBox.GONE);
		} else {
			mSensorManager.registerListener(this,
					mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
					tipo);
		}
		// iniciar magnetometro
		if (magnetometro == null) {
			magnetico.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
			magnetico.setTextColor(Color.RED);
			grafMagnetico.setVisibility(Button.GONE);
			magneto.setVisibility(CheckBox.GONE);
		} else {
			mSensorManager
					.registerListener(this, mSensorManager
							.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), tipo);
		}
		// iniciar proximidad
		if (proximo == null) {
			proximidad.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
			proximidad.setTextColor(Color.RED);
			grafProximidad.setVisibility(Button.GONE);
			detecta.setVisibility(View.GONE);
			prox.setVisibility(CheckBox.GONE);
		} else {
			mSensorManager.registerListener(this,
					mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
					tipo);
		}
		// iniciar luces
		if (luces == null) {
			luminosidad.setText("\nNO ESTA DISPONIBLE EL SENSOR\n");
			luminosidad.setTextColor(Color.RED);
			grafLuz.setVisibility(Button.GONE);
			luz.setVisibility(CheckBox.GONE);
		} else {
			mSensorManager.registerListener(this,
					mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), tipo);
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

	// Acciones cuando los sensores van cambiando, en este caso los mostramos
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		String txt = "\nSensor: ";
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				double m = Double.valueOf(Math.abs(Math.sqrt(Math.pow(
						event.values[0], 2)
						+ Math.pow(event.values[1], 2)
						+ Math.pow(event.values[2], 2))));
				double x = Math.round(event.values[0]*10000.0)/10000.0;
				double y = Math.round(event.values[1]*10000.0)/10000.0;
				double z = Math.round(event.values[2]*10000.0)/10000.0;
				double modulo = Math.round(m*10000.0)/10000.0;
				
				txt += getString(R.string.acelerometro)+ " ("+getString(R.string.unidad_acelerometro)+")\n";
				txt += "\n X: " + x;
				txt += "\n Y: " + y;
				txt += "\n Z: " + z;
				txt += "\n a: " + modulo;
				acelerometro.setText(txt);
				break;

			case Sensor.TYPE_GYROSCOPE:
				double m2 = Double.valueOf(Math.abs(Math.sqrt(Math.pow(
						event.values[0], 2)
						+ Math.pow(event.values[1], 2)
						+ Math.pow(event.values[2], 2))));
				double x2 = Math.round(event.values[0]*10000.0)/10000.0;
				double y2 = Math.round(event.values[1]*10000.0)/10000.0;
				double z2 = Math.round(event.values[2]*10000.0)/10000.0;
				double modulo2 = Math.round(m2*10000.0)/10000.0;
				txt += getString(R.string.giroscopio)+ " ("+getString(R.string.unidad_giroscopio)+")\n";
				txt += "\n X: " + x2;
				txt += "\n Y: " + y2;
				txt += "\n Z: " + z2;
				txt += "\n ω: " + modulo2;
				giroscopo.setText(txt);
				break;

			case Sensor.TYPE_MAGNETIC_FIELD:
				double m3 = Double.valueOf(Math.abs(Math.sqrt(Math.pow(
						event.values[0], 2)
						+ Math.pow(event.values[1], 2)
						+ Math.pow(event.values[2], 2))));
				double x3 = Math.round(event.values[0]*10000.0)/10000.0;
				double y3 = Math.round(event.values[1]*10000.0)/10000.0;
				double z3 = Math.round(event.values[2]*10000.0)/10000.0;
				double modulo3 = Math.round(m3*10000.0)/10000.0;
				txt += getString(R.string.magnetico)+ " ("+getString(R.string.unidad_campo_magnetico)+")\n";
				txt += "\n X: " + x3;
				txt += "\n Y: " + y3;
				txt += "\n Z: " + z3;
				txt += "\n B: " + modulo3;
				magnetico.setText(txt);
				break;

			case Sensor.TYPE_PROXIMITY:
				double m4 = Double.valueOf(Math.abs(Math.sqrt(Math.pow(
						event.values[0], 2))));
				double x4 = Math.round(event.values[0]*10000.0)/10000.0;
				double modulo4 = Math.round(m4*10000.0)/10000.0;
				txt += getString(R.string.proximidad)+ " ("+getString(R.string.unidad_proximidad)+")\n";
				txt += "\n "+getString(R.string.distancia)+": " + x4;
				txt += "\n d: " + modulo4;
				proximidad.setText(txt);
				// Si detecta 0 lo represento
				if (event.values[0] == 0) {
					detecta.setBackgroundColor(Color.parseColor("#cf091c"));
					detecta.setText("Proximidad Detectada");
					detecta.setVisibility(TextView.VISIBLE);
				} else {
					detecta.setVisibility(TextView.INVISIBLE);
				}
				break;

			case Sensor.TYPE_LIGHT:
				double m5 = Double.valueOf(Math.abs(Math.sqrt(Math.pow(
						event.values[0], 2))));
				double x5 = Math.round(event.values[0]*10000.0)/10000.0;
				double modulo5 = Math.round(m5*10000.0)/10000.0;
				txt += getString(R.string.luminosidad)+ " ("+getString(R.string.unidad_luz)+")\n";
				txt += "\n "+getString(R.string.luminosi)+": " + x5;
				txt += "\n E: " + modulo5;
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

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Cargamos las opciones que vamos a usar en esta pantalla

		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menusimulacion, menu);
		return true;
		/** true -> el menú ya está visible */
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Elegimos entre las opciones disponibles en esta pantalla
		switch (item.getItemId()) {
		case R.id.configurate:
			Intent i = new Intent(this, Preferencias.class);
			// Iniciamos la actividad y esperamos respuesta con los datos
			startActivityForResult(i, 0);
			break;
		}
		return true;
		/** true -> consumimos el item, no se propaga */
	}

	// Con esta función recogemos la configuración de los diferentes campos.
	// Solo es llamada cuando hacemos cambios en la configuración
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		String type = pref.getString("frecuencia",
				"SensorManager.SENSOR_DELAY_NORMAL");
		if (type.equals("SensorManager.SENSOR_DELAY_NORMAL")) {
			tipo = SensorManager.SENSOR_DELAY_NORMAL;
		} else if (type.equals("SensorManager.SENSOR_DELAY_UI")) {
			tipo = SensorManager.SENSOR_DELAY_UI;
		}
		tiempoInicio = Integer.parseInt(pref.getString("temporizador", "0"));
		tiempoParada = Integer.parseInt(pref.getString("tiempo", "0"));

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.checkAcelerometro:
			acce = isChecked;
			break;
		case R.id.checkGiroscopio:
			gi = isChecked;
			break;
		case R.id.checkMagetico:
			mag = isChecked;
			break;
		case R.id.checkProximidad:
			proxi = isChecked;
			break;
		case R.id.checkLuz:
			lu = isChecked;
			break;
		}
	}
}
