package com.example.app;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

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

	private int tipoSensor = Grafica.sensor;

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
			break;
		case Sensor.TYPE_GYROSCOPE:
			worker.giroscopioEvent = event;
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			worker.magnetometroEvent = event;
			break;
		case Sensor.TYPE_LIGHT:
			worker.luzEvent = event;
			break;
		case Sensor.TYPE_PROXIMITY:
			worker.proximidadEvent = event;
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
			if (tipoSensor == Sensor.TYPE_ACCELEROMETER) {
				if (currentEvent != null) {
					// Log.d("syn", "else tocker");
					activity.onTick(currentEvent);
				}
			} else if (tipoSensor == Sensor.TYPE_GYROSCOPE) {
				if (giroscopioEvent != null) {
					activity.onTick(giroscopioEvent);
				}
			} else if (tipoSensor == Sensor.TYPE_MAGNETIC_FIELD) {
				if (magnetometroEvent != null) {
					// Log.d("syn", "else tocker");
					activity.onTick(magnetometroEvent);
				}
			} else if (tipoSensor == Sensor.TYPE_LIGHT) {
				if (luzEvent != null) {
					activity.onTick(luzEvent);
				}
			} else if (tipoSensor == Sensor.TYPE_PROXIMITY) {
				if (proximidadEvent != null) {
					// Log.d("syn", "else tocker");
					activity.onTick(proximidadEvent);
				}
			}
		}
	}

}