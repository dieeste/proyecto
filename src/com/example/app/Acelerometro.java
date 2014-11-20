package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
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

public class Acelerometro extends Activity implements SensorEventListener, OnItemSelectedListener{
	
	String velocidad;
	Spinner spinner;
	ToggleButton toggleGps;
	ToggleButton toggleTiempo;
	TextView tiempo;
	EditText etTiempo;
	Button grafica;
	TextView prueba;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acelerometro);
		
		spinner = (Spinner) findViewById(R.id.spinnerfrecuencia);
		toggleGps = (ToggleButton) findViewById(R.id.toggleGps);
		toggleTiempo = (ToggleButton) findViewById(R.id.toggleTiempo);
		tiempo = (TextView) findViewById(R.id.tiempos);
		etTiempo = (EditText) findViewById(R.id.etTiempo);
		grafica =(Button) findViewById(R.id.grafica);
		prueba = (TextView) findViewById(R.id.prueba);
		
		//Declaramos el botón de la gráfica
		grafica.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View boton) {
				// TODO Auto-generated method stub
				Intent grafica = new Intent(Acelerometro.this, Grafica.class);
				startActivity(grafica);
			}
		});
		
		//Declaración del spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.frecuencia_recogida, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		//Declaramos el toggle del tiempo y elegimos la acción que realizará al encender o apagar el toggle
		toggleTiempo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked){
					tiempo.setVisibility(View.VISIBLE);
					etTiempo.setVisibility(View.VISIBLE);
				} else {
					tiempo.setVisibility(View.INVISIBLE);
					etTiempo.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		String txt="";
		parent.getItemAtPosition(pos);
		if (pos==0){
			prueba.setText(txt+"has elegido normal");
		}
		}
		

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
