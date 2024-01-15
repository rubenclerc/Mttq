package org.example;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

/**
 * Publishes data to MQTT.
 * @author Petri Kannisto
 */
public class MyMqttPublisher extends MyMqttClient
{
    public MyMqttPublisher()
    {
        super("Message to publish");
    }

    @Override
    public void doYourThing(Mqtt5Client client, String userInput)
    {
        String topic = getTopic();

        write("Now publishing to topic '" + topic + "'... ");

        client.
                toBlocking().
                publishWith().
                topic(topic).
                payload(userInput.getBytes()).
                send();

        writeLine("published!");
    }
}