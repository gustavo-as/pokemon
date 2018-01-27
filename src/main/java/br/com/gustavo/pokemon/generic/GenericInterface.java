package br.com.gustavo.pokemon.generic;

import br.com.gustavo.pokemon.model.Pokemon;
import org.mongodb.morphia.Key;

import java.util.List;

public interface GenericInterface{

    Key<Pokemon> save(Pokemon pokemon) throws Exception;

    Pokemon search(String numero) throws Exception;

    Pokemon update(String numero) throws Exception;

    List<Pokemon> list() throws Exception;

    void delete(String numero) throws Exception;

    void importData(String pathFile) throws Exception;

}
