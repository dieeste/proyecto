package com.example.app;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Intent;
import android.os.AsyncTask;

public class Exportar extends AsyncTask<ConcurrentLinkedQueue<float[]>, Integer, String>{
	
	private Grafica activity;

	public Exportar(Grafica activity) {
		this.activity = activity;
	}

	@Override
	protected String doInBackground(ConcurrentLinkedQueue<float[]>... params) {
		// TODO Auto-generated method stub
		StringBuilder datos = new StringBuilder();
		Iterator<float[]> iterator = params[0].iterator();
		while (iterator.hasNext()) {
			float[] values = iterator.next();
			for (int i = 0; i< 3; i++) {
				datos.append(String.valueOf(values[i]));
				if (i < 3) {
					datos.append(",");
				}
			}
			datos.append("\n");
		}
		return datos.toString();
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		activity.setProgressBarVisibility(false);
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, result);
		sendIntent.setType("text/plain");
		activity.startActivity(sendIntent);
		super.onPostExecute(result);
	}

}
