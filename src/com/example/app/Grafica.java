package com.example.app;

import android.app.Activity;
import android.os.Bundle;

import com.androidplot.xy.XYPlot;

public class Grafica extends Activity{
	
	 private XYPlot plot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grafica);
	}

}
