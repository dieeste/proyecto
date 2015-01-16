package com.example.app;


public class CargarGraficas extends Thread{
	/*@Override
	public void run() {
		
		for (AccelData values : sensorDatas) {
			long f = (values.getTimestamp() - t) / 1000;
			double d = ((values.getTimestamp() - t) % 1000) * 0.001;
			double fin = f + d;
			csvData.append(String.valueOf(fin) + ", "
					+ String.valueOf(values.getX()) + ", "
					+ String.valueOf(values.getY()) + ", "
					+ String.valueOf(values.getZ()) + "\n");
		}

		Bundle bundle = new Bundle();
		Message msg = new Message();

		try {

			String appName = getResources().getString(R.string.app_name);
			String dirPath = Environment.getExternalStorageDirectory()
					.toString() + "/" + appName;
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = DateFormat
					.format("dd-MM-yyyy HH-mm-ss",
							System.currentTimeMillis()).toString()
					.concat(".csv");

			File file = new File(dirPath, fileName);
			if (file.createNewFile()) {
				FileOutputStream fileOutputStream = new FileOutputStream(
						file);

				fileOutputStream.write(csvData.toString().getBytes());
				fileOutputStream.close();

			}
			catch (Exception e) {
				// Si no se ha podido guardar entonces nos envía un mensaje
				// diciendo que no se ha guardado
				
			}*/
}
