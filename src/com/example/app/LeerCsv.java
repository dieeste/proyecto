package com.example.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class LeerCsv extends Activity {
	/*
		private ArrayList<ArrayList<String>> arrListData;
		private File archivo;
		private FileReader fr;
		private BufferedReader br;
		static Graph mGraph;
		String nombre;
		LinearLayout layout;
		GraphicalView view;
		ConcurrentLinkedQueue<AccelData> datos;
		
		public LeerCsv(){}
		
		
		
		public ArrayList<ArrayList<String>> cargarArchivo(String file){
			
			try {
				// Apertura del fichero y creacion de BufferedReader para poder
				// hacer una lectura comoda (disponer del metodo readLine()).
				Bundle graficas = getIntent().getExtras();
				nombre = graficas.getString("fichero");
				archivo = new File (nombre);
				fr = new FileReader (archivo);
				br = new BufferedReader(fr);
				setContentView(R.layout.grafica);
				layout = (LinearLayout) findViewById(R.id.chart);
				
				datos = new ConcurrentLinkedQueue<AccelData>();
				
				arrListData = new ArrayList<> ();

				// Lectura del fichero
				String linea;
				
				while((linea = br.readLine())!=null){
					
					StringTokenizer strTok = new StringTokenizer(linea, ",");
					
	
					
					// use comma as separator
				
					long tiempo =
					double x = Double.parseDouble(country[1]);
					double y = Double.parseDouble(country[2]);
					double z = Double.parseDouble(country[3]);
					AccelData data = new AccelData(tiempo, x, y, z);
					datos.add(data);
					mGraph = new Graph(this);
					mGraph.initData(datos);
					mGraph.setProperties2();
					view = mGraph.getGraph();
					layout.addView(view);
					
					while (strTok.hasMoreTokens()) {
						datos.get( datos.size()-1 ).add( strTok.nextToken() );
					}
					
				}
			
			} catch(Exception e){
				e.printStackTrace();
				arrListData = null;
			} finally{
				// En el finally cerramos el fichero, para asegurarnos
				// que se cierra tanto si todo va bien como si salta 
				// una excepcion.
				try{                    
					if( null != fr ){   
						fr.close();     
					}
					
					return arrListData;
					
				} catch (Exception e2){ 
					e2.printStackTrace();
					return arrListData;
				}
			}
		} // cierre de metodo
		
		*/
}
		
	
	



	