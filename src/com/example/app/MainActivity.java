package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	// Declaramos los botones, los hacemos globales para ser usados mas adelante
	Button simulacion;
	Button teoria;
	Button listasensores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Recogemos los botones
		simulacion = (Button) findViewById(R.id.medicion);
		teoria = (Button) findViewById(R.id.teoria);
		listasensores = (Button) findViewById(R.id.sensoresdisponibles);

		// Escuchamos los botones
		simulacion.setOnClickListener(this);
		teoria.setOnClickListener(this);
		listasensores.setOnClickListener(this);
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
			startActivity(simulacion);
			break;
		case R.id.teoria:
			Intent teo = new Intent(this, Teoria.class);
			startActivity(teo);
			break;
		case R.id.sensoresdisponibles:
			Intent sensoresdisponibles = new Intent(this, Listasensores.class);
			startActivity(sensoresdisponibles);
			break;
		default:
			break;
		}
	}
}
