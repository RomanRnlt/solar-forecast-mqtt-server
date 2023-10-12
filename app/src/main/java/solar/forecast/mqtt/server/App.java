package solar.forecast.mqtt.server;

import java.util.List;
import java.util.Map;

import solar.forecast.mqtt.server.mqtt.MqttService;

public class App {
  public static void main(String[] args) {
    MqttService.startService();
  }

  public static String getSolarForecast(Map<String, String> latLonMap, String kwp) {
    String forecastUrl = String.format("https://api.forecast.solar/estimate/%s/%s/37/0/%s",
        latLonMap.get("lat"), latLonMap.get("lon"), kwp);
    System.out.println("API-Anfrage: " + forecastUrl);
    System.out.println();

    return HttpUtil.sendGetRequest(forecastUrl);
  }

  public static String buildGeocodeUrl(List<String> address) {
    String geoUrl = String.format(
        "https://geocode.maps.co/search?street=%s+%s&city=%s&state=%s&postalcode=%s&country=%s",
        address.get(0), address.get(1), address.get(2), address.get(3), address.get(4), address.get(5));

    System.out.println("API-Anfrage: " + geoUrl);

    return geoUrl;
  }
}