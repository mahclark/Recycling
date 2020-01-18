import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Location {
    private double longitude;
    private double latitude;

    private static final String GEOCODE_TOKEN = "893f62730e9a83";

    private static final String GEOCODE_BASE_URL = "https://eu1.locationiq.com/v1/search.php?key=%s&q=%s&format=json";
    private static final String PLACES_BASE_URL = "http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/suggest" + "?text=%s" + "&f=json";

    private String GeocodeSearchUrl;
    private String input;

    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(String input) {
        this.input = input;
        this.GeocodeSearchUrl = String.format(GEOCODE_BASE_URL, GEOCODE_TOKEN, input);

        try {
            URL url = new URL(GeocodeSearchUrl);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonArray rootArr = root.getAsJsonArray(); // convert json element to json array as the output gives an array of json objects
            JsonObject rootObj = rootArr.get(0).getAsJsonObject(); // get the first json from the array and convert that to json object

            latitude = rootObj.get("lat").getAsDouble();
            longitude = rootObj.get("lon").getAsDouble();
            //this.placeId = rootObj.get("place_id").getAsString();

        } catch (Exception e) {
            System.err.println("IOException: " + e.getMessage());
            latitude = 52.2107375;
            longitude = 0.09179849999999999;
            //this.placeId = "333245852306";
        }

    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getDistanceTo(Location loc) {
        return Math.sqrt(Math.pow(loc.longitude - longitude, 2) + Math.pow(loc.latitude - latitude, 2));
    }

    @Override
    public String toString() {
        return "input=" + input +
                ", longitude=" + longitude +
                ", latitude=" + latitude;
    }
}
