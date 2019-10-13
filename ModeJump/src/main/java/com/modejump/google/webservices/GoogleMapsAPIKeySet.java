package com.modejump.google.webservices;
/**
 * POJO to store all of the restricted API keys for google maps API.
 * @author Alex
 *
 */
public class GoogleMapsAPIKeySet {
	/**Directions API key*/
	private String directionsAPIKey;
	/**Geocoding API Key*/
	private String geocodingAPIKey;
	/**API key for map*/
	private String mapsAPIKey;
	
	private String placesAPIKey;

	public GoogleMapsAPIKeySet(String directionsAPIKey, String geocodingAPIKey, String mapsAPIKey, String placesAPIKey) {
		this.directionsAPIKey = directionsAPIKey;
		this.geocodingAPIKey = geocodingAPIKey;
		this.mapsAPIKey = mapsAPIKey;
		this.placesAPIKey = placesAPIKey;
	}
	
	
	public String getDirectionsAPIKey() {
		return directionsAPIKey;
	}

	public String getPlacesAPIKey() {
		return placesAPIKey;
	}


	public String getGeocodingAPIKey() {
		return geocodingAPIKey;
	}

	public String getMapsAPIKey() {
		return mapsAPIKey;
	}
}
