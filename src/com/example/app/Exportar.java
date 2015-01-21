package com.example.app;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Intent;

public class Exportar{
	
	private Grafica activity;

	public Exportar(Grafica activity) {
		this.activity = activity;
	}

	protected void hacer(ConcurrentLinkedQueue<AccelData> sensorDatas) {
		// TODO Auto-generated method stub
		long t = sensorDatas.peek().getTimestamp();

		StringBuilder csvData = new StringBuilder();
		csvData.append("Tiempo, X, Y, Z \n");
		for (AccelData values : sensorDatas) {
			long f = (values.getTimestamp() - t) / 1000;
			double d = ((values.getTimestamp() - t) % 1000) * 0.001;
			double fin = f + d;
			csvData.append(String.valueOf(fin) + ", "
					+ String.valueOf(values.getX()) + ", "
					+ String.valueOf(values.getY()) + ", "
					+ String.valueOf(values.getZ()) + ", "
					+ String.valueOf(values.getModulo()) + "\n");
		}
		enviar(csvData.toString());
	}
	
	protected void enviar(String result) {
		// TODO Auto-generated method stub
		activity.setProgressBarVisibility(false);
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, result);
		sendIntent.setType("text/plain");
		activity.startActivity(sendIntent);
	}


}
