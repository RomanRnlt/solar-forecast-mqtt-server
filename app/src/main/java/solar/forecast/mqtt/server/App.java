package solar.forecast.mqtt.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    static String houseNumber, street, city, postalcode, state, country;
    static String geocodeResponse, forecastResponse;
    static Map<String, String> latLonMap = new HashMap<>();

    public static void getLatLon() {
        houseNumber = "12";
        street = "Lachnerstraße";
        city = "Karlsruhe";
        postalcode = "76131";
        state = "Baden-Württemberg";
        country = "DE";

        String geocodeUrl = String.format(
                "https://geocode.maps.co/search?street=%s+%s&city=%s&state=%s&postalcode=%s&country=%s", houseNumber,
                street, city, state, postalcode, country);

        geocodeResponse = sendGetRequest(geocodeUrl);
    }

    public static void getSolarForecast() {
        String forecastUrl = String.format("https://api.forecast.solar/estimate/%s/%s/37/0/1", latLonMap.get("lat"),
                latLonMap.get("lon"));
        System.out.println(forecastUrl);

        forecastResponse = sendGetRequest(forecastUrl);

        System.out.println(forecastResponse);
    }

    public static void mqtt() {
        String broker = "tcp://mqtt.eclipse.org:1883"; // MQTT-Broker-Adresse
        String clientId = "Server1";
        try {
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            client.connect(connOpts);

            // Hier auf Adresse-Topic lauschen
            client.subscribe("adresse-topic", (topic, message) -> {
                // Adresse aus der MQTT-Nachricht extrahieren
                String adresse = new String(message.getPayload());

                // GET-Request an Drittanbieter API senden und Response verarbeiten
                // ...

                // API-Response via MQTT an Client senden
                MqttMessage responseMessage = new MqttMessage("API-Response".getBytes());
                client.publish("api-response-topic", responseMessage);
            });

            // Hier weiteren Code für andere Operationen oder Endlosschleife fürs Lauschen

            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // new App().getLatLon();
        // parseJson();
        // getSolarForecast();
    }

    public static String sendGetRequest(String urlString) {
        try {
            URL urlObject = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();

            StringBuilder response = new StringBuilder();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
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

    public static void parseJson() {
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

    }

}