package com.example.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csvreader.CsvReader;

public class LeerCsv extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	static Graph mGraph;
	LinearLayout layout;
	GraphicalView view;
	public boolean init = false;
	ConcurrentLinkedQueue<AccelData> datos = new ConcurrentLinkedQueue<AccelData>();;
	ConcurrentLinkedQueue<AccelData2> sensor = new ConcurrentLinkedQueue<AccelData2>();
	String nombre;
	String unidad;
	String tituloejey;
	String titulografica;
	String tamano;
	String calidad;
	String nombresensor;
	CheckBox ejex;
	CheckBox ejey;
	CheckBox ejez;
	CheckBox moduloc;
	ImageButton lupa;
	boolean checkx = true;
	boolean checky = true;
	boolean checkz = true;
	boolean checkmodulo = true;
	int tipo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Bundle graficas = getIntent().getExtras();
		nombre = graficas.getString("file");
		titulografica = graficas.getString("nombrearchivo");
		setContentView(R.layout.graficaarchivo);

		layout = (LinearLayout) findViewById(R.id.chart);
		// escucha de los checkbox
		ejex = (CheckBox) findViewById(R.id.ejex);
		ejey = (CheckBox) findViewById(R.id.ejey);
		ejez = (CheckBox) findViewById(R.id.ejez);
		moduloc = (CheckBox) findViewById(R.id.modulo);
		lupa = (ImageButton) findViewById(R.id.lupa);

		ejex.setOnCheckedChangeListener(this);
		ejey.setOnCheckedChangeListener(this);
		ejez.setOnCheckedChangeListener(this);
		moduloc.setOnCheckedChangeListener(this);
		lupa.setOnClickListener(this);

		float scale = getApplicationContext().getResources()
				.getDisplayMetrics().density;
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			tamano = "grande";
		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			tamano = "normal";
		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			tamano = "pequena";
		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			tamano = "extra";
		}
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int dips = 40;

		// TRATAR DE ENCONTRAR EL MISMO RESULTADO
		float pixelBoton = 0;
		float scaleDensity = 0;

		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_XXXHIGH:
			scaleDensity = scale * 640;
			pixelBoton = dips * (scaleDensity / 640);
			calidad = "xxxhigh";
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			scaleDensity = scale * 480;
			pixelBoton = dips * (scaleDensity / 480);
			calidad = "xxhigh";
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			scaleDensity = scale * 320;
			pixelBoton = dips * (scaleDensity / 320);
			calidad = "xhigh";
			break;
		case DisplayMetrics.DENSITY_HIGH: // HDPI
			scaleDensity = scale * 240;
			pixelBoton = dips * (scaleDensity / 240);
			calidad = "alta";
			break;
		case DisplayMetrics.DENSITY_MEDIUM: // MDPI
			scaleDensity = scale * 160;
			pixelBoton = dips * (scaleDensity / 160);
			calidad = "media";
			break;

		case DisplayMetrics.DENSITY_LOW: // LDPI
			scaleDensity = scale * 120;
			pixelBoton = dips * (scaleDensity / 120);
			calidad = "baja";
			break;
		}
		lee(nombre);

	}

	public void lee(String file) {
		int numerolineas = 0;
		try {
			CsvReader fichero = new CsvReader(file);
			fichero.setDelimiter(';');
			fichero.getHeaders();
			fichero.skipLine();
			fichero.readHeaders();

			while (fichero.readRecord()) {
				numerolineas = fichero.getColumnCount();
				if (numerolineas == 5) {
					double tiempo = Double.parseDouble(fichero.get("t (s)"));
					double x = Double.parseDouble(fichero.get("X").replace(",",
							"."));
					double y = Double.parseDouble(fichero.get("Y").replace(",",
							"."));
					double z = Double.parseDouble(fichero.get("Z").replace(",",
							"."));
					double modulo = Double.parseDouble(fichero.get("Modulo")
							.replace(",", "."));
					unidad = fichero.getHeader(5);
					AccelData data = new AccelData(tiempo, x, y, z, modulo);
					datos.add(data);
				} else if (numerolineas == 2) {
					double tiempo = Double.parseDouble(fichero.get("t (s)"));
					double x = Double.parseDouble(fichero.get("X").replace(",",
							"."));
					unidad = fichero.getHeader(2);
					AccelData2 data = new AccelData2(tiempo, x);
					sensor.add(data);
				}
			}
			fichero.close();
			if (numerolineas == 5) {
				tipo = 1;
				if (unidad.equalsIgnoreCase("Unidad sensor: "
						+ getResources()
								.getString(R.string.unidad_acelerometro))
						|| unidad.equalsIgnoreCase("Unidad sensor: m/sÂ²")) {
					tituloejey = "a ("
							+ getString(R.string.unidad_acelerometro) + ")";
					nombresensor = getString(R.string.acelerometro);
					setTitle(nombresensor);
				} else if (unidad.equalsIgnoreCase("Unidad sensor: "
						+ getResources().getString(R.string.unidad_giroscopio))) {
					tituloejey = "ω (" + getString(R.string.unidad_giroscopio)
							+ ")";
					nombresensor = getString(R.string.giroscopio);
					setTitle(nombresensor);
				} else if (unidad.equalsIgnoreCase("Unidad sensor: "
						+ getResources().getString(
								R.string.unidad_campo_magnetico))
						|| unidad.equalsIgnoreCase("Unidad sensor: ÂµT")) {
					tituloejey = "B ("
							+ getString(R.string.unidad_campo_magnetico) + ")";
					nombresensor = getString(R.string.magnetico);
					setTitle(nombresensor);
				}
				iniciar();

			} else if (numerolineas == 2) {
				tipo = 2;
				ejey.setVisibility(TextView.GONE);
				ejez.setVisibility(TextView.GONE);
				moduloc.setVisibility(TextView.GONE);
				if (unidad.equalsIgnoreCase("Unidad sensor: "
						+ getResources().getString(R.string.unidad_luz))) {
					tituloejey = "E (" + getString(R.string.unidad_luz) + ")";
					nombresensor = getString(R.string.luminosidad);
					setTitle(nombresensor);
				} else if (unidad.equalsIgnoreCase("Unidad sensor: "
						+ getResources().getString(R.string.unidad_proximidad))) {
					tituloejey = "d (" + getString(R.string.unidad_proximidad)
							+ ")";
					nombresensor = getString(R.string.proximidad);
					setTitle(nombresensor);
				}
				iniciar2();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.ejex:

			checkx = isChecked;
			if (tipo == 1) {
				iniciar();
				Log.d("chec", "sest: " + checkx);
			} else if (tipo == 2) {
				iniciar2();
			}
			break;
		case R.id.ejey:

			checky = isChecked;
			if (tipo == 1) {
				iniciar();
			} else if (tipo == 2) {
				iniciar2();
			}
			break;
		case R.id.ejez:
			checkz = isChecked;
			if (tipo == 1) {
				iniciar();
			} else if (tipo == 2) {
				iniciar2();
			}
			break;
		case R.id.modulo:
			checkmodulo = isChecked;
			if (tipo == 1) {
				iniciar();
			} else if (tipo == 2) {
				iniciar2();
			}
			break;
		}

	}

	public void iniciar() {
		mGraph = new Graph(this);
		Log.d("jeje", "entramos");
		mGraph.iniciar(datos);
		mGraph.ejeY(datos);
		mGraph.setProperties(checkx, checky, checkz, checkmodulo,
				titulografica, tituloejey, calidad, tamano);
		if (!init) {
			view = mGraph.getGraph();
			layout.addView(view);
			init = true;
		} else {
			layout.removeView(view);
			view = mGraph.getGraph();
			layout.addView(view);
		}
	}

	public void iniciar2() {
		mGraph = new Graph(this);
		mGraph.iniciar2(sensor);
		mGraph.ejeY2(sensor);
		mGraph.setProperties2(checkx, titulografica, tituloejey, calidad,
				tamano);
		if (!init) {
			view = mGraph.getGraph();
			layout.addView(view);
			init = true;
		} else {
			layout.removeView(view);
			view = mGraph.getGraph();
			layout.addView(view);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case (R.id.lupa):
			if (tipo == 1) {
				iniciar();
			} else if (tipo == 2) {
				iniciar2();
			}
			break;
		}

	}
}
