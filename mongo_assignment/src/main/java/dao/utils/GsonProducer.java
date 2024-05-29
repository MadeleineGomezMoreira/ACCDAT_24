package dao.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.utils.adapters.LocalDateAdapter;
import dao.utils.adapters.LocalDateTimeAdapter;
import dao.utils.adapters.ObjectIdAdapter;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
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
                .create();
    }

}
