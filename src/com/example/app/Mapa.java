package com.example.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class Mapa extends FragmentActivity {
	private GoogleMap googleMap;

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
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
