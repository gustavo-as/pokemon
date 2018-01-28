package br.com.gustavo.pokemon.generic;

import br.com.gustavo.pokemon.model.Pokemon;
import spark.Request;

import java.util.List;

public interface GenericInterface{

    Pokemon save(Pokemon pokemon) throws Exception;

    Pokemon search(String numero) throws Exception;

    Pokemon update(Pokemon pokemon) throws Exception;

    List<Pokemon> list() throws Exception;

    void delete(String numero) throws Exception;

    void importData(String jsonString) throws Exception;

    List<Pokemon> paginable(Request req) throws Exception;

    List<Pokemon> searchByType(Request req) throws Exception;

}
