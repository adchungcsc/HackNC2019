package com.modejump.transitmap;

import java.util.ArrayList;

/**
 * POJO to keep track of the direction instructions for a request.
 * @author Alex
 *
 */
public class Directions {
	private ExactLocation startPoint;
	private ExactLocation endPoint;
	private String localCost;
	private ArrayList<Step> steps;
	private String totalDistance;
	private String duration;
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
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
	public String getLocalCost() {
		return localCost;
	}
	public void setLocalCost(String localCost) {
		this.localCost = localCost;
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
	
	
}
