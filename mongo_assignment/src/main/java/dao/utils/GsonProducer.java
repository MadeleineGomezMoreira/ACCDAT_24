package dao.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.utils.adapters.LocalDateAdapter;
import dao.utils.adapters.LocalDateTimeAdapter;
import dao.utils.adapters.ObjectIdAdapter;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import model.mongo.MedicalRecord;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GsonProducer {

    @Produces
    @Singleton
    public Gson produceGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass() == MedicalRecord.class && f.getName().equals("patientId");
                    }

                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

}
