package br.com.gustavo.pokemon.init;

import br.com.gustavo.pokemon.model.Pokemon;
import br.com.gustavo.pokemon.service.PokemonService;
import br.com.gustavo.pokemon.service.PokemonServiceImpl;
import br.com.gustavo.pokemon.util.ResponseHttpSpark;
import br.com.gustavo.pokemon.util.StatusResponse;
import com.google.gson.Gson;

import static spark.Spark.*;


public class Api {

    public static final String ROUTE_POKEMON = "/pokemon";
    public static final String CONTENT_TYPE = "application/json";

    private static Gson gson = new Gson();
    private static PokemonService pokemonService = new PokemonServiceImpl();

    public static void main(String[] args) {



        post(ROUTE_POKEMON, (req, res) -> {
            res.type(CONTENT_TYPE);
            Pokemon pokemon = gson.fromJson(req.body(), Pokemon.class);
            return pokemonService.save(pokemon);
        }, gson::toJson);

        get(ROUTE_POKEMON+"/:num", (req, res) -> {
            res.type(CONTENT_TYPE);
            return pokemonService.search(req.params("num"));
        }, gson :: toJson);

        get(ROUTE_POKEMON, (req, res) -> {
            res.type(CONTENT_TYPE);
            return pokemonService.list();
        }, gson ::toJson);

        put(ROUTE_POKEMON+"/:num", (req, res) -> {
            res.type(CONTENT_TYPE);
            String par = req.params("num");
            return pokemonService.update(par);
        }, gson ::toJson);

        delete(ROUTE_POKEMON+"/:id", (req, res) -> {
            res.type(CONTENT_TYPE);
            pokemonService.delete(req.params(":id"));
            return new Gson().toJson(
                    new ResponseHttpSpark(StatusResponse.SUCCESS, "Pokemon deleted."));
        });
    }

}