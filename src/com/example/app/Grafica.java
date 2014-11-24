package com.example.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

public class Grafica extends Activity implements SensorEventListener{
	
	 @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Parar_sensores();
	}


	private XYPlot plot;
	 SensorManager mSensorManager;
	 int tipo;
	 Button parar;
	 ArrayList <Double> Vector = new ArrayList<Double>();
	 
	double DatoX, DatoY;
	 
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.grafica);
		plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Bundle graficas = getIntent().getExtras();
		tipo = graficas.getInt("tipo");
		plot.setDomainBoundaries(0, 10, BoundaryMode.FIXED);
		plot.setRangeBoundaries(-10, 20, BoundaryMode.FIXED);
		plot.setDomainStepValue(5);
		parar = (Button) findViewById(R.id.parar);
		
		Iniciar_sensores();
		
		
		
		
		parar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onStop();
			}
		});
	}
	protected void Iniciar_sensores(){
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),tipo);
		
	}
	
	
	
	
	
	
	protected void Parar_sensores(){
		mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
	}
	
	
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
	
		synchronized (this) {
			switch (event.sensor.getType()){
			case Sensor.TYPE_ACCELEROMETER:
				
				for (int i=0;i<=30; i++){
					//DatoX = DatoX + 0.5;
					//Vector.add(DatoX);
					DatoY = event.values[0];
					Vector.add(DatoY);
					
				}
			Timer timer =new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {	
					@Override
					public void run() {
						// TODO Auto-generated method stub
						XYSeries serie1 = new SimpleXYSeries(Vector, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "X");
						LineAndPointFormatter serie1Format = new LineAndPointFormatter(Color.rgb(100, 100, 200),Color.rgb(100, 100, 200),null,null);
						plot.addSeries(serie1, serie1Format);
					
						
						plot.redraw();
					}
				}, 0, 150);	
				//Number[] valoresX = {event.values[0]};
		
		break;
			}
		}
		
	}
	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
