package com.example.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.csvreader.CsvReader;

public class LeerCsv extends Activity {
	static Graph mGraph;
	LinearLayout layout;
	GraphicalView view;
	ConcurrentLinkedQueue<AccelData> datos = new ConcurrentLinkedQueue<AccelData>();;
	ConcurrentLinkedQueue<AccelData2> sensor = new ConcurrentLinkedQueue<AccelData2>();
	String nombre;
	String unidad;
	String titulo;
	String tamano;
	String calidad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Bundle graficas = getIntent().getExtras();
		nombre = graficas.getString("file");
		setContentView(R.layout.graficaarchivo);
		layout = (LinearLayout) findViewById(R.id.chart);
	
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		float scale = getApplicationContext().getResources().getDisplayMetrics().density;
		if ((getResources().getConfiguration().screenLayout & 
			    Configuration.SCREENLAYOUT_SIZE_MASK) == 
			        Configuration.SCREENLAYOUT_SIZE_LARGE) {
		 tamano = "grande";
			}
		if ((getResources().getConfiguration().screenLayout & 
			    Configuration.SCREENLAYOUT_SIZE_MASK) == 
			        Configuration.SCREENLAYOUT_SIZE_NORMAL) {
		 tamano = "normal";
			}
		if ((getResources().getConfiguration().screenLayout & 
			    Configuration.SCREENLAYOUT_SIZE_MASK) == 
			        Configuration.SCREENLAYOUT_SIZE_SMALL) {
		 tamano = "pequena";
			}
		if ((getResources().getConfiguration().screenLayout & 
			    Configuration.SCREENLAYOUT_SIZE_MASK) == 
			        Configuration.SCREENLAYOUT_SIZE_XLARGE) {
		 tamano = "extra";
			}
		DisplayMetrics metrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(metrics);
    	int dips = 40;
    	// ENCONTRAR LOS PIXELES POR UN VALOR A DPI
    	float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, metrics);
    	
    	// TRATAR DE ENCONTRAR EL MISMO RESULTADO
    	float pixelBoton = 0;
    	float scaleDensity = 0;
    	
    	switch(metrics.densityDpi)
    	{
    	case DisplayMetrics.DENSITY_XXXHIGH:
    		scaleDensity = scale * 640;
    		pixelBoton = dips * (scaleDensity / 640);
    		calidad = "xxxhigh";
    		break;
    	case DisplayMetrics.DENSITY_XXHIGH:
    		scaleDensity = scale * 480;
    		pixelBoton = dips * (scaleDensity / 480);
    		calidad = "xxhigh";
    		break;
    	case DisplayMetrics.DENSITY_XHIGH:
    		scaleDensity = scale * 320;
    		pixelBoton = dips * (scaleDensity / 320);
    		calidad = "xhigh";
    		break;
    	case DisplayMetrics.DENSITY_HIGH: //HDPI
    		scaleDensity = scale * 240;
    		pixelBoton = dips * (scaleDensity / 240);
    		calidad = "alta";
    		break;
    	case DisplayMetrics.DENSITY_MEDIUM: //MDPI
    		scaleDensity = scale * 160;
    		pixelBoton = dips * (scaleDensity / 160);
    		calidad = "media";
    		break;
    		
    	case DisplayMetrics.DENSITY_LOW:  //LDPI
    		scaleDensity = scale * 120;
    		pixelBoton = dips * (scaleDensity / 120);
    		calidad = "baja";
    		break;
    	}
    	lee(nombre);
	
	}

	public void lee(String file) {
		boolean check[] = {true,true,true,true};
		int numerolineas = 0;
		for (AccelData2 data : sensor) {
			datos.remove(data);
		}
		for (AccelData data : datos) {
			datos.remove(data);
		}
		try {

			CsvReader fichero = new CsvReader(file);
			fichero.setDelimiter(';');
			fichero.getHeaders();
			fichero.skipLine();
			fichero.readHeaders();

			while (fichero.readRecord()) {
				numerolineas = fichero.getColumnCount();
				Log.d("numeor", "columnas asdfadsfa: " + numerolineas);
				if (numerolineas == 6) {
					double tiempo = Double.parseDouble(fichero.get("Tiempo"));
					double x = Double.parseDouble(fichero.get("X"));
					double y = Double.parseDouble(fichero.get("Y"));
					double z = Double.parseDouble(fichero.get("Z"));
					double modulo = Double.parseDouble(fichero.get("Modulo"));
					unidad = fichero.getHeader(6);
					
					//unidad = fichero.get("Unidad sensor");
					Log.d("asf","unidad "+unidad);

					AccelData data = new AccelData(tiempo, x, y, z, modulo);

					datos.add(data);
				} else if (numerolineas == 4) {
					double tiempo = Double.parseDouble(fichero.get("Tiempo"));
					double x = Double.parseDouble(fichero.get("X"));
					double modulo = Double.parseDouble(fichero.get("Modulo"));
					unidad = fichero.get("Unidad sensor");

					AccelData2 data = new AccelData2(tiempo, x, modulo);

					sensor.add(data);
				}
			}
			fichero.close();
			if (numerolineas == 6) {
				if (unidad.equalsIgnoreCase("m/sÂ²")) {
					titulo = "Aceleración "
							+ getResources().getString(
									R.string.unidad_acelerometro);
				} else if (unidad.equalsIgnoreCase(getResources().getString(
						R.string.unidad_giroscopio))) {
					titulo = "Velocidad angular "
							+ getResources().getString(
									R.string.unidad_giroscopio);
				} else if (unidad.equalsIgnoreCase("ÂµT")) {
					titulo = "Inducción magnética "
							+ getResources().getString(
									R.string.unidad_campo_magnetico);
				}
				mGraph = new Graph(this);
				mGraph.iniciar(datos);
				mGraph.ejeY(datos);
				mGraph.setProperties(check, unidad, calidad, tamano);
				view = mGraph.getGraph();
				layout.addView(view);
				for (AccelData data : datos) {
					datos.remove(data);
				}
			} else if (numerolineas == 4) {
				if (unidad.equalsIgnoreCase(getResources().getString(
						R.string.unidad_luz))) {
					titulo = "Iluminancia "
							+ getResources().getString(
									R.string.unidad_luz);
				} else if (unidad.equalsIgnoreCase(getResources().getString(
						R.string.unidad_proximidad))) {
					titulo = "Distancia "
							+ getResources().getString(
									R.string.unidad_proximidad);
				}
				mGraph = new Graph(this);
				mGraph.iniciar2(sensor);
				mGraph.ejeY2(sensor);
				mGraph.setProperties2(check, titulo);
				view = mGraph.getGraph();
				layout.addView(view);
				for (AccelData2 data : sensor) {
					datos.remove(data);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
