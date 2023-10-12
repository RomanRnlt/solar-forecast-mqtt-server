package solar.forecast.mqtt.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;

public class JsonHandler {
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

      System.out.println("Breitengrad: " + latLonMap.get("lat") + ", Längengrad: " + latLonMap.get("lon"));
      System.out.println();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return latLonMap;
  }

  public static void logJsonToFile(String jsonDataset) {
    String filePath = "/Users/roman/Downloads/solar-forecast_log.json";
    try {
      // Create the file path
      Path path = Paths.get(filePath);

      // Check if the file exists, create it if not
      if (!Files.exists(path)) {
        Files.createFile(path);
      }

      // Parse the JSON string to a formatted JSON string
      ObjectMapper objectMapper = new ObjectMapper();
      Object json = objectMapper.readValue(jsonDataset, Object.class);
      String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

      // Open the file in append mode (true as the second argument)
      FileWriter fileWriter = new FileWriter(filePath, true);
      BufferedWriter writer = new BufferedWriter(fileWriter);

      // Write the formatted JSON dataset to the file with a newline character
      writer.write(formattedJson);
      writer.newLine();

      // Close the writer
      writer.close();

      System.out.println("Forecast successfully logged to the file.");
      System.out.println();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
