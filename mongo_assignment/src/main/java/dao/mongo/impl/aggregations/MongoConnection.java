package dao.mongo.impl.aggregations;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.Constants;
import common.config.Config;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;

@Log4j2
@Singleton
public class MongoConnection {

    private Config config;
    private MongoClient mongo;
    private MongoDatabase db;
    private MongoCollection<Document> patients;
    private MongoCollection<Document> doctors;

    @Inject
    public MongoConnection(Config config) {
        this.config = config;
        this.mongo = MongoClients.create(config.getProperty(Constants.MONGO_CLIENT));
        this.db = mongo.getDatabase(config.getProperty(Constants.MONGO_DB));
        this.patients = db.getCollection(config.getProperty(Constants.PATIENTS_COLLECTION));
        this.doctors = db.getCollection(config.getProperty(Constants.DOCTORS_COLLECTION));
    }

    public MongoCollection<Document> getPatients() {
        return patients;
    }
    public MongoCollection<Document> getDoctors() {
        return doctors;
    }
}
