package com.example.app;

public class AccelData2 {
	private long timestamp;
	private double x;
	
	
	
	public AccelData2(long timestamp, double x) {
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
	public String toString()
	{
		return "t="+timestamp+", x="+x;
	}
	
}
