package com.modejump.transitmap;

public class Step {
	
	private String distance;
	private String duration;
	private String instructions;

	public Step(String distance, String duration, String instructions) {
		super();
		this.distance = distance;
		this.duration = duration;
		this.instructions = instructions;
	}
	
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
}
