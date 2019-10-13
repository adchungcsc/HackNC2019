package com.modejump.ui;

import java.io.FileInputStream;
import java.util.Properties;

import com.modejump.google.webservices.GoogleMapsAPIKeySet;
import com.modejump.google.webservices.GoogleMapsDirectionWebServices;
import com.modejump.transitmap.TransitDirections;
import com.modejump.transitmap.ExactLocation;
import com.modejump.transitmap.ManualMotionDirections;
import com.modejump.transitmap.Step;

/**
 * This exists solely for basic testing purposes.
 * 
 * @author Alex
 *
 */
public class TestModeJump {

	private static final String googleMapsProperties = "src\\main\\resources\\googlemapsapikeys.properties";

	private static final String directionsKey = "directionsAPIKey";

	private static final String geocodingKey = "geocodingAPIKey";

	private static final String staticMapsKey = "staticMapsAPIKey";

	private static final String placesMapsKey = "placesAPIKey";

	public static void main(String[] args) {
		Properties properties = new Properties();
		String gmDirectionsKey = null, gmGeocodingKey = null, gmMapsKey = null, gmPlacesKey = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(googleMapsProperties);
			properties.load(fileInputStream);
			gmDirectionsKey = properties.getProperty(directionsKey);
			gmGeocodingKey = properties.getProperty(geocodingKey);
			gmMapsKey = properties.getProperty(staticMapsKey);
			gmPlacesKey = properties.getProperty(placesMapsKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		GoogleMapsDirectionWebServices webservices = new GoogleMapsDirectionWebServices(
				new GoogleMapsAPIKeySet(gmDirectionsKey, gmGeocodingKey, gmMapsKey, gmPlacesKey));
		// Rough test if single transit instructions working.
		// Normally I'd junit this, but this data is very hard to work with by hand/pen
		// & paper
		// I'm comparing the outputs to what I'd expect to see on google maps (comparing
		// returned location/data)
		System.out.println("TransitOnlyTest============================================================");
		TransitDirections d = webservices.getDirectionsUsingTransit("35.909108, -79.048576", "35.788786, -78.672599");
		System.out.println("START: " + d.getStartPoint().toString() + " DEST: " + d.getEndPoint().toString());
		System.out.println(
				"FARE: " + d.getLocalCost() + " DISTANCE: " + d.getTotalDistance() + " DURATION " + d.getDuration());
		for (Step step : d.getSteps()) {
			System.out.println(step.getDistance() + " " + step.getDuration() + " " + step.getInstructions());
		}
		System.out.println("LocationFindingTests============================================================");
		// Rough test if it can properly locate a place
		ExactLocation el = webservices.getLocationFromPlace("NC State Memorial Belltower");
		System.out.println("lat. " + el.getLatitude() + " long. " + el.getLongitude());
		// Rough test if it can properly find the closest transit stop
		ExactLocation fetzerGym = webservices.getLocationFromPlace("Fetzer Gym Chapel Hill");
		System.out.println("fetzer gym lat. " + fetzerGym.getLatitude() + " long. " + fetzerGym.getLongitude());
		ExactLocation stop = webservices.getNearbyTransitStopLocation(fetzerGym);
		System.out.println("stop near fetzer gym lat. " + stop.getLatitude() + " long. " + stop.getLongitude());
		System.out.println("WalkOnlyTest============================================================");
		ManualMotionDirections m = webservices.getDirectionsUsingMode("35.908723, -79.048870", "35.912446, -79.058287",
				"bicycle");
		System.out.println("DISTANCE: " + m.getTotalDistance() + " DURATION " + m.getDuration());
		for (Step step : m.getSteps()) {
			System.out.println(step.getDistance() + " " + step.getDuration() + " " + step.getInstructions());
		}
		System.out.println("BikeOnlyTest============================================================");
		ManualMotionDirections b = webservices.getDirectionsUsingMode("35.908723, -79.048870", "35.912446, -79.058287",
				"bicycle");
		System.out.println("DISTANCE: " + b.getTotalDistance() + " DURATION " + b.getDuration());
		for (Step step : b.getSteps()) {
			System.out.println(step.getDistance() + " " + step.getDuration() + " " + step.getInstructions());
		}
		// Can we swap modes? Let's try getting from google chapel hill to NCSU's Talley
		// student union
		// go pack
		System.out.println("MultimodalTest============================================================");
		ExactLocation googleLoc = webservices.getLocationFromPlace("Google Chapel Hill");
		ExactLocation talleyLoc = webservices.getLocationFromPlace("Talley Student Union");
		ExactLocation nearestTransitGoogle = webservices.getNearbyTransitStopLocation(googleLoc);
		ExactLocation nearestTransitTalley = webservices.getNearbyTransitStopLocation(talleyLoc);
		// let's hop on a chapel hill bike to the stop.
		ManualMotionDirections googleToTransit = webservices.getDirectionsUsingMode(googleLoc.toString(),
				nearestTransitGoogle.toString(), "bicycle");
		// Hop on the bus to Raleigh via GoTriangle's regional transit center + RDU
		// airport depending on what time of day it is.
		TransitDirections universityToUniversity = webservices
				.getDirectionsUsingTransit(nearestTransitGoogle.toString(), nearestTransitTalley.toString());
		// Woohoo we made it to discount Franklin street (probably).
		// Let's rent a certain green colored bike to get to the student union.
		ManualMotionDirections transitToTalley = webservices.getDirectionsUsingMode(nearestTransitTalley.toString(),
				talleyLoc.toString(), "bicycle");
		//Let's see what that took.
		System.out.println("*******************************************************************");
		for (Step step : googleToTransit.getSteps()) {
			System.out.println(step.getDistance() + " " + step.getDuration() + " " + step.getInstructions());
		}
		System.out.println("Get on bus");
		for (Step step : universityToUniversity.getSteps()) {
			System.out.println(step.getDistance() + " " + step.getDuration() + " " + step.getInstructions());
		}
		System.out.println("Get off bus");
		for (Step step : transitToTalley.getSteps()) {
			System.out.println(step.getDistance() + " " + step.getDuration() + " " + step.getInstructions());
		}
		System.out.println("-------------------------------------------------------------------");
		System.out.println("p1 googleToStop: " + googleToTransit.getTotalDistance() + " time " + googleToTransit.getDuration());
		System.out.println("p2 stopToStop: " + universityToUniversity.getTotalDistance() + " time " + universityToUniversity.getDuration());
		System.out.println("p3 stopToNCSUTalley: " + transitToTalley.getTotalDistance() + " time " + transitToTalley.getDuration());
		System.out.println("*******************************************************************");
	}

}
