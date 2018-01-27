package br.com.gustavo.pokemon.repository;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class ImportData {

    MongoClient client = new MongoClient("localhost", 27017); //connect to mongodb
    Datastore datastore = new Morphia().createDatastore(client, "pokemon"); //select shop collection

    public ImportData() {



    }
}
