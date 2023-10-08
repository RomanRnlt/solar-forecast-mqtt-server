package solar.forecast.mqtt.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JsonParser {
  public static Map<String, String> parseGeocodeResponse(String geocodeResponse) {
    Map<String, String> latLonMap = new HashMap<>();

    ObjectMapper objectMapper = new ObjectMapper();

    try {
      JsonNode jsonNode = objectMapper.readTree(geocodeResponse);

      for (JsonNode node : jsonNode) {
        latLonMap.put("lat", node.get("lat").asText());
        latLonMap.put("lon", node.get("lon").asText());
        break;
      }

      System.out.println("Lat: " + latLonMap.get("lat") + ", Lon: " + latLonMap.get("lon"));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return latLonMap;
  }
}
