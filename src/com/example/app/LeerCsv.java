package com.example.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class LeerCsv extends Activity {

	static Graph mGraph;
	String nombre;
	LinearLayout layout;
	GraphicalView view;
	ConcurrentLinkedQueue<AccelData> datos;

	public void main(String[] args) {

		LeerCsv obj = new LeerCsv();
		obj.run();

	}

	public void run() {
		setContentView(R.layout.grafica);
		layout = (LinearLayout) findViewById(R.id.chart);
		Bundle graficas = getIntent().getExtras();
		nombre = graficas.getString("fichero");
		datos = new ConcurrentLinkedQueue<AccelData>();
		Log.d("fichero run", "el fichero es: " + nombre);
		String csvFile = nombre;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] country = line.split(cvsSplitBy);
				long tiempo = Long.parseLong(country[0]);
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

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
