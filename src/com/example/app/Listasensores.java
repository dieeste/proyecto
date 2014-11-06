package com.example.app;

import java.util.List;


import android.app.ListActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Listasensores extends ListActivity  {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		setTitle("Listado de sensores");

		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		List<Sensor> sensores = mSensorManager.getSensorList(Sensor.TYPE_ALL);

		Log.d("sensores", "" + sensores.size());

		SensorAdapter adapter = new SensorAdapter(this,
				android.R.layout.simple_list_item_1, sensores);

		setListAdapter(adapter);
	}
	
	class SensorAdapter extends ArrayAdapter<Sensor> {
		private int textViewResourceId;

		public SensorAdapter(Context context, int textViewResourceId, List<Sensor> objects) {
			super(context, textViewResourceId, objects);
			
			this.textViewResourceId = textViewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(textViewResourceId, null);
			}
				
			Sensor s = getItem(position);

			TextView text = (TextView) convertView.findViewById(android.R.id.text1);
			text.setText(s.getName());

			return convertView;
		}

		

	}
}
