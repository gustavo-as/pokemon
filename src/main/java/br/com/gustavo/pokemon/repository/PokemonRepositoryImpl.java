package br.com.gustavo.pokemon.repository;


import br.com.gustavo.pokemon.model.Pokemon;
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
import org.bson.conversions.Bson;
import spark.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PokemonRepositoryImpl implements PokemonRepository {

    private static int LIMIT = 20;

    private MongoClient client;

    private MongoDatabase db;

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
        Document doc = cursor.first();
        Gson gson = new Gson();
        return gson.fromJson(doc.toJson(), Pokemon.class);
    }

    @Override
    public Pokemon update(Pokemon pokemon) {
        MongoCursor<Document> cursor = collection.find(Filters.eq("num", pokemon.getNum())).iterator();
        try {

            Bson filter = new Document("num", pokemon.getNum());
            Bson newValue = new Document("name", pokemon.getName())
                    .append("img", pokemon.getImg())
                    .append("type", pokemon.getType())
                    .append("height", pokemon.getHeight())
                    .append("weight", pokemon.getWeight())
                    .append("candy", pokemon.getCandy())
                    .append("candy_count", pokemon.getCandy_count())
                    .append("egg", pokemon.getEgg())
                    .append("spawn_change", pokemon.getSpawn_chance())
                    .append("avg_spawns", pokemon.getAvg_spawns())
                    .append("spawn_time", pokemon.getSpawn_time())
                    .append("multiplyers", pokemon.getMultiplyers())
                    .append("weaknesses", pokemon.getWeaknesses())
                    .append("next_evolutions", pokemon.getNext_evolutions());

            Bson updateOperationDocument = new Document("$set", newValue);

            collection.updateOne(filter, updateOperationDocument);

            return pokemon;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Pokemon> list() {
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            List<Pokemon> pokemons = new ArrayList<Pokemon>();
            while (cursor.hasNext()) {
                Gson gson = new Gson();
                pokemons.add(gson.fromJson(cursor.next().toJson(), Pokemon.class));
            }
            return pokemons;
        } finally {
            cursor.close();
        }
    }

    public Long countData() {
       return collection.count();
    }

    @Override
    public void delete(String numero) {
        collection.deleteOne(Filters.eq("num",numero));
    }

    @Override
    public void importData(String filePath) throws IOException {
        Runtime run = Runtime.getRuntime();
        run.exec("mongoimport --db novaxs --collection pokemon --file " + filePath);
    }

    @Override
    public List<Pokemon> paginable(Request req) {
        List<Pokemon> pokemons = new ArrayList<>();
        MongoCursor<Document> cursor = null;
        if(countData() < LIMIT){
            cursor = collection.find().limit(Integer.parseInt(req.params("limit"))).iterator();
        } else {
            cursor = collection.find().skip(Integer.parseInt(req.params("page"))).limit(Integer.parseInt(req.params("limit"))).iterator();
        }

        while (cursor.hasNext()) {
            Gson gson = new Gson();
            pokemons.add(gson.fromJson(cursor.next().toJson(), Pokemon.class));
        }
        return pokemons;
    }

    @Override
    public List<Pokemon> searchByType(Request req) {
        List<Pokemon> pokemons = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find(Filters.eq("type", req.params("type"))).limit(Integer.parseInt(req.params("limit"))).iterator();

        while (cursor.hasNext()) {
            Gson gson = new Gson();
            pokemons.add(gson.fromJson(cursor.next().toJson(), Pokemon.class));
        }
        return pokemons;
    }

}