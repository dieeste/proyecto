package com.example.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.csvreader.CsvReader;

public class LeerCsv extends Activity {
	static Graph mGraph;
	Bundle graficas = getIntent().getExtras();
	String nombre= graficas.getString("fichero");
	LinearLayout layout;
	GraphicalView view;
	ConcurrentLinkedQueue<AccelData> datos = new ConcurrentLinkedQueue<AccelData>();
	
	public void main(String args) {
		Log.d("hola", "entra");
		
		try {
			
			CsvReader fichero = new CsvReader(args);
		
			fichero.readHeaders();

			while (fichero.readRecord())
			{
				long tiempo = Long.parseLong(fichero.get("Tiempo"));
				double x = Double.parseDouble(fichero.get("X"));
				double y = Double.parseDouble(fichero.get("Y"));
				double z = Double.parseDouble(fichero.get("Z"));
				AccelData data = new AccelData(tiempo, x, y, z);
				chart(data);
				datos.add(data);
			}
			
			fichero.close();
			mGraph = new Graph(this);
			mGraph.initData(datos);
			view = mGraph.getGraph();
			layout.addView(view);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void chart(AccelData data){
		
	}
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
		
	
	



	