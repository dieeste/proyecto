package com.example.app;

import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Setting extends PreferenceActivity implements
		OnPreferenceChangeListener {
	PreferenceManager manager;
	ListPreference listPreference;
	SharedPreferences sharedPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);

		manager = getPreferenceManager();
		listPreference = (ListPreference) manager.findPreference("idioma");

		listPreference.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		Resources resource = getResources();
		Configuration config = resource.getConfiguration();
		Locale spanish = new Locale("es", "ES");
		Locale portuguese = new Locale("pt", "PT");
		int pos = Integer.parseInt((String) newValue);
		if (pos == 1) {
			sharedPreference.edit().putString("language", "en").commit();
			config.locale = Locale.ENGLISH;
			listPreference.setValue("1");
		} else if (pos == 2) {
			sharedPreference.edit().putString("language", "fr").commit();
			config.locale = Locale.FRANCE;
			listPreference.setValue("2");
		} else if (pos == 3) {
			sharedPreference.edit().putString("language", "es").commit();
			config.locale = spanish;
			listPreference.setValue("3");
		} else if (pos == 4) {
			sharedPreference.edit().putString("language", "pt").commit();
			config.locale = portuguese;
			listPreference.setValue("4");
		} else {
			sharedPreference.edit().putString("language", "auto").commit();
			config.locale = Locale.getDefault();
			listPreference.setValue("0");
		}

		getBaseContext().getResources().updateConfiguration(config, null);

		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		return false;
	}
}
