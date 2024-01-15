package org.example;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Abstract base class for MQTT clients.
 * @author Petri Kannisto
 */
public abstract class MyMqttClient
{
    private final String m_topic = "fires"; // The topic is hard coded
    private final String m_elaboration;
    private Mqtt5Client client;

    /**
     * Constructor.
     * @param elaboration The hint to be shown to the user about what to give as the input.
     */
    protected MyMqttClient(String elaboration)
    {
        m_elaboration = elaboration;
    }

    protected String getTopic()
    {
        return m_topic;
    }

    public void createClient() {
    	client = Mqtt5Client.builder().build();
        Mqtt5BlockingClient blockingClient = client.toBlocking();
        blockingClient.connect();
    }

    public Mqtt5Client getClient() {
        return client;
    }

    /**
     * Runs the client.
     */
    public void run()
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            // Create client
            Mqtt5Client client = Mqtt5Client.builder().build();

            // Get the "blocking" client API
            Mqtt5BlockingClient blockingClient = client.toBlocking();

            write("Connecting... ");
            blockingClient.connect();
            writeLine("connected!");

            try
            {
                // Repeat until the user wants to quit
                while (true)
                {
                    writeLine("Please give input, either:");
                    writeLine("- q to Quit");
                    writeLine("- " + m_elaboration);
                    write(" > ");
                    String userInput = reader.readLine().trim();

                    if (userInput.toLowerCase().equals("q"))
                    {
                        writeLine("Quitting.");
                        break;
                    }

                    // Do the client-specific thing
                    doYourThing(client, userInput);
                }
            }
            finally
            {
                blockingClient.disconnect();
            }

        }
        catch (Exception e)
        {
            System.err.println(e.toString());
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (Exception e)
            {}
        }
    }

    /**
     * Implements the client-specific workflow.
     * @param client MQTT client.
     * @param userInput The user input received.
     */
    public abstract void doYourThing(Mqtt5Client client, String userInput);

    /**
     * Writes a line.
     * @param msg Line to be written.
     */
    protected void writeLine(String msg)
    {
        System.out.println(msg);
    }

    /**
     * Writes a message with no line break at the end.
     * @param msg Message to be written.
     */
    protected void write(String msg)
    {
        System.out.print(msg);
    }
}
