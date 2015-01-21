package com.example.app;

public class AccelData2 {
	private long timestamp;
	private double x;
	private double modulo;
	
	
	
	public AccelData2(long timestamp, double x, double modulo) {
		this.timestamp = timestamp;
		this.x = x;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getModulo() {
		return modulo;
	}
	public void setModulo(double modulo) {
		this.modulo= modulo;
	}
	public String toString()
	{
		return "t="+timestamp+", x="+x+", modulo="+modulo;
	}
	
}
