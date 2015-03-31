package com.example.app;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class RepresentarGps extends FragmentActivity {
	private GoogleMap googleMap;
	Polyline line;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		try {
			if (googleMap == null) {
				googleMap = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.map)).getMap();
			}
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			googleMap.getUiSettings().setZoomControlsEnabled(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<LatLng> puntos = (ArrayList<LatLng>) getIntent()
				.getSerializableExtra("puntos");
		MarkerOptions marker = new MarkerOptions();

		PolylineOptions options = new PolylineOptions().width(5)
				.color(Color.BLUE).geodesic(true);
		for (LatLng datos : puntos) {
			options.add(datos);
			marker.position(datos);
			googleMap.addMarker(marker);
			Log.d("hola", "esto es datos: " + datos);
		}
		line = googleMap.addPolyline(options);
	}

}