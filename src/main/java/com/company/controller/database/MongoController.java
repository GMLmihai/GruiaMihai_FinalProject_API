package com.company.controller.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.set;

public class MongoController {


    private static final String usersCollectionName = "users";
    private static final String chatRoomsCollectionName = "chatrooms";
    private final MongoClient client;
    private final String dbName;

    public MongoController(MongoCredentials mongoCredentials) {
        this.client = new MongoClient(new MongoClientURI(mongoCredentials.getConnectionURI()));
        this.dbName = mongoCredentials.getDbName();
    }

    public MongoController() {
        MongoCredentials envCreds = getEnvCreds();
        this.client = new MongoClient(new MongoClientURI(envCreds.getConnectionURI()));
        this.dbName = envCreds.getDbName();

    }

    private MongoCredentials getEnvCreds() {
        Map<String, String> env = System.getenv();
        String mongoUser = env.get("MONGO_USER");
        String mongoPassword = env.get("MONGO_PASSWORD");
        String mongoCluster = env.get("MONGO_CLUSTER");
        String mongoDbName = env.get("MONGO_DB_NAME");
        return new MongoCredentials(mongoUser, mongoPassword, mongoCluster, mongoDbName);
    }


    public MongoDatabase getDatabase() {
        return client.getDatabase(dbName);
    }


    public MongoCollection<Document> getUsersCollection() {
        return getDatabase().getCollection(usersCollectionName);
    }

    public MongoCollection<Document> getChatRoomsCollection() {
        return getDatabase().getCollection(chatRoomsCollectionName);
    }


    public Document getUserWithUsername(String username) {
        MongoCollection<Document> usersCollection = getUsersCollection();
        Bson bsonFilter = Filters.eq("username", username);
        return usersCollection.find(bsonFilter).first();
    }

    public void addUser(String username, String password) {
        Document doc = new Document();
        doc.append("username", username);
        doc.append("password", password);
        getUsersCollection().insertOne(doc);
    }

    public Document getChatRoomWithName(String name) {
        MongoCollection<Document> chatRoomsCollection = getChatRoomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", name);
        return chatRoomsCollection.find(bsonFilter).first();
    }

    public ArrayList<Document> getChatroomWithName(String name) {
        MongoCollection<Document> chatRoomsCollection = getChatRoomsCollection();
        MongoCursor<Document> cursor = chatRoomsCollection.find(Filters.eq("chatroomName", name)).cursor();
        ArrayList<Document> result = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                Document currentDoc = cursor.next();
                result.add(currentDoc);
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return result;
    }

    public ArrayList<Document> getChatrooms() {
        MongoCollection<Document> chatroomsCollections = getChatRoomsCollection();
        MongoCursor<Document> cursor = chatroomsCollections.find().cursor();
        ArrayList<Document> result = new ArrayList<>();

        try {
            while (cursor.hasNext()) {
                Document currentDoc = cursor.next();
                result.add(currentDoc);
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return result;
    }

    public void addChatroom(String chatroomName, String password, String creatorName) {
        ArrayList<String> adminUsersNames = new ArrayList<>();
        adminUsersNames.add(creatorName);
        ArrayList<String> bannedUsers = new ArrayList<>();
        adminUsersNames.add(creatorName);

        Document doc = new Document();
        doc.append("chatroomName", chatroomName);
        doc.append("password", password);
        doc.append("admins", adminUsersNames);
        doc.append("chatroomName", chatroomName);
        doc.append("bannedUsers", bannedUsers);

        getChatRoomsCollection().insertOne(doc);
    }

    public void deleteChatroom(String chatroomName) {
        Document doc = new Document();
        doc.append("chatroomName", chatroomName);
        getChatRoomsCollection().deleteOne(doc);
    }

    public void addMessage(String chatroomName, String message, String username) {

        MongoCollection<Document> chatRoomsCollection = getChatRoomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);

        Document messageDoc = new Document("username", username)
                .append("message", message)
                .append("timestamp", new Date().toString());

        Bson updateOperation = push("messages", messageDoc);
        chatRoomsCollection.updateOne(bsonFilter, updateOperation);

    }

    public void updateChatroomName(String chatroomName, String newChatroomName) {

        MongoCollection<Document> chatRoomsCollection = getChatRoomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);
        Document doc = new Document();
        doc.append("chatroomName", newChatroomName);
        Bson updateOperation = set("chatroomName", newChatroomName);
        chatRoomsCollection.updateOne(bsonFilter, updateOperation);
    }

    public void addAdmin(String chatroomName, String username) {

        MongoCollection<Document> chatroomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroomName);

        Bson updateOperation = push("admins", username);
        chatroomsCollection.updateOne(bsonFilter, updateOperation);
    }

    public boolean isAdmin(String username, String chatroom) {

        MongoCollection<Document> chatRoomsCollection = getChatroomsCollection();
        Bson bsonFilter = Filters.eq("chatroomName", chatroom);

        try (MongoCursor<Document> cursor = chatRoomsCollection.find(bsonFilter).cursor()) {
            while (cursor.hasNext()) {
                Document currentDoc = cursor.next();
                List<Document> admins = new ArrayList<>();
                admins = currentDoc.getList("admins", Document.class, new ArrayList<>());
                for (Document admin : admins) {
                    if (admin.toString().equals(username)) {
                        return true;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return false;
    }
    public MongoCollection<Document> getChatroomsCollection() {
        return getDatabase().getCollection(chatRoomsCollectionName);
    }

}
