package com.endlosiot.mongocall.service;

import com.endlosiot.common.model.PageModelString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MongoCommunicationService {

    private final MongoClient mongoClient;

    @Autowired
    public MongoCommunicationService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public Document findDeviceParameterModel(String databaseName, String collectionName, String fieldName, Object value) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document query = new Document(fieldName, value);
        // Use a cursor to find the document
        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            if (cursor.hasNext()) {
                return cursor.next();
            }
        }
        return null; // Document not found
    }

    public void saveDeviceParameterModel(
            String databaseName,
            String collectionName,
            Document document
    ) throws JsonProcessingException {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.insertOne(document);
    }

    public void updateDeviceParameterModel(String databaseName, String collectionName, String fieldName, Object value, Document updateDocument) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document query = new Document(fieldName, value);
        // Use updateOne to update the document
        collection.updateOne(query, updateDocument);
    }

    public PageModelString fetchRecordsWithPagination(
            String databaseName,
            String collectionName,
            String deviceId,
            Integer start,
            Integer recordSize
    ) throws JsonProcessingException {
        return fetchAndProcessRecordsWithPagination(databaseName, collectionName, "key", deviceId, start, recordSize);
    }

    public PageModelString processRecordsWithPagination(
            String databaseName,
            String collectionName,
            String deviceId,
            Integer start,
            Integer recordSize
    ) throws JsonProcessingException {
        return fetchAndProcessRecordsWithPagination(databaseName, collectionName, "dId", deviceId, start, recordSize);
    }

    public PageModelString fetchAndProcessRecordsWithPagination(
            String databaseName,
            String collectionName,
            String keyName,
            String key,
            Integer start,
            Integer recordSize
    ) throws JsonProcessingException {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(collectionName);

        Bson query = Filters.eq(keyName, key);

        // Get the total count of documents that match the query
        long totalDocuments = collection.countDocuments(query);

        FindIterable<Document> findIterable = collection.find(query).skip(start).limit(recordSize);

        List<String> views = new ArrayList<>();

        for (Document document : findIterable) {
            String json = document.toJson(); // Serialize Document to JSON
            views.add(json);
        }

        return PageModelString.create(views, totalDocuments);
    }


}
