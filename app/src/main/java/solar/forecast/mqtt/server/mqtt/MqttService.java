package solar.forecast.mqtt.server.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttService {
  public static void startService() {
    String broker = "tcp://localhost:1883";
    String clientId = "Server";

    MqttClient client = MqttConnector.connect(broker, clientId);

    if (client != null) {
      try {
        client.subscribe("topic/server", (topic, message) -> {
          MqttMessageHandler.handleMessage(client, message);
        });

        // Wait for messages
        while (true) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      } catch (MqttException e) {
        e.printStackTrace();
      }
    }
  }
}
