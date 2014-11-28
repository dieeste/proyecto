package com.example.app;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.Dialog;
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

public class Grafica extends Activity implements SensorEventListener,
		OnClickListener {
	private static final String TAG = "Acelerometro aplicacion";
	 private boolean[] mGraphs = { true, true, true };
	// Conjunto de datos principal que incluye todas las series de la gráfica
	XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	// Render que incluye el renderer personalizado de la gráfica
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	// Renderer creado más reciente, personalizando las series actuales
	XYSeriesRenderer mRendererActual;
	// Gráfica que mostrará los datos
	GraphView mVistaGrafica;
	// Declaramos los colores del fondo, de las líneas y del texto
	private int mBGColor;
	private int mZeroLineColor;
	private int mStringColor;
	private int[] mAngleColors = new int[3];

	
	
	// Int tipo es la frecuencia que recogemos de la actividad anterior
	int tipo;
	// Hacemos una cola FIFO con listas enlazadas
	ConcurrentLinkedQueue<float[]> datosSensor = new ConcurrentLinkedQueue<float[]>();
	ConcurrentLinkedQueue<float[]> mRawHistory = new ConcurrentLinkedQueue<float[]>();
	// Aquí guardamos los valores de x,y,z en un array que luego irán el la cola
	float[] xyz = new float[3];

	// Declaración del layout
	LinearLayout layout;
	// Maneja los sensores
	SensorManager sensorManager;
	// Declaramos los botones
	Button parar;
	Button guardar;
	Button enviar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grafica);
		// Declaramos objetos
		layout = (LinearLayout) findViewById(R.id.chart);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		parar = (Button) findViewById(R.id.parar);
		guardar = (Button) findViewById(R.id.guardar);
		enviar = (Button) findViewById(R.id.enviar);
		
		//Escuchamos los botones
		parar.setOnClickListener(this);
		guardar.setOnClickListener(this);
		enviar.setOnClickListener(this);
		Resources resources = getResources();

		mStringColor = resources.getColor(R.color.string);
		mBGColor = resources.getColor(R.color.background);
		mZeroLineColor = resources.getColor(R.color.zero_line);
		mAngleColors[SensorManager.DATA_X] = resources
                .getColor(R.color.accele_x);
		mAngleColors[SensorManager.DATA_Y] = resources
                .getColor(R.color.accele_y);
		mAngleColors[SensorManager.DATA_Z] = resources
                .getColor(R.color.accele_z);

		// Recogemos el tipo de frecuencia que hemos pasado desde la actividad
		// de acelerómetro
		Bundle graficas = getIntent().getExtras();
		tipo = graficas.getInt("tipo");

		// declaramos la grafica
		mVistaGrafica = new GraphView(this);
		layout.addView(mVistaGrafica, 0);
		onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (!mDrawRoop) {
            // グラフの描画を再開
            mDrawRoop = true;
            mVistaGrafica.surfaceCreated(mVistaGrafica.getHolder());
		}
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		mRawHistory = new ConcurrentLinkedQueue<float[]>();
		Iniciar_sensores();
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mRawHistory = new ConcurrentLinkedQueue<float[]>();
		Parar_sensores();
		super.onStop();
	}

	private void saveHistory() {
		// Paramos los sensores
		onStop();

		// 保存用Threadを開始
		SaveThread thread = new SaveThread();
		thread.start();
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
			mRawHistory.add(event.values.clone());

			for (int i = 0; i < 3; i++) {
				float valor = event.values[i];
				xyz[i] = valor;
			}

			synchronized (this) {
				 if (datosSensor.size() >= mMaxHistorySize) {
                     datosSensor.poll();
             }
				datosSensor.add(xyz.clone());
				Log.d("Valor xyz", "xxxx " + xyz[1] + "  puta " + datosSensor
						+ "");
			}
		}
	}

	private class GraphView extends SurfaceView implements
			SurfaceHolder.Callback, Runnable {

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

			 Log.i(TAG, "GraphView.run()");

             int width = getWidth();
             mMaxHistorySize = (int) ((width - 20) / mLineWidth);

             Paint textPaint = new Paint();
             textPaint.setColor(mStringColor);
             textPaint.setAntiAlias(true);
             textPaint.setTextSize(14);

             Paint zeroLinePaint = new Paint();
             zeroLinePaint.setColor(mZeroLineColor);
             zeroLinePaint.setAntiAlias(true);

             Paint[] linePaints = new Paint[4];
             for (int i = 0; i < 3; i++) {
                     linePaints[i] = new Paint();
                     linePaints[i].setColor(mAngleColors[i]);
                     linePaints[i].setAntiAlias(true);
                     linePaints[i].setStrokeWidth(2);
             }

             while (mDrawRoop) {
                     Canvas canvas = holder.lockCanvas();

                     if (canvas == null) {
                             break;
                     }

                     canvas.drawColor(mBGColor);

                     float zeroLineY = mZeroLineY + mZeroLineYOffset;

                     synchronized (holder) {
                             float twoLineY = zeroLineY - (20 * mGraphScale);
                             float oneLineY = zeroLineY - (10 * mGraphScale);
                             float minasOneLineY = zeroLineY + (10 * mGraphScale);
                             float minasTwoLineY = zeroLineY + (20 * mGraphScale);

                             canvas.drawText("2", 5, twoLineY + 5, zeroLinePaint);
                             canvas.drawLine(20, twoLineY, width, twoLineY,
                                             zeroLinePaint);

                             canvas.drawText("1", 5, oneLineY + 5, zeroLinePaint);
                             canvas.drawLine(20, oneLineY, width, oneLineY,
                                             zeroLinePaint);

                             canvas.drawText("0", 5, zeroLineY + 5, zeroLinePaint);
                             canvas.drawLine(20, zeroLineY, width, zeroLineY,
                                             zeroLinePaint);

                             canvas.drawText("-1", 5, minasOneLineY + 5, zeroLinePaint);
                             canvas.drawLine(20, minasOneLineY, width, minasOneLineY,
                                             zeroLinePaint);

                             canvas.drawText("-2", 5, minasTwoLineY + 5, zeroLinePaint);
                             canvas.drawLine(20, minasTwoLineY, width, minasTwoLineY,
                                             zeroLinePaint);

                             if (datosSensor.size() > 1) {
                                     Iterator<float[]> iterator = datosSensor.iterator();
                                     float[] before = new float[3];
                                     int x = width - datosSensor.size() * mLineWidth;
                                     int beforeX = x;
                                     x += mLineWidth;

                                     if (iterator.hasNext()) {
                                             float[] history = iterator.next();
                                             for (int angle = 0; angle < 3; angle++) {
                                                     before[angle] = zeroLineY
                                                                     - (history[angle] * mGraphScale);
                                             }
                                             while (iterator.hasNext()) {
                                                     history = iterator.next();
                                                     for (int angle = 0; angle < 3; angle++) {
                                                             float startY = zeroLineY
                                                                             - (history[angle] * mGraphScale);
                                                             float stopY = before[angle];
                                                             if (mGraphs[angle]) {
                                                                     canvas.drawLine(x, startY, beforeX,
                                                                                     stopY, linePaints[angle]);
                                                             }
                                                             before[angle] = startY;
                                                     }
                                                     beforeX = x;
                                                     x += mLineWidth;
                                             }
                                     }
                             }
                     }

                     holder.unlockCanvasAndPost(canvas);

                     try {
                             Thread.sleep(mDrawDelay);
                     } catch (InterruptedException e) {
                             Log.e(TAG, e.getMessage());
                     }
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
					roop = false;

				} catch (InterruptedException e) {
				}
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
				String appName = getResources().getString(R.string.app_name);
				String dirPath = Environment.getExternalStorageDirectory().toString() + "/" + appName;
				File dir = new File(dirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				// CSVãƒ•ã‚¡ã‚¤ãƒ«åã‚’ä½œæˆ
				String fileName = DateFormat
						.format("yyyyMMddkkmmss", System.currentTimeMillis())
						.toString().concat(".csv");

				// CSVãƒ•ã‚¡ã‚¤ãƒ«ã¸ä¿å­˜
				File file = new File(dirPath, fileName);
				if (file.createNewFile()) {
					FileOutputStream fileOutputStream = new FileOutputStream(
							file);
					// ãƒ‡ãƒ¼ã‚¿ã‚’æ›¸ãè¾¼ã‚€
					fileOutputStream.write(csvData.toString().getBytes());
					fileOutputStream.close();
				}

			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				//bundle.putString("msg", e.getMessage());
				bundle.putBoolean("success", false);
			}

			// ãƒãƒ³ãƒ‰ãƒ©ã‚’ä½¿ã£ã¦ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸ãƒ³ã‚°
			msg.setData(bundle);

		}
	}

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		switch (boton.getId()) {
		case (R.id.parar):
			onStop();
			break;
		case (R.id.guardar):
			Log.d("algo","boton guardar");
			saveHistory();
			break;
		case (R.id.enviar):
			break;
		}
	}

	private boolean mDrawRoop = true;
	        private int mDrawDelay = 100;
	        private int mLineWidth = 2;
	        private int mGraphScale = 6;
	        private int mZeroLineY = 230;
	        private int mZeroLineYOffset = 0;

			private int mMaxHistorySize;
				

}
