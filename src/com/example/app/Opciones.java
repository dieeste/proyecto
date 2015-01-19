package com.example.app;

public class Opciones implements Comparable<Opciones> {

	private String name;
	private String data;
	private String path;

	public Opciones(String n, String d, String p) {
		name = n;
		data = d;
		path = p;
	}

	public String getName() {
		return name;
	}

	public String getData() {
		return data;
	}

	public String getPath() {
		return path;
	}

	@Override
	public int compareTo(Opciones o) {
		if (this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
		else
			throw new IllegalArgumentException();
	}

}
