package solar.forecast.mqtt.server;

import java.util.List;
import java.util.Map;

import solar.forecast.mqtt.server.mqtt.MqttService;

public class App {
  public static void main(String[] args) {
    MqttService.startService();
  }

  public static String getSolarForecast(Map<String, String> latLonMap) {
    String forecastUrl = String.format("https://api.forecast.solar/estimate/%s/%s/37/0/1",
        latLonMap.get("lat"), latLonMap.get("lon"));
    System.out.println(forecastUrl);

    return HttpUtil.sendGetRequest(forecastUrl);
  }

  public static String buildGeocodeUrl(List<String> address) {
    return String.format(
        "https://geocode.maps.co/search?street=%s+%s&city=%s&state=%s&postalcode=%s&country=%s",
        address.get(0), address.get(1), address.get(2), address.get(3), address.get(4), address.get(5));
  }
}