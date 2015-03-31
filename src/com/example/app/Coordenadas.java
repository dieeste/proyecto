package com.example.app;

public class Coordenadas {
	private double latitud;
	private double longitud;

	public Coordenadas(double latitud, double longitud) {
		this.latitud = latitud;
		this.longitud = longitud;
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
		return "latitud=" + latitud + ", longitud="
				+ longitud;
	}
}
