package com.sensor.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	// Declaramos los botones, los hacemos globales para ser usados mas adelante
	Button simulacion;
	Button cargargraficas;
	SharedPreferences sharedPreference;
	// Recogemos las preferencias
	int tiempoInicio, tiempoParada;
	int tipo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Recogemos los botones
		simulacion = (Button) findViewById(R.id.medicion);
		cargargraficas = (Button) findViewById(R.id.cargargrafica);

		// Escuchamos los botones
		simulacion.setOnClickListener(this);
		cargargraficas.setOnClickListener(this);
		tipo = SensorManager.SENSOR_DELAY_NORMAL;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
		/** true -> el menú ya está visible */
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.acercaDe:
			Intent i = new Intent(this, AcercaDe.class);
			startActivity(i);
			break;
		case R.id.menu_settings:
			Intent i2 = new Intent(this, Setting.class);
			startActivityForResult(i2, 0);
			break;
		case R.id.sensoresdisponibles:
			Intent sensoresdisponibles = new Intent(this, Listasensores.class);
			startActivity(sensoresdisponibles);
			break;
		case R.id.menu_ayuda:
			Intent ayuda = new Intent(this, Ayuda.class);
			final String[] TITLES = { getString(R.string.inicio),
					getString(R.string.medicion), getString(R.string.grafica),
					getString(R.string.cargargraficas),
					getString(R.string.teoria) };
			ayuda.putExtra("TITLES", TITLES);
			startActivity(ayuda);
		}
		return true;
		/** true -> consumimos el item, no se propaga */
	}

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		switch (boton.getId()) {
		case R.id.medicion:
			// envía lo que nosotros queremos y nos deja elegir entre las
			// aplicaciones que tenemos para enviar el correo
			Intent simulacion = new Intent(this, Simulacion.class);
			simulacion.putExtra("tipo", tipo);
			simulacion.putExtra("tiempo", tiempoParada);
			simulacion.putExtra("temporizador", tiempoInicio);
			startActivity(simulacion);
			break;
		case R.id.cargargrafica:
			Intent intent = new Intent(this, FileChooserActivity.class);
			this.startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		/*
		 * Resources resource = getResources(); Configuration config =
		 * resource.getConfiguration(); sharedPreference =
		 * PreferenceManager.getDefaultSharedPreferences(this); if
		 * ("fr".equalsIgnoreCase(sharedPreference.getString("language", null)))
		 * { config.locale = Locale.FRANCE; } else if
		 * ("en".equalsIgnoreCase(sharedPreference.getString("language", null)))
		 * { config.locale = Locale.ENGLISH; } else { config.locale =
		 * Locale.getDefault(); }
		 * getBaseContext().getResources().updateConfiguration(config, null);
		 */

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String type = pref.getString("frecuencia", "lento");
		if (type.equals("lento")) {
			tipo = SensorManager.SENSOR_DELAY_NORMAL;
		} else if (type.equals("normal")) {
			tipo = SensorManager.SENSOR_DELAY_UI;
		} else if (type.equals("rapido")) {
			tipo = SensorManager.SENSOR_DELAY_GAME;
		} else if (type.equals("muyrapido")) {
			tipo = SensorManager.SENSOR_DELAY_FASTEST;
		}
		String ti = pref.getString("temporizador", "0");
		if (ti.equals("")) {
			ti = "0";
			int tiempoi = Integer.parseInt(ti);
			tiempoInicio = tiempoi * 1000;
		} else {
			int tiempoi = Integer.parseInt(ti);
			tiempoInicio = tiempoi * 1000;
		}
		String tp = pref.getString("tiempo", "0");
		if (tp.equals("")) {
			tp = "0";
			int tiempop = Integer.parseInt(tp);
			tiempoParada = tiempop * 1000;
		} else {
			int tiempop = Integer.parseInt(tp);
			tiempoParada = tiempop * 1000;
		}

	}
}
