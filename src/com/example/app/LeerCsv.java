package com.example.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.csvreader.CsvReader;

public class LeerCsv extends Activity {
	static Graph mGraph;
	LinearLayout layout;
	GraphicalView view;
	static ConcurrentLinkedQueue<AccelData> datos;
	String nombre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Bundle graficas = getIntent().getExtras();
		nombre = graficas.getString("file");
		Log.d("este es el archivo", "eso es: " + nombre);
		setContentView(R.layout.graficaarchivo);
		layout = (LinearLayout) findViewById(R.id.chart);
		datos= new ConcurrentLinkedQueue<AccelData>();
		lee(nombre);
	}

	public void lee(String file) {
		try {

			CsvReader fichero = new CsvReader(file);

			fichero.readHeaders();

			while (fichero.readRecord()) {

				double tiempo = Double.parseDouble(fichero.get("Tiempo"));
				double x = Double.parseDouble(fichero.get("X"));
				double y = Double.parseDouble(fichero.get("Y"));
				double z = Double.parseDouble(fichero.get("Z"));
				Log.d("zzzzzz", "esto es: " + z);
				double modulo = Double.parseDouble(fichero.get("Modulo"));

				AccelData data = new AccelData(tiempo, x, y, z, modulo);

				datos.add(data);
				
			}
			fichero.close();
			
			mGraph = new Graph(this);
			mGraph.iniciar(datos);
			mGraph.propiedades();
			view = mGraph.getGraph();
			layout.addView(view);
			for (AccelData data : datos) {
				datos.remove(data);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}