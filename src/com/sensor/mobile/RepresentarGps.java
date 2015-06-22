package com.sensor.mobile;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RepresentarGps extends FragmentActivity {
	private GoogleMap googleMap;
	Polyline line;
	ArrayList<LatLng> puntos = new ArrayList<LatLng>();
	TextView distancia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		DecimalFormat formateador3 = new DecimalFormat("0.00000");
		Bundle map = getIntent().getExtras();
		String titulo = map.getString("nombre");
		double distance = map.getDouble("distancia");
		puntos = LeerCsv.localizacion;
		distancia = (TextView) findViewById(R.id.distancia);
		distancia.setText("La distacia recorrida es: "
				+ String.valueOf(formateador3.format(distance)) + " km");
		// ArrayList<LatLng> puntos = (ArrayList<LatLng>) getIntent()
		// .getSerializableExtra("puntos");
		setTitle(titulo);
		Log.d("tama","este es: "+puntos.size());
		try {
			if (googleMap == null) {
				googleMap = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.map)).getMap();
			}
			LatLng ini = puntos.get(0);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ini, 15));
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			googleMap.getUiSettings().setZoomControlsEnabled(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		MarkerOptions marker = new MarkerOptions();
		PolylineOptions options = new PolylineOptions().width(5)
				.color(Color.BLUE).geodesic(true);
		for (LatLng datos : puntos) {
			options.add(datos);
		}

		marker.position(puntos.get(0));
		googleMap.addMarker(marker);
		marker.position(puntos.get(puntos.size() - 1)).icon(
				BitmapDescriptorFactory.fromResource(R.drawable.llegada));
		googleMap.addMarker(marker);
		line = googleMap.addPolyline(options);
	}

}