package com.example.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class CargarGraficas extends ListActivity {

	private File currentDir;
	private FileArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentDir = new File(Environment.getExternalStorageDirectory()
				.toString() + "/" + getResources().getString(R.string.app_name));
		ficheros(currentDir);
	}

	private void ficheros(File f) {
		File[] dirs = f.listFiles();
		this.setTitle("Directorio actual: " + f.getName());
		List<Opciones> dir = new ArrayList<Opciones>();
		List<Opciones> fls = new ArrayList<Opciones>();
		try {
			for (File ff : dirs) {
				if (ff.isDirectory())
					dir.add(new Opciones(ff.getName(), "Carpeta", ff
							.getAbsolutePath()));
				else {
					fls.add(new Opciones(ff.getName(),
							"Tamaño: " + ff.length(), ff.getAbsolutePath()));
				}
			}
		} catch (Exception e) {

		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new Opciones("..", "Parent Directory", f.getParent()));
		adapter = new FileArrayAdapter(CargarGraficas.this,
				R.layout.cargagrafica, dir);
		this.setListAdapter(adapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Opciones o = adapter.getItem(position);
		if (o.getData().equalsIgnoreCase("folder")
				|| o.getData().equalsIgnoreCase("parent directory")) {
			currentDir = new File(o.getPath());
			ficheros(currentDir);
		} else {
			onFileClick(o);
		}
	}

	private void onFileClick(Opciones o) {
		Toast.makeText(this, "File Clicked: " + o.getName(), Toast.LENGTH_SHORT)
				.show();
		Intent i = new Intent(CargarGraficas.this,LeerCsv.class);
		i.putExtra("fichero", o.getPath());
		startActivity(i);
	}

}
