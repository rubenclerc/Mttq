package org.example;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient.Mqtt5Publishes;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

import java.io.IOException;

/**
 * Subscribes for data from MQTT.
 * @author Petri Kannisto
 */
public class MyMqttSubscriber extends MyMqttClient
{
    public MyMqttSubscriber()
    {
        super("ENTER to start listening to messages");
    }

    @Override
    public void doYourThing(Mqtt5Client client, String userInput)
    {
        Mqtt5BlockingClient blockingClient = client.toBlocking();
        String topic = getTopic();

        try (Mqtt5Publishes publishes = blockingClient.publishes(MqttGlobalPublishFilter.ALL))
        {
            blockingClient.
                    subscribeWith().
                    topicFilter(topic).
                    send();
            writeLine("Subscribed to topic '" + topic + "'");

            while (true)
            {
                try
                {
                    Mqtt5Publish publishMessage = publishes.receive();
                    writeLine("Message received:");
                    String received = new String(publishMessage.getPayloadAsBytes());
                    writeLine(received);

                    ElasticsearchClientRepository elasticsearchClient = ElasticsearchClientRepository.getInstance();
                    addFireInElasticsearch(received, elasticsearchClient);
                    elasticsearchClient.closeConnection();
                }
                catch (InterruptedException | IOException e)
                {
                    break; // Quit; likely never reached!
                }
            }

            // In any actual application, the client should unsubscribe before exit.
            // It's unclear if this happens in this application when CTRL+C is used to quit.
            blockingClient.unsubscribeWith().topicFilter(topic).send();
            writeLine("Unsubscribed from topic '" + topic + "'");
        }
    }

    public void addFireInElasticsearch(String fire, ElasticsearchClientRepository elasticsearchClient) throws IOException
    {
        // Ajout d'un feu dans la base de données elasticsearch : index : fire/
        elasticsearchClient.sendFire(fire);
    }
}