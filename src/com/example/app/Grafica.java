package com.example.app;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Grafica extends Activity implements SensorEventListener {

	// Conjunto de datos principal que incluye todas las series de la gráfica
	XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	// Render que incluye el renderer personalizado de la gráfica
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	// Renderer creado más reciente, personalizando las series actuales
	XYSeriesRenderer mRendererActual;
	// Gráfica que mostrará los datos
	GraphView mVistaGrafica;
	
	private int mBGColor;
	private int mZeroLineColor;
	private int mStringColor;

	int tipo;
	ConcurrentLinkedQueue<float[]> datosSensor = new ConcurrentLinkedQueue<float[]>();
	float [] xyz= new float[3];
	
	// Declaración del layout
	LinearLayout layout;
	// Maneja los sensores
	SensorManager sensorManager;
	Button parar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grafica);
		// Declaramos objetos
		layout = (LinearLayout) findViewById(R.id.chart);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		parar = (Button) findViewById(R.id.parar);

		Resources resources = getResources();
		
		mStringColor = resources.getColor(R.color.string);
		mBGColor = resources.getColor(R.color.background);
		mZeroLineColor = resources.getColor(R.color.zero_line);
		// Declaramos la escucha del botón parar
		parar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Parar_sensores();
			}
		});

		// Recogemos el tipo de frecuencia que hemos pasado desde la actividad
		// de acelerómetro
		Bundle graficas = getIntent().getExtras();
		tipo = graficas.getInt("tipo");

		//declaramos la grafica
		mVistaGrafica = new GraphView(this);
		layout.addView(mVistaGrafica, 0);
		Iniciar_sensores();
	}

	protected void Iniciar_sensores() {
		sensorManager
				.registerListener(this, sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), tipo);
		mVistaGrafica.surfaceCreated(mVistaGrafica.getHolder());
	}

	protected void Parar_sensores() {
		sensorManager.unregisterListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
			switch (event.sensor.getType()) {

			case Sensor.TYPE_ACCELEROMETER:
				
				
				for (int i=0;i <3;i++) {
				float valor = event.values[i];
				xyz[i] = valor;
				}
				
				synchronized (this) {
					datosSensor.add(xyz.clone());
Log.d("Valor xyz", "xxxx "+ xyz[1]+"  puta "+ datosSensor+"");
			}
		}
	}
	private class GraphView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
		
		private Thread hilo;
		private SurfaceHolder holder;
		
		public GraphView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			holder = getHolder();
			holder.addCallback(this);

			setFocusable(true);
			requestFocus();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			

			Paint textPaint = new Paint();
			textPaint.setColor(mStringColor);
			textPaint.setAntiAlias(true);
			textPaint.setTextSize(14);

			Paint zeroLinePaint = new Paint();
			zeroLinePaint.setColor(mZeroLineColor);
			zeroLinePaint.setAntiAlias(true);

		

		
				Canvas canvas = holder.lockCanvas();

			

				canvas.drawColor(mBGColor);

				float zeroLineY = 230;

				synchronized (holder) {
					float twoLineY = zeroLineY - (20 * 6);
					float oneLineY = zeroLineY - (10 * 6);
					float minasOneLineY = zeroLineY + (10 * 6);
					float minasTwoLineY = zeroLineY + (20 * 6);

					canvas.drawText("2", 5, twoLineY + 5, zeroLinePaint);
					canvas.drawLine(20, twoLineY, getWidth(), twoLineY,
							zeroLinePaint);

					canvas.drawText("1", 5, oneLineY + 5, zeroLinePaint);
					canvas.drawLine(20, oneLineY, getWidth(), oneLineY,
							zeroLinePaint);

					canvas.drawText("0", 5, zeroLineY + 5, zeroLinePaint);
					canvas.drawLine(20, zeroLineY, getWidth(), zeroLineY,
							zeroLinePaint);

					canvas.drawText("-1", 5, minasOneLineY + 5, zeroLinePaint);
					canvas.drawLine(20, minasOneLineY, getWidth(), minasOneLineY,
							zeroLinePaint);

					canvas.drawText("-2", 5, minasTwoLineY + 5, zeroLinePaint);
					canvas.drawLine(20, minasTwoLineY, getWidth(), minasTwoLineY,
							zeroLinePaint);

					if (datosSensor.size() > 1) {
						Iterator<float[]> iterator = datosSensor.iterator();
						float[] before = new float[4];
						int x = getWidth() - datosSensor.size() * 2;
						int beforeX = x;
						x += 2;

						if (iterator.hasNext()) {
							float[] history = iterator.next();
							for (int angle = 0; angle < 4; angle++) {
								before[angle] = zeroLineY
										- (history[angle] * 6);
							}
							while (iterator.hasNext()) {
								history = iterator.next();
								for (int angle = 0; angle < 4; angle++) {
									float startY = zeroLineY
											- (history[angle] * 6);
									
									before[angle] = startY;
								}
								beforeX = x;
								x += 2;
							}
						}
					}
				}

				holder.unlockCanvasAndPost(canvas);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					
				}
			}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			hilo = new Thread(this);
			hilo.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			boolean roop = true;
			while (roop) {
					try {
						hilo.join();
						roop= false;
						
					} catch (InterruptedException e) { }
			}
			hilo = null;
			
		}
		
		
	}
	private class SaveThread extends Thread {
		@Override
		public void run() {
			// CSVãƒ‡ãƒ¼ã‚¿ã‚’ä½œæˆ
			StringBuilder csvData = new StringBuilder();
			Iterator<float[]> iterator = datosSensor.iterator();
			while (iterator.hasNext()) {
				float[] values = iterator.next();
				for (int angle = 0; angle < 3; angle++) {
					csvData.append(String.valueOf(values[angle]));
					if (angle < 3) {
						csvData.append(",");
					}
				}
				csvData.append("\n");
			}

			// ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸ãƒ³ã‚°ã®æº–å‚™
			Message msg = new Message();
			Bundle bundle = new Bundle();

			try {
				// SDã‚«ãƒ¼ãƒ‰ã«ãƒ‡ã‚£ãƒ¬ã‚¯ãƒˆãƒªãŒãªã‘ã‚Œã°ä½œæˆ
				String appName = "app";
				String dirPath = Environment.getExternalStorageDirectory()
						.toString()
						+ "/" + appName;
				File dir = new File(dirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				// CSVãƒ•ã‚¡ã‚¤ãƒ«åã‚’ä½œæˆ
				String fileName = DateFormat.format("yyyyMMddkkmmss", System.currentTimeMillis()).toString().concat(".csv");
				
				// CSVãƒ•ã‚¡ã‚¤ãƒ«ã¸ä¿å­˜
				File file = new File(dirPath, fileName.toString());
				if (file.createNewFile()) {
					FileOutputStream fileOutputStream = new FileOutputStream(
							file);
					// ãƒ‡ãƒ¼ã‚¿ã‚’æ›¸ãè¾¼ã‚€
					fileOutputStream.write(csvData.toString().getBytes());
					fileOutputStream.close();
				}

				// ãƒ•ã‚¡ã‚¤ãƒ«ã¸ã®å‡ºåŠ›å®Œäº†ã‚’é€šçŸ¥
				bundle.putString("msg", "GUARDADO");
				bundle.putBoolean("success", true);
			} catch (Exception e) {
			

				// ãƒ•ã‚¡ã‚¤ãƒ«ã¸ã®å‡ºåŠ›å¤±æ•—ã‚’é€šçŸ¥
				bundle.putString("msg", e.getMessage());
				bundle.putBoolean("success", false);
			}

			// ãƒãƒ³ãƒ‰ãƒ©ã‚’ä½¿ã£ã¦ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸ãƒ³ã‚°
			msg.setData(bundle);
		
		}
	}
}

