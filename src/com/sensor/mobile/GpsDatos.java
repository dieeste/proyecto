package com.sensor.mobile;

public class GpsDatos {
	private double timestamp;
	private double latitud;
	private double longitud;

	public GpsDatos(double timestamp, double latitud, double longitud) {
		this.timestamp = timestamp;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public String toString() {
		return "t=" + timestamp + ", latitud=" + latitud + ", longitud="
				+ longitud;
	}
}
