package com.example.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Receive events from the <code>Sensor</code> and periodically update the UI
 */
class Ticker extends Thread implements SensorEventListener {

	/**
	 * The most recent event, received from the <code>Sensor</code>
	 */
	private SensorEvent currentEvent;
	private SensorEvent giroscopioEvent;
	private SensorEvent magnetometroEvent;
	private SensorEvent luzEvent;
	private SensorEvent proximidadEvent;

	/**
	 * The activity, we are ticking for
	 */
	private Grafica activity;

	/**
	 * Reference to the worker thread which does the actual UI updating
	 */
	private Ticker worker;

	/**
	 * How long to sleep between taking a sample
	 */
	private int SLEEPTIME = Grafica.SAMPLERATE;


	/**
	 * Create a new <code>Ticker</code> and start ticking the
	 * <code>Activity</code>
	 * 
	 * @param activity
	 *            the <code>Activity</code> to tick.
	 */
	public Ticker(Grafica activity) {
		worker = new Ticker();
		worker.activity = activity;
		this.activity = activity;
	}

	/**
	 * For creating the worker thread
	 */
	private Ticker() {
	}

	// Interface: SensorEventListener
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	// Interface: SensorEventListener
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			worker.currentEvent = event;
			if(activity.acce==true){
				double x = event.values[0];
				double y = event.values[1];
				double z = event.values[2];
				double modulo = Double.valueOf(Math.abs(Math.sqrt(Math.pow(x, 2)
						+ Math.pow(y, 2) + Math.pow(z, 2))));
				double timestamp = System.currentTimeMillis();
					AccelData data = new AccelData(timestamp, x, y, z, modulo);
					activity.sensorDatas.add(data);
			}
			break;
		case Sensor.TYPE_GYROSCOPE:
			worker.giroscopioEvent = event;
			if (activity.giro==true){
				double x2 = event.values[0];
				double y2 = event.values[1];
				double z2 = event.values[2];
				double modulo2 = Double.valueOf(Math.abs(Math.sqrt(Math.pow(x2, 2)
						+ Math.pow(y2, 2) + Math.pow(z2, 2))));
				double timestamp2 = System.currentTimeMillis();
				AccelData data2 = new AccelData(timestamp2, x2, y2, z2, modulo2);
				activity.sensorGiroscopio.add(data2);
			}
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			worker.magnetometroEvent = event;
			if (activity.magne==true){
				double x4 = event.values[0];
				double y4 = event.values[1];
				double z4 = event.values[2];
				double modulo4 = Double.valueOf(Math.abs(Math.sqrt(Math.pow(x4, 2)
						+ Math.pow(y4, 2) + Math.pow(z4, 2))));
				double timestamp4 = System.currentTimeMillis();

				AccelData data4 = new AccelData(timestamp4, x4, y4, z4, modulo4);
				activity.sensorMagnetico.add(data4);
			}
			break;
		case Sensor.TYPE_LIGHT:
			worker.luzEvent = event;
			if (activity.luz==true){
				double x3 = event.values[0];
				double timestamp3 = System.currentTimeMillis();

				AccelData2 data3 = new AccelData2(timestamp3, x3);
				activity.sensorLuz.add(data3);
			}
			break;
		case Sensor.TYPE_PROXIMITY:
			worker.proximidadEvent = event;
			if (activity.proxi==true){
				double x5 = event.values[0];
				double timestamp5 = System.currentTimeMillis();
				
				AccelData2 data5 = new AccelData2(timestamp5, x5);
				activity.sensorProximidad.add(data5);
			}
			break;
		}
	}

	@Override
	public void run() {
		// This class implements a master and a private worker. We have to
		// figure out on which
		// Thread we are sitting.
		if (worker != null) {
			// We are the master -> schedule the worker
			try {
				while (true) {
					Thread.sleep(SLEEPTIME);
					activity.runOnUiThread(worker);
					// Log.d("syn", "sleeetp");
				}
			} catch (Exception e) {
				// Goodbye
			}
		} else {
			// We are the worker -> update the UI
			if (activity.sensor == Sensor.TYPE_ACCELEROMETER) {
				if (currentEvent != null) {
					// Log.d("syn", "else tocker");
					activity.onTick(currentEvent);
				}
			} else if (activity.sensor == Sensor.TYPE_GYROSCOPE) {
				if (giroscopioEvent != null) {
					activity.onTick(giroscopioEvent);
				}
			} else if (activity.sensor == Sensor.TYPE_MAGNETIC_FIELD) {
				if (magnetometroEvent != null) {
					// Log.d("syn", "else tocker");
					activity.onTick(magnetometroEvent);
				}
			} else if (activity.sensor == Sensor.TYPE_LIGHT) {
				if (luzEvent != null) {
					activity.onTick(luzEvent);
				}
			} else if (activity.sensor == Sensor.TYPE_PROXIMITY) {
				if (proximidadEvent != null) {
					// Log.d("syn", "else tocker");
					activity.onTick(proximidadEvent);
				}
			}
		}
	}

}