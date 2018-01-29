package br.com.gustavo.pokemon.init;

import br.com.gustavo.pokemon.model.Pokemon;
import br.com.gustavo.pokemon.service.PokemonService;
import br.com.gustavo.pokemon.service.PokemonServiceImpl;
import br.com.gustavo.pokemon.util.ResponseHttpSpark;
import br.com.gustavo.pokemon.util.StatusResponse;
import com.google.gson.Gson;

import java.io.File;

import static spark.Spark.*;


public class Api {

    public static final String FILE_JSON = "pokedex.json";

    public static final String ROUTE_POKEMON = "/pokemon";
    public static final String CONTENT_TYPE = "application/json";

    private static Gson gson = new Gson();
    private static PokemonService pokemonService = new PokemonServiceImpl();

    public static void main(String[] args) throws Exception {

        try {
            Api api = new Api();
            ClassLoader classLoader = api.getClass().getClassLoader();
            File file = new File(classLoader.getResource(FILE_JSON).getFile());
            pokemonService.importData(file.getPath());

        } catch (Exception e) {
            System.out.println("Error on import json file.");
            throw e;            
        }

        post(ROUTE_POKEMON, (req, res) -> {
            res.type(CONTENT_TYPE);
            Pokemon pokemon = gson.fromJson(req.body(), Pokemon.class);
            res.status(201);
            return pokemonService.save(pokemon);
        }, gson::toJson);

        get(ROUTE_POKEMON+"/:num", (req, res) -> {
            res.type(CONTENT_TYPE);            
            Pokemon pokemon = pokemonService.search(req.params("num"));
            if(pokemon == null){
                res.status(404);
                return null;
            }
            res.status(200);
            return pokemon;
        }, gson :: toJson);


        get(ROUTE_POKEMON+"/:page/:limit", (req, res) -> {
            res.type(CONTENT_TYPE);
            res.status(200);
            return pokemonService.paginable(req);
        }, gson :: toJson);

        get(ROUTE_POKEMON+"s/:type/:limit", (req, res) -> {
            res.type(CONTENT_TYPE);
            res.status(200);
            return pokemonService.searchByType(req);
        }, gson :: toJson);

        get(ROUTE_POKEMON, (req, res) -> {
            res.type(CONTENT_TYPE);
            res.status(200);
            return pokemonService.list();
        }, gson ::toJson);

        put(ROUTE_POKEMON, (req, res) -> {
            res.type(CONTENT_TYPE);
            Pokemon pokemon = gson.fromJson(req.body(), Pokemon.class);
            res.status(200);
            return pokemonService.update(pokemon);
        }, gson ::toJson);

        delete(ROUTE_POKEMON+"/:id", (req, res) -> {
            res.type(CONTENT_TYPE);
            pokemonService.delete(req.params(":id"));
            return new Gson().toJson(
                    new ResponseHttpSpark(StatusResponse.SUCCESS, "Pokemon deleted."));
        });
    }

}