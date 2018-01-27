package br.com.gustavo.pokemon.repository;


import br.com.gustavo.pokemon.model.Pokemon;
import br.com.gustavo.pokemon.util.CollectionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public class PokemonRepositoryImpl implements PokemonRepository {

    private MongoClient client;

    MongoDatabase db;

    private MongoCollection<Document> collection;

    public PokemonRepositoryImpl() {
        client = new MongoClient( "localhost" , 27017 );
        db = client.getDatabase("novaxs");
        collection = db.getCollection("pokemon");

    }

    private static Document Entity2Document(Pokemon pokemon){
        Gson gson = new Gson();

        String data = gson.toJson(pokemon);

        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<String, Object> map = g.fromJson(data, new TypeToken<Map<String, Object>>() {}.getType());

        Document doc = new Document(map);
        return doc;
    }

    public Pokemon save(Pokemon pokemon){
        collection.insertOne(Entity2Document(pokemon));
        return pokemon;
    }

    public Pokemon search(String numero){
        FindIterable<Document> cursor = collection.find(new BasicDBObject("num", numero));
        System.out.println(cursor.toString());
        return null;
    }

    @Override
    public Pokemon update(Pokemon pokemon) {
        MongoCursor<Document> cursor = collection.find(Filters.eq("num", pokemon.getNum())).iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();

                Map<String,Object> mapDoc = (Map)gson.fromJson(doc.toJson(), Map.class);
                mapDoc.put("name", pokemon.getName());
                mapDoc.put("img", pokemon.getImg());
                mapDoc.put("type", pokemon.getType());
                mapDoc.put("height", pokemon.getHeight());
                mapDoc.put("weight", pokemon.getWeight());
                mapDoc.put("candy", pokemon.getCandy());
                mapDoc.put("candy_count", pokemon.getCandy_count());
                mapDoc.put("egg", pokemon.getEgg());
                mapDoc.put("spawn_change", pokemon.getSpawn_chance());
                mapDoc.put("avg_spawns", pokemon.getAvg_spawns());
                mapDoc.put("spawn_time", pokemon.getSpawn_time());
                mapDoc.put("multiplayers", pokemon.getMultiplyers());
                mapDoc.put("weaknesses", pokemon.getWeaknesses());
                mapDoc.put("next_evolutions", pokemon.getNext_evolutions());

                Document docUp = new Document(mapDoc);
                collection.updateOne(doc, docUp);
                // collection.updateOne(doc, new Document("$set", new Document("updated", new Date())));

                // collection.updaparteOne(doc, new Document("updated", new Date()));
            }
            list();
        } finally {
            cursor.close();
        }
        return null;

    }

    @Override
    public List<Pokemon> list() {
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    @Override
    public void delete(String numero) {
        collection.deleteOne(Filters.eq("num",numero));
    }

    @Override
    public void importData(String pathFile) {
        CollectionManager.cleanAndFill(client.getDB("database"), pathFile, "pokemon");
//        datastore.ensureIndexes(Pokemon.class);
    }

}
