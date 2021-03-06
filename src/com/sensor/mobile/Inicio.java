package com.sensor.mobile;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

public class Inicio extends Activity implements OnClickListener {

	private static final long SPLASH_SCREEN_DELAY = 3000;
	TimerTask task;
	RelativeLayout init;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set portrait orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// Hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inicio);
		init = (RelativeLayout) findViewById(R.id.init);
		init.setOnClickListener(this);
		task = new TimerTask() {
			@Override
			public void run() {

				// Start the next activity
				Intent mainIntent = new Intent().setClass(Inicio.this,
						MainActivity.class);
				startActivity(mainIntent);

				// Close the activity so the user won't able to go back this
				// activity pressing Back button
				finish();
			}
		};

		// Simulate a long loading process on application startup.
		Timer timer = new Timer();
		timer.schedule(task, SPLASH_SCREEN_DELAY);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case (R.id.init):
			Intent mainIntent = new Intent().setClass(Inicio.this,
					MainActivity.class);
			startActivity(mainIntent);
			task.cancel();
			finish();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

}
