package com.example.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferenciasGrafica extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.confgrafica);
	}
}
