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
	//Declaramos los botones, los hacemos globales para ser usados mas adelante
	Button test;
	Button ejemplo;
	Button teoria;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Recogemos los botones 
		test = (Button) findViewById(R.id.test);
		ejemplo = (Button) findViewById(R.id.ejemplos);
		teoria = (Button) findViewById(R.id.teoria);
		
		//Escuchamos los botones
		test.setOnClickListener(this);
		ejemplo.setOnClickListener(this);
		teoria.setOnClickListener(this);
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

	/*public void lanzarAcercaDe(View view) {

		Intent i = new Intent(this, AcercaDe.class);

		startActivity(i);
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*
		 * int id = item.getItemId(); if (id == R.id.action_settings) { return
		 * true; } return super.onOptionsItemSelected(item); }
		 */
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
		case R.id.test:
			//envía lo que nosotros queremos y nos deja elegir entre las aplicaciones que tenemos para enviar el correo
			Intent test = new Intent(Intent.ACTION_SEND); 
			test.setType("text/plain");
			startActivity(test);
			break;
		case R.id.ejemplos:
			break;
		case R.id.teoria:
			break;

		default:
			break;
		}
	}
}
