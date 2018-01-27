package br.com.gustavo.pokemon.service;

import br.com.gustavo.pokemon.model.Pokemon;
import com.google.gson.Gson;

import static spark.Spark.post;


public class PokemonServiceImpl implements PokemonService{

    public PokemonService pokemonService = new PokemonServiceImpl();

    public void post1(){
        Gson gson = new Gson();
        post("/add", (req, res) -> {
            res.type("application/json");
            Pokemon pokemon = gson.fromJson(req.body(), Pokemon.class);
            return pokemonService.addPokemon(pokemon);
        }, gson ::toJson);
        };
    }
}