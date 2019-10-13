package com.modejump.ui;

import java.io.FileInputStream;
import java.util.Properties;

import com.modejump.google.webservices.GoogleMapsAPIKeySet;
import com.modejump.google.webservices.GoogleMapsDirectionWebServices;
import com.modejump.transitmap.Directions;
import com.modejump.transitmap.ExactLocation;
import com.modejump.transitmap.Step;

/**
 * This exists solely for basic testing purposes.
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
		//Rough test if single transit instructions working.
		System.out.println("============================================================");
		Directions d = webservices.getDirectionsUsingTransit("35.909108, -79.048576", "35.788786, -78.672599");
		System.out.println("START: " + d.getStartPoint().toString() + " DEST: " + d.getEndPoint().toString());
		System.out.println("FARE: " + d.getLocalCost() + " DISTANCE: " + d.getTotalDistance() + " DURATION " + d.getDuration());
		for(Step step: d.getSteps()) {
			System.out.println(step.getDistance() + " " + step.getDuration() + " " + step.getInstructions());
		}
		System.out.println("============================================================");
		//Rough test if it can properly locate a place
		ExactLocation el = webservices.getLocationFromPlace("NC State Memorial Belltower");
		System.out.println("lat. " + el.getLatitude() + " long. " + el.getLongitude());
		//Rough test if it can properly find the closest transit stop
		ExactLocation fetzerGym = webservices.getLocationFromPlace("Fetzer Gym Chapel Hill");
		System.out.println("fetzer gym lat. " + fetzerGym.getLatitude() + " long. " + fetzerGym.getLongitude());
		ExactLocation stop = webservices.getNearbyTransitStopLocation(fetzerGym);
		System.out.println("stop near fetzer gym lat. " + stop.getLatitude() + " long. " + stop.getLongitude());
		

	}

}
