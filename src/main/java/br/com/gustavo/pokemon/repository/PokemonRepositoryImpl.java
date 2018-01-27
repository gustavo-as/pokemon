package br.com.gustavo.pokemon.repository;

import br.com.gustavo.pokemon.model.Pokemon;
import br.com.gustavo.pokemon.util.CollectionManager;
import com.mongodb.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;

public class PokemonRepositoryImpl implements PokemonRepository {

    private MongoClient client;

    private Datastore datastore;

    private DBCollection collection;

    public PokemonRepositoryImpl() {
        client = new MongoClient("localhost", 27017);
        datastore = new Morphia().createDatastore(client, "novobanco");
        DBCollection dt = datastore.getCollection(Pokemon.class);

        DB database = getDatabase();
        collection = database.getCollection("pokemon");


    }

    public static DB getDatabase() {
        Mongo mongo = new Mongo("localhost", 27017);
        DB database = mongo.getDB("novobanco");
        return database;
    }

    public Key<Pokemon> save(Pokemon pokemon){
        return datastore.save(pokemon);
    }

    public Pokemon search(String numero){
        return datastore.find(Pokemon.class).field("num").equal(numero).get();
    }

    @Override
    public Pokemon update(String num) {

        Query query = datastore.createQuery(Pokemon.class).field("num").equal(num);

        Pokemon pokemon = search(num);

        UpdateOperations<Pokemon> op;
        op = datastore.createUpdateOperations(Pokemon.class)
                .set("num", pokemon.getNum())
                .set("name", pokemon.getName())
                .set("img", pokemon.getImg())
                .set("height", pokemon.getHeight())
                .set("weight", pokemon.getWeight())
                .set("candy", pokemon.getCandy())
                .set("candy_count", pokemon.getCandy_count())
                .set("egg", pokemon.getEgg())
                .set("spawn_chance", pokemon.getSpawn_chance())
                .set("avg_spawns", pokemon.getAvg_spawns())
                .set("spawn_time", pokemon.getSpawn_time());
        datastore.update(query, op);

        return search(pokemon.getNum());

    }

    @Override
    public List<Pokemon> list() {
        DBCursor cursor = collection.find();
        cursor.toArray();
        while(cursor.hasNext()) {
            System.out.println("Record - " + cursor.next());
        }

        return datastore.find(Pokemon.class).asList();
    }

    @Override
    public void delete(String numero) {
        datastore.delete(search(numero));
    }

    @Override
    public void importData(String pathFile) {
        CollectionManager.cleanAndFill(client.getDB("database"), pathFile, "pokemon");
        datastore.ensureIndexes(Pokemon.class);
    }

}
