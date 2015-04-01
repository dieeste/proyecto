package com.example.app;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		ArrayList<LatLng> puntos = (ArrayList<LatLng>) getIntent()
				.getSerializableExtra("puntos");
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
		Log.d("tamano", "represetnar " + puntos.size());
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