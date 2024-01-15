package org.example;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ElasticsearchClientRepository {
    private static ElasticsearchClientRepository instance;
    private final RestHighLevelClient client;

    private ElasticsearchClientRepository(String elasticsearchHost, int elasticsearchPort)
    {
        this.client = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort, "http")));
    }

    public static ElasticsearchClientRepository getInstance()
    {
        if (instance == null)
        {
            instance = new ElasticsearchClientRepository("localhost", 9200);
        }
        return instance;
    }

    public void closeConnection() throws IOException
    {
        if (client != null)
        {
            client.close();
        }
    }

    public void addDataToIndex (int id, String index, Map<String, Object> data) throws IOException
    {
        IndexRequest indexRequest = new IndexRequest(index).source("testProp", "test");
        //IndexRequest indexRequest = new IndexRequest(index).id(String.valueOf(id)).source(data);
        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("Data added to index ");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi de la requête à Elasticsearch : " + e.getMessage());
            throw e;
        }
        catch (Exception e) {
            System.err.println("Error adding data to index: " + index + " " + e.getMessage());
        }
    }

    public void sendFire(String fire) throws IOException
    {
        addDataToIndex(0, "test", Map.of(
                "testProp", "test"
        ));

//        String index = "fire";
//        int fireId = fire.getFireID();
//        String latitude = String.valueOf(fire.getLatitude());
//        String longitude = String.valueOf(fire.getLongitude());
//        int intensity = fire.getIntensity();
//        float temperature = fire.getTemperature();
//        float carbonDioxide = fire.getCarbonDioxide();
//        String currentDateTime = getCurrentDateTime();
//
//        // Ajout des données dans l'index
//        addDataToIndex(fireId, index, Map.of(
//                "fireId", fireId,
//                "fireLatitude", latitude,
//                "fireLongitude", longitude,
//                "fireIntensity", intensity,
//                "temperature", temperature,
//                "CO2", carbonDioxide,
//                "date", currentDateTime
//        ));
    }

    public String getCurrentDateTime()
    {
        LocalDate currentDate = LocalDate.now();
        // Formatte la date au format yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

//    public void sendFireStation(FireStationEntity fireStation) throws IOException
//    {
//        //"fireStationId" : {"type": "integer"},
//        //      "fireStationId" : {"type": "integer"},
//        //      "fireStationName": {"type": "text"},
//        //      "coordinate" : {"type": "geo_point"}
//
//        String index = "firestation";
//        String name = fireStation.getName();
//        // Formatte la coordonnée au format "latitude,longitude" : POINT (latitude longitude)
//        String coordinate = "POINT (" + fireStation.getLatitude() + " " + fireStation.getLongitude() + ")";
//        int fireStationId = fireStation.getId();
//
//        // Ajout des données dans l'index firestation
//        addDataToIndex(fireStationId, index, Map.of(
//                "fireStationId", fireStationId,
//                "name", name,
//                "coordinate", coordinate
//        ));
//    }
}
