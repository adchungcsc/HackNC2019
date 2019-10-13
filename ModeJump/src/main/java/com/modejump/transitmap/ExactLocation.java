package com.modejump.transitmap;

public class ExactLocation {
	
	private double latitude;
	private double longitude;
	
	public ExactLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public ExactLocation(String location) {
		String coordinates[] = location.split(",");
		//Going to trust after this that the user didn't try to make this break.
		//Not enough time and highly unlikely that this will break with the way I'm using it at the moment.
		if(coordinates.length != 2) {
			throw new IllegalArgumentException();
		}
		//do a little cleanup
		coordinates[0].strip();
		coordinates[1].strip();
		this.latitude = Double.parseDouble(coordinates[0]);
		this.longitude = Double.parseDouble(coordinates[0]);
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Format string form of exact location to API readable standard format.
	 * @return formatted exact location
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(latitude);
		sb.append(',');
		sb.append(longitude);
		return sb.toString();
	}
}
