package solar.forecast.mqtt.server.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttConnector {
  public static MqttClient connect(String broker, String clientId) {
    MemoryPersistence persistence = new MemoryPersistence();

    try {
      MqttClient client = new MqttClient(broker, clientId, persistence);

      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);

      System.out.println("Connecting to broker: " + broker);
      client.connect(connOpts);
      System.out.println("Connected");
      System.out.println();

      return client;
    } catch (MqttException me) {
      me.printStackTrace();
      return null;
    }
  }
}
