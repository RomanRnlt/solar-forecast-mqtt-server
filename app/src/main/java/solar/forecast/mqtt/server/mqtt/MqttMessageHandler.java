package solar.forecast.mqtt.server.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import solar.forecast.mqtt.server.App;
import solar.forecast.mqtt.server.HttpUtil;
import solar.forecast.mqtt.server.JsonParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MqttMessageHandler {
  public static void handleMessage(MqttClient client, MqttMessage message) {
    String receivedMessage = new String(message.getPayload());
    System.out.println("Received message from client: " + receivedMessage);

    // API access
    List<String> address = Arrays.asList(receivedMessage.split("&"));
    String geocodeUrl = App.buildGeocodeUrl(address);
    String geocodeResponse = HttpUtil.sendGetRequest(geocodeUrl);
    Map<String, String> latLonMap = JsonParser.parseGeocodeResponse(geocodeResponse);

    // Respond to the client
    String responseMessage = App.getSolarForecast(latLonMap);
    MqttMessage responseMqttMessage = new MqttMessage(responseMessage.getBytes());
    responseMqttMessage.setQos(2);

    try {
      client.publish("topic/client", responseMqttMessage);
      System.out.println("Published response message: " + responseMessage);
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }
}
