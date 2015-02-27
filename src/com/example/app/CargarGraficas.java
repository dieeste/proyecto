package com.example.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CargarGraficas extends ListActivity {

	private List<String> listaNombresArchivos;
	private List<String> listaRutasArchivos;
	private ArrayAdapter<String> adaptador;
	private String directorioRaiz;
	private TextView carpetaActual;
	ArrayList<Uri> ficheros = new ArrayList<Uri>();
	String root;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carga);
		carpetaActual = (TextView) findViewById(R.id.rutaActual);
		root = Environment.getExternalStorageDirectory().getPath();
		directorioRaiz = (Environment.getExternalStorageDirectory().toString()
				+ "/" + getResources().getString(R.string.app_name)).toString();

		File dir = new File(directorioRaiz);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		verArchivosDirectorio(directorioRaiz);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						boolean borrado = false;
						File archivo = new File(listaRutasArchivos
								.get(position));
						if (archivo.isFile()) {
							archivo.getPath();
							Uri path = Uri.fromFile(archivo);
							if (ficheros.isEmpty()) {
								Log.d("hola", "este es lo coge" + path);
								ficheros.add(path);
								view.setBackgroundColor(Color.GRAY);
							} else {
								for (int i = 0; i < ficheros.size(); i++) {
									if (ficheros.get(i).equals(path)) {
										Log.d("hola", "este lo quita" + path);
										ficheros.remove(i);
										view.setBackgroundColor(0);
										Log.d("hola", "este tama iff  "
												+ ficheros.size());
										borrado = true;
									}
								}
								if (borrado == false) {
									for (int i = 0; i < ficheros.size(); i++) {
										if (!ficheros.get(i).equals(path)) {
											Log.d("hola", "este lo mete" + path);
											ficheros.add(path);
											view.setBackgroundColor(Color.GRAY);
											Log.d("hola", "este tama else  "
													+ ficheros.size());
											break;
										}
									}
								}
							}
						}
						return true;
					}

				});
	}

	/**
	 * Muestra los archivos del directorio pasado como parametro en un listView
	 * 
	 * @param rutaDirectorio
	 */
	private void verArchivosDirectorio(String rutaDirectorio) {
		carpetaActual.setText("Estas en: " + rutaDirectorio);
		listaNombresArchivos = new ArrayList<String>();
		listaRutasArchivos = new ArrayList<String>();
		File directorioActual = new File(rutaDirectorio);
		File[] listaArchivos = directorioActual.listFiles();

		int x = 0;

		// Si no es nuestro directorio raiz creamos un elemento que nos
		// permita volver al directorio padre del directorio actual
		if (!rutaDirectorio.equals(root)) {
			listaNombresArchivos
					.add("../           Ver otras carpetas de la memoria");
			listaRutasArchivos.add(directorioActual.getParent());
			x = 1;
		}

		// Almacenamos las rutas de todos los archivos y carpetas del directorio
		for (File archivo : listaArchivos) {
			listaRutasArchivos.add(archivo.getPath());
		}

		// Ordenamos la lista de archivos para que se muestren en orden
		// alfabetico
		Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);

		// Recorredos la lista de archivos ordenada para crear la lista de los
		// nombres
		// de los archivos que mostraremos en el listView
		for (int i = x; i < listaRutasArchivos.size(); i++) {
			File archivo = new File(listaRutasArchivos.get(i));
			if (archivo.isFile()) {
				listaNombresArchivos.add(archivo.getName());
			} else {
				listaNombresArchivos.add("/" + archivo.getName());
			}
		}

		// Si no hay ningun archivo en el directorio lo indicamos
		if (listaArchivos.length < 1) {
			listaNombresArchivos.add("No hay ningun archivo");
			listaRutasArchivos.add(rutaDirectorio);
		}

		// Creamos el adaptador y le asignamos la lista de los nombres de los
		// archivos y el layout para los elementos de la lista
		adaptador = new ArrayAdapter<String>(this,
				R.layout.text_view_lista_archivos, listaNombresArchivos);
		setListAdapter(adaptador);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// Obtenemos la ruta del archivo en el que hemos hecho click en el
		// listView
		File archivo = new File(listaRutasArchivos.get(position));

		// Si es un archivo se muestra un Toast con su nombre y si es un
		// directorio
		// se cargan los archivos que contiene en el listView
		if (archivo.isFile()) {
			Intent vamos = new Intent(this, LeerCsv.class);
			vamos.putExtra("file", archivo.getPath());
			startActivity(vamos);
		} else {
			// Si no es un directorio mostramos todos los archivos que contiene
			verArchivosDirectorio(listaRutasArchivos.get(position));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Cargamos las opciones que vamos a usar en esta pantalla
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menucargar, menu);
		return true;
		/** true -> el menú ya está visible */
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (ficheros.size() > 0) {
			MenuItem item = menu.findItem(R.id.enviar);
			item.setEnabled(true);
		} else {
			MenuItem item = menu.findItem(R.id.enviar);
			item.setEnabled(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Elegimos entre las opciones disponibles en esta pantalla
		switch (item.getItemId()) {
		case (R.id.enviar):
			// enviar(();
			enviar(ficheros);
			ficheros.remove(true);
			break;
		}
		return true;
		/** true -> consumimos el item, no se propaga */
	}

	protected void enviar(ArrayList<Uri> ficheros) {
		// TODO Auto-generated method stub
		this.setProgressBarVisibility(false);
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
		sendIntent.setType("file/*");
		sendIntent.putExtra(Intent.EXTRA_STREAM, ficheros);
		startActivity(sendIntent);
	}

}