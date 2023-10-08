package solar.forecast.mqtt.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
  public static String sendGetRequest(String urlString) {
    try {
      URL urlObject = new URL(urlString);
      HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

      connection.setRequestMethod("GET");
      connection.setRequestProperty("Accept", "application/json");

      int responseCode = connection.getResponseCode();

      StringBuilder response = new StringBuilder();

      if (responseCode == HttpURLConnection.HTTP_OK) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
        }
      } else {
        System.out.println("GET request failed. Response Code: " + responseCode);
      }
      connection.disconnect();

      return response.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
