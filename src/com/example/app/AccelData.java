package com.example.app;

public class AccelData {
	private double timestamp;
	private double x;
	private double y;
	private double z;
	private double modulo;
	
	public AccelData(double timestamp, double x, double y, double z, double modulo) {
		this.timestamp = timestamp;
		this.x = x;
		this.y = y;
		this.z = z;
		this.modulo = modulo;
	}
	public double getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public double getModulo() {
		return modulo;
	}
	public void setModulo(double modulo) {
		this.modulo= modulo;
	}
	
	public String toString()
	{
		return "t="+timestamp+", x="+x+", y="+y+", z="+z+", modulo="+modulo;
	}
	

}
