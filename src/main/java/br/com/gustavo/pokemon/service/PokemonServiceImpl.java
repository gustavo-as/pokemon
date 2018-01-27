package br.com.gustavo.pokemon.service;

import br.com.gustavo.pokemon.exception.BusinessException;
import br.com.gustavo.pokemon.model.Pokemon;
import br.com.gustavo.pokemon.repository.PokemonRepository;
import br.com.gustavo.pokemon.repository.PokemonRepositoryImpl;
import org.mongodb.morphia.Key;

import java.util.List;

public class PokemonServiceImpl implements PokemonService {

    private PokemonRepository pokemonRepository = new PokemonRepositoryImpl();

    @Override
    public Key<Pokemon> save(Pokemon pokemon) throws Exception {
        try {
            return pokemonRepository.save(pokemon);
        } catch (BusinessException be) {
            throw new Exception("Error Saving Pokemon");
        }
    }

    @Override
    public Pokemon search(String numero) throws Exception {
        try {
            return pokemonRepository.search(numero);
        } catch (BusinessException be) {
            throw new Exception("Error search Pokemon");
        }
    }

    @Override
    public Pokemon update(String numero) throws Exception {
        try {
            return pokemonRepository.update(numero);
        } catch (BusinessException be) {
            throw new Exception("Error update Pokemon");
        }
    }

    @Override
    public List<Pokemon> list() throws Exception {
        try {
            return pokemonRepository.list();
        } catch (BusinessException be) {
            throw new Exception("Error on list Pokemon");
        }
    }

    @Override
    public void delete(String numero) throws Exception {
        try {
            pokemonRepository.delete(numero);
        } catch (BusinessException be) {
            throw new Exception("Error delete Pokemon");
        }
    }

    @Override
    public void importData(String pathFile) throws Exception {
        try {
            pokemonRepository.importData(pathFile);
        } catch (BusinessException be) {
            throw new Exception("Error importing data " + pathFile);
        }
    }


}
