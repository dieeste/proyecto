package com.sensor.mobile;

public class AccelData2 {
	private double timestamp;
	private double x;

	public AccelData2(double timestamp, double x) {
		this.timestamp = timestamp;
		this.x = x;
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

	public String toString() {
		return "t=" + timestamp + ", x=" + x;
	}

}
