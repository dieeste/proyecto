
package com.example.app;
import android.hardware.*;


/**
 * Receive events from the <code>Sensor</code> and periodically update the UI
 */
class Ticker extends Thread implements SensorEventListener {

  /**
   * The most recent event, received from the <code>Sensor</code>
   */
  private SensorEvent currentEvent;

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
  private static final int SLEEPTIME = (int) 1000/Grafica.SAMPLERATE;
  
  /**
   * Create a new <code>Ticker</code> and start ticking the <code>Activity</code>
   * @param activity the <code>Activity</code> to tick.
   */
  public Ticker(Grafica activity) {
    worker = new Ticker();
    worker.activity = activity;
    this.activity=activity;
  }
  
  /**
   * For creating the worker thread
   */
  private Ticker() {}
  
  // Interface: SensorEventListener
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }

  // Interface: SensorEventListener
  public void onSensorChanged(SensorEvent event) {
    worker.currentEvent = event;
  }
  
  @Override
  public void run() {
    // This class implements a master and a private worker. We have to figure out on which
    // Thread we are sitting.
    if (worker!=null) {
      // We are the master -> schedule the worker
      try {
        while(true) {
          Thread.sleep(SLEEPTIME);
          activity.runOnUiThread(worker);
        }
      }
      catch (Exception e) {
        // Goodbye
      }
    }
    else {
      // We are the worker -> update the UI
      if (currentEvent!=null) {
        activity.onTick(currentEvent);
      }
    }
  }
  
  
}