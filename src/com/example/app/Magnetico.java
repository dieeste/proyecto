package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Magnetico extends Activity implements 
		OnItemSelectedListener, LocationListener {

	String velocidad;
	Spinner spinner;
	ToggleButton toggleGps;
	ToggleButton toggleTiempo;
	TextView tiempo;
	EditText etTiempo;
	Button grafica;
	TextView gps;
	LocationManager controladorgps;
	String proveedor;
	Button infomagnetico;
	int tipo;
	int tiempoParada;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.magnetico);

		spinner = (Spinner) findViewById(R.id.spinnerfrecuencia);
		toggleGps = (ToggleButton) findViewById(R.id.toggleGps);
		toggleTiempo = (ToggleButton) findViewById(R.id.toggleTiempo);
		tiempo = (TextView) findViewById(R.id.tiempos);
		etTiempo = (EditText) findViewById(R.id.etTiempo);
		grafica = (Button) findViewById(R.id.grafica);
		gps = (TextView) findViewById(R.id.gps);
		controladorgps = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		proveedor = controladorgps.getBestProvider(criteria, true);
		infomagnetico= (Button) findViewById(R.id.infomagnetico);
		
		/*/Recogemos el tiempo
		if (etTiempo.getText() != null){
		tiempoParada = Integer.parseInt(etTiempo.getText().toString());
		Log.d("tiempo", "tiempoParada");
		}*/ 

		// Declaración del gps
		
		 Location localizacion = controladorgps.getLastKnownLocation(proveedor);
		 muestraLocalizacion(localizacion);
		 
		controladorgps.isProviderEnabled(proveedor);

		// Declaramos el botón de la gráfica
		grafica.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View boton) {
				// TODO Auto-generated method stub
				Intent graficas = new Intent(Magnetico.this, Grafica.class);
				graficas.putExtra("tipo", tipo);
				graficas.putExtra("tiempo", tiempoParada);
				startActivity(graficas);
			}
		});

		//Declaración del botón informacion
		infomagnetico.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View boton) {
				// TODO Auto-generated method stub
				Intent info = new Intent(Magnetico.this, DefinicionMagnetico.class);
				startActivity(info);
			}
		});
		
		// Declaración del spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.frecuencia_recogida,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		toggleGps.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {

					gps.setVisibility(View.VISIBLE);
					onResume();
				} else {
					gps.setVisibility(View.INVISIBLE);
					onPause();
				}
			}
		});
		// Declaramos el toggle del tiempo y elegimos la acción que realizará al
		// encender o apagar el toggle
		toggleTiempo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {

					tiempo.setVisibility(View.VISIBLE);
					etTiempo.setVisibility(View.VISIBLE);
				} else {
					tiempo.setVisibility(View.INVISIBLE);
					etTiempo.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void muestraLocalizacion(Location localizacion) {
		// TODO Auto-generated method stub
		if (localizacion == null)
			gps.setText("Localización desconocida");
		else
			gps.setText(localizacion.toString());
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		parent.getItemAtPosition(pos);

		if (pos == 0) {
			tipo = SensorManager.SENSOR_DELAY_NORMAL;
		}

		if (pos == 1) {
			tipo = SensorManager.SENSOR_DELAY_UI;
		}
		if (pos == 2) {
			tipo = SensorManager.SENSOR_DELAY_GAME;
		}
		if (pos == 3) {
			tipo = SensorManager.SENSOR_DELAY_FASTEST;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		muestraLocalizacion(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
	
	 @Override protected void onResume() { // TODO Auto-generated method stub
	 super.onResume(); 
	 controladorgps.requestLocationUpdates(proveedor, 10000,
	 1, this);
	 
	 }
	 
	 @Override protected void onPause() { // TODO Auto-generated method stub
	 super.onPause(); 
	 controladorgps.removeUpdates(this); }
	
}
