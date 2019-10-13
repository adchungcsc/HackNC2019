package com.modejump.google.webservices;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modejump.transitmap.TransitDirections;
import com.modejump.transitmap.ExactLocation;
import com.modejump.transitmap.ManualMotionDirections;
import com.modejump.transitmap.Step;

/**
 * 
 * @author Alex
 *
 */
public class GoogleMapsDirectionWebServices {
	/** Base string for querying direction from API */
	private static final String DIRECTION_QUERY_BASE = "https://maps.googleapis.com/maps/api/directions/json?";
	
	/** Log errors */
	static Logger logger = Logger.getLogger(GoogleMapsDirectionWebServices.class.getName());

	/** Google maps API keys */
	private GoogleMapsAPIKeySet keys;

	/**
	 * Get ready to use google maps API with provided key (Generate from google
	 * cloud platform console).
	 * 
	 * @param googleMapsApiKey API key that has permissions to query from the server
	 *                         IP (machine this program is running on).
	 * @throws IllegalArgumentException Need to pass in a valid API key
	 */
	public GoogleMapsDirectionWebServices(GoogleMapsAPIKeySet keys) {
		if (keys == null) {
			logger.log(Level.WARNING, "Google Maps API key(s) were invalid");
			throw new IllegalArgumentException();
		}
		this.keys = keys;
	}

	/**
	 * 
	 * @param origin
	 * @param destination
	 * @param mode bicycling or walking (not hard enforced as of now)
	 * @return
	 */
	public ManualMotionDirections getDirectionsUsingMode(String origin, String destination, String mode) {
		// Build the HTTPS API query for a get
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(DIRECTION_QUERY_BASE);
		stringBuilder.append("origin=");
		stringBuilder.append(origin);
		stringBuilder.append("&destination=");
		stringBuilder.append(destination);
		stringBuilder.append("&mode=");
		stringBuilder.append(mode);
		stringBuilder.append("&key=");
		stringBuilder.append(keys.getDirectionsAPIKey());
		// Get ready to read the returned JSON
		ObjectMapper mapper = new ObjectMapper();
		ManualMotionDirections directions = new ManualMotionDirections();
		try {
			// Query the google maps API and store it in the POJO
			// Taking a stroll in the forest.
			String url = stringBuilder.toString();
			url = formatSpacesWithinUrl(url);
			//System.out.println(url);
			JsonNode directionsJsonRoots = mapper.readTree(new URL(url));
			JsonNode route = directionsJsonRoots.findValue("routes");
			if (route == null) {
				return null;
			}
			String duration = route.findValue("duration").findValue("text").asText();
			String distance = route.findValue("distance").findValue("text").asText();
			ArrayList<Step> steps = processTransitSteps(route.findValue("steps"));
			directions.setDuration(duration);
			directions.setTotalDistance(distance);
			directions.setSteps(steps);
			directions.setEndPoint(new ExactLocation(destination));
			directions.setStartPoint(new ExactLocation(origin));
		} catch (JsonParseException e) {
			logger.log(Level.WARNING, "Google maps direction JSON parse", e);
		} catch (JsonMappingException e) {
			logger.log(Level.WARNING, "Google maps direction JSON mapping", e);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Google maps direction JSON IO", e);
		}
		//logger.log(Level.INFO, "Successful parse.");
		return directions;
	}

	private ArrayList<Step> processTransitSteps(JsonNode steps) {
		ArrayList<Step> overallSteps = new ArrayList<>();
		for (JsonNode step : steps) {
			String distance = step.findValue("distance").findValue("text").asText();
			String duration = step.findValue("duration").findValue("text").asText();
			String html_instructions = step.findValue("html_instructions").asText();
			Step newStep = new Step(distance, duration, html_instructions);
			overallSteps.add(newStep);
		}
		return overallSteps;
	}

	/**
	 * Get google maps directions to location using transit (Prioritizes fewest
	 * transfers when possible)
	 * 
	 * @param origin     start location (formatted in google maps API expected
	 *                   configuration)
	 * @param destiation end location (formatted in google maps API expected
	 *                   configuration) // * @param mode mode must be "driving",
	 *                   "walking", "bicycling", or // * "transit"
	 * 
	 * @return directions if query was successful, null if not.
	 */
	public TransitDirections getDirectionsUsingTransit(String origin, String destination) {
		// Attempting to query with a get request similar to the following:
		// https://maps.googleapis.com/maps/api/directions/json?origin=35.909108,-79.048576&destination=35.788786,-78.672599&mode=transit&transit_routing_preference=fewer_transfers&key=[KEY_HERE]
		// Build the HTTPS API query for a get
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(DIRECTION_QUERY_BASE);
		stringBuilder.append("origin=");
		stringBuilder.append(origin);
		stringBuilder.append("&destination=");
		stringBuilder.append(destination);
		stringBuilder.append("&mode=transit");
		stringBuilder.append("&key=");
		stringBuilder.append(keys.getDirectionsAPIKey());
		// Get ready to read the returned JSON
		ObjectMapper mapper = new ObjectMapper();
		TransitDirections directions = new TransitDirections();
		try {
			// Query the google maps API and store it in the POJO
			// Taking a stroll in the forest.
			String url = stringBuilder.toString();
			url = formatSpacesWithinUrl(url);
			//System.out.println(url);
			JsonNode directionsJsonRoots = mapper.readTree(new URL(url));
			JsonNode route = directionsJsonRoots.findValue("routes");
			if (route == null) {
				return null;
			}
			String localCost = route.findValue("fare").findValue("text").asText();
			String duration = route.findValue("duration").findValue("text").asText();
			String distance = route.findValue("distance").findValue("text").asText();
			ArrayList<Step> steps = processTransitSteps(route.findValue("steps"));
			directions.setLocalCost(localCost);
			directions.setDuration(duration);
			directions.setTotalDistance(distance);
			directions.setSteps(steps);
			directions.setEndPoint(new ExactLocation(destination));
			directions.setStartPoint(new ExactLocation(origin));
			//System.out.println(localCost + " | " + duration + " | " + distance);
		} catch (JsonParseException e) {
			logger.log(Level.WARNING, "Google maps direction JSON parse", e);
		} catch (JsonMappingException e) {
			logger.log(Level.WARNING, "Google maps direction JSON mapping", e);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Google maps direction JSON IO", e);
		}
		//logger.log(Level.INFO, "Successful parse.");
		return directions;
	}

	/**
	 * Replace all spaces in url to %20 so server can understand it.
	 * 
	 * @param urlString url to format spaces in
	 * @return formatted url string.
	 */
	public String formatSpacesWithinUrl(String urlString) {
		if (urlString.contains(" ")) {
			urlString = urlString.replace(" ", "%20");
			return urlString;
		} else {
			return urlString;
		}
	}

	/**
	 * Get the nearest transit stop to a location.
	 * 
	 * @param location location where we need to find nearby transit lines.
	 */
	public ExactLocation getNearbyTransitStopLocation(ExactLocation location) {
		// Make a get api call to fetch the closest transit stop to an exact location.
		// https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=35.908818,-79.049571&radius=1500&type=bus_station&key=
		StringBuilder sb = new StringBuilder();
		sb.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
		sb.append(location.getLatitude());
		sb.append(',');
		sb.append(location.getLongitude());
		// Shortcut for now so we can get to minimum viable product (guessing that
		// closest transit stop is best to get to final destination but this is likely
		// not always the case)
		sb.append("&rankby=distance");
		// Transit station is a generic cover all for bus, subway, light rail, etc.
		sb.append("&type=transit_station&key=");
		sb.append(keys.getPlacesAPIKey());
		// Use jackson API to quickly parse JSON
		ObjectMapper mapper = new ObjectMapper();
		try {
			String url = sb.toString();
			url = formatSpacesWithinUrl(url);
			// System.out.println(url);
			JsonNode locationNode = mapper.readTree(new URL(url));
			if (locationNode == null) {
				// found nothing.
				return null;
			}
			// Find the first instance of lat and longitude
			double latitude = locationNode.findValue("lat").asDouble();
			double longitude = locationNode.findValue("lng").asDouble();
			System.out.println(latitude + " " + longitude);
			return new ExactLocation(latitude, longitude);
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, "Could not parse json for location find");
		}
		return null;
	}

	/**
	 * Get exact location from address or named place.
	 * 
	 * @param place place to get location of
	 * @return exact location of place (coords) if it exists in maps api database.
	 *         If not, return null.
	 */
	public ExactLocation getLocationFromPlace(String place) {
		// Attempt to query with a get request similar to the following
		// Adjust the fields argument in the future if we want more info about the
		// place.
		// https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=[LOCATION_HERE]&inputtype=textquery&fields=geometry&key=[KEY_HERE]
		StringBuilder sb = new StringBuilder();
		sb.append("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=");
		sb.append(place);
		sb.append("&inputtype=textquery&fields=geometry&key=");
		sb.append(keys.getPlacesAPIKey());
		// System.out.println(sb.toString());
		ObjectMapper mapper = new ObjectMapper();
		try {
			String url = sb.toString();
			url = formatSpacesWithinUrl(url);
			// System.err.println(url);
			// I'm sure there's a cleaner way to parse the Json so I'll clean this up later.
			// It works but there's not enough time right now to fix it so it's up to best
			// practices & more efficient.
			JsonNode locationNode = mapper.readTree(new URL(url));
			locationNode = locationNode.findValue("location");
			if (locationNode == null) {
				// found nothing.
				return null;
			}
			double latitude = locationNode.get("lat").asDouble();
			double longitude = locationNode.get("lng").asDouble();
			// System.out.println(latitude + " " + longitude);
			return new ExactLocation(latitude, longitude);
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, "Could not parse json for location find");
		}
		return null;
	}
}
