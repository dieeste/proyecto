package com.example.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PoIExplorer extends ListActivity {

	private List<String> listaNombresArchivos;
	private List<String> listaRutasArchivos;
	private ArrayAdapter<String> adaptador;
	private String directorioRaiz;
	private TextView carpetaActual;
	String root;
	ImageView imagen;
	
	/*public void listV(String name, int icono) {
        R.id.firstLine = name;
        R.id.icon=icono;
    }*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carga);
		carpetaActual = (TextView) findViewById(R.id.rutaActual);
		root= Environment.getExternalStorageDirectory().getPath();
		directorioRaiz = (Environment.getExternalStorageDirectory()
				.toString() + "/" + getResources().getString(R.string.app_name)).toString();
	//	imagen = (ImageView) findViewById(R.id.icon);
		verArchivosDirectorio(directorioRaiz);
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
			listaNombresArchivos.add("../           Ver otras carpetas de la memoria");
			listaRutasArchivos.add(directorioActual.getParent());
			x = 1;
		}
		
		// Almacenamos las rutas de todos los archivos y carpetas del directorio
		for (File archivo : listaArchivos) {
			listaRutasArchivos.add(archivo.getPath());
		}
			
		// Ordenamos la lista de archivos para que se muestren en orden alfabetico
		Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);
		
		
		// Recorredos la lista de archivos ordenada para crear la lista de los nombres
		// de los archivos que mostraremos en el listView
		for (int i = x; i < listaRutasArchivos.size(); i++){
			File archivo = new File(listaRutasArchivos.get(i));
			if (archivo.isFile()) {
				listaNombresArchivos.add(archivo.getName());
			//	listaNombresArchivos.add(R.drawable.file);
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
		
		// Si es un archivo se muestra un Toast con su nombre y si es un directorio
		// se cargan los archivos que contiene en el listView
		if (archivo.isFile()) {
			Intent vamos = new Intent(this, LeerCsv.class);
			vamos.putExtra("file", archivo.getPath());
			startActivity(vamos);
		/*	Toast.makeText(this,
					"Has seleccionado el archivo: " + archivo.getName(),
					Toast.LENGTH_LONG).show();*/
		} else {
			// Si no es un directorio mostramos todos los archivos que contiene
			verArchivosDirectorio(listaRutasArchivos.get(position));
		}

	}

}