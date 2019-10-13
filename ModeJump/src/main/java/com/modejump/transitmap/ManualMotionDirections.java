package com.modejump.transitmap;

import java.util.ArrayList;

/**
 * 
 * Hold the instructions for navigating
 * 
 * Could abstract out most of the shared functionality with TransitDirections in the future
 * @author Alex
 *
 */
public class ManualMotionDirections {
	
	private ExactLocation startPoint;
	private ExactLocation endPoint;
	private ArrayList<Step> steps;
	private String totalDistance;
	private String duration;

	public ExactLocation getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(ExactLocation startPoint) {
		this.startPoint = startPoint;
	}

	public ExactLocation getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(ExactLocation endPoint) {
		this.endPoint = endPoint;
	}

	public ArrayList<Step> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}

	public String getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
}
