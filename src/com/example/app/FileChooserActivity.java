package com.example.app;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FileChooserActivity extends ListActivity {

	private File currentFolder;
	private FileArrayAdapter fileArrayListAdapter;
	private FileFilter fileFilter;
	private File fileSelected;
	private ArrayList<String> extensions;
	ArrayList<Uri> ficheros = new ArrayList<Uri>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras
					.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS) != null) {
				extensions = extras
						.getStringArrayList(Constants.KEY_FILTER_FILES_EXTENSIONS);
				fileFilter = new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						return ((pathname.isDirectory()) || (pathname.getName()
								.contains(".") ? extensions.contains(pathname
								.getName().substring(
										pathname.getName().lastIndexOf(".")))
								: false));
					}
				};
			}
		}
		currentFolder = new File(Environment.getExternalStorageDirectory()
				.toString() + "/" + getResources().getString(R.string.app_name));
		fill(currentFolder);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						boolean borrado = false;
						FileInfo fileDescriptor = fileArrayListAdapter
								.getItem(position);
						File archivo = new File(fileDescriptor.getPath());
						if (archivo.isFile()) {
							archivo.getPath();
							Uri path = Uri.fromFile(archivo);
							if (ficheros.isEmpty()) {
								Log.d("hola", "este es lo coge" + path);
								ficheros.add(path);
								fileDescriptor.selected = true;
								fileArrayListAdapter.notifyDataSetChanged();
								// view.setSelected(true);
							} else {
								for (int i = 0; i < ficheros.size(); i++) {
									if (ficheros.get(i).equals(path)) {
										Log.d("hola", "este lo quita" + path);
										ficheros.remove(i);
										fileDescriptor.selected = false;
										fileArrayListAdapter
												.notifyDataSetChanged();

										// view.setSelected(false);
										Log.d("hola", "este tama iff  "
												+ ficheros.size());
										borrado = true;
									}
								}
								if (borrado == false) {
									for (int i = 0; i < ficheros.size(); i++) {
										if (!ficheros.get(i).equals(path)) {
											Log.d("hola", "este lo mete" + path);
											ficheros.add(path);
											fileDescriptor.selected = true;
											fileArrayListAdapter
													.notifyDataSetChanged();

											// view.setSelected(true);
											Log.d("hola", "este tama else  "
													+ ficheros.size());
											break;
										}
									}
								}
							}
						}
						return true;
					}

				});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			setResult(Activity.RESULT_CANCELED);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void fill(File f) {
		File[] folders = null;
		if (fileFilter != null)
			folders = f.listFiles(fileFilter);
		else
			folders = f.listFiles();

		this.setTitle(getString(R.string.currentDir) + ": " + f.getName());
		List<FileInfo> dirs = new ArrayList<FileInfo>();
		List<FileInfo> files = new ArrayList<FileInfo>();
		try {
			for (File file : folders) {
				if (file.isDirectory() && !file.isHidden())
					// si es un directorio en el data se ponemos la contante
					// folder
					dirs.add(new FileInfo(file.getName(), Constants.FOLDER,
							file.getAbsolutePath(), true, false));
				else {
					if (!file.isHidden())
						files.add(new FileInfo(file.getName(),
								getString(R.string.fileSize) + ": "
										+ file.length(),
								file.getAbsolutePath(), false, false));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(dirs);
		Collections.sort(files);
		dirs.addAll(files);

		fileArrayListAdapter = new FileArrayAdapter(FileChooserActivity.this,
				R.layout.file_row, dirs);
		this.setListAdapter(fileArrayListAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
		FileInfo fileDescriptor = fileArrayListAdapter.getItem(position);
		if (fileDescriptor.isFolder() || fileDescriptor.isParent()) {
			currentFolder = new File(fileDescriptor.getPath());
			fill(currentFolder);
		} else {
			fileSelected = new File(fileDescriptor.getPath());
			String filenameArray[] = fileSelected.getPath().split("\\.");
			String extension = filenameArray[filenameArray.length - 1];
			if (!extension.equalsIgnoreCase("csv")) {
				Toast.makeText(this, "No es posible abrir este archivo",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent vamos = new Intent(this, LeerCsv.class);
			vamos.putExtra("file", fileSelected.getPath());
			vamos.putExtra("nombrearchivo", fileSelected.getName());
			startActivity(vamos);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Cargamos las opciones que vamos a usar en esta pantalla
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menucargar, menu);
		return true;
		/** true -> el menú ya está visible */
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Elegimos entre las opciones disponibles en esta pantalla
		switch (item.getItemId()) {
		case (R.id.enviar):
			// enviar(();
			if (ficheros.size() == 0) {
				Toast.makeText(this,
						getResources().getString(R.string.datosCompartir),
						Toast.LENGTH_SHORT).show();
			}else {
			enviar(ficheros);
			ficheros.remove(true);
			}
			break;
		case R.id.menu_ayuda:
			Intent ayuda = new Intent(this, Ayuda.class);
			final String[] TITLES = { getString(R.string.cargargraficas),
					getString(R.string.inicio), getString(R.string.medicion),
					getString(R.string.grafica), getString(R.string.teoria) };
			ayuda.putExtra("TITLES", TITLES);
			startActivity(ayuda);
			break;
		}
		return true;
		/** true -> consumimos el item, no se propaga */
	}

	protected void enviar(ArrayList<Uri> ficheros) {
		// TODO Auto-generated method stub
		this.setProgressBarVisibility(false);
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
		sendIntent.setType("file/*");
		sendIntent.putExtra(Intent.EXTRA_STREAM, ficheros);
		startActivity(sendIntent);
	}

}