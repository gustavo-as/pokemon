package br.com.gustavo.pokemon.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by gustavo on 27/01/18.
 */
@Data
public class Pokemon {

    private Long id;

    private String num;

    private String name;

    private String img;

    private List<String> type;

    private String height;

    private String weight;

    private String candy;

    private Integer candy_count;

    private String egg;

    private Double spawn_chance;

    private Integer avg_spawns;

    private String spawn_time;

    private List<Double> multiplyers;

    private List<String> weaknesses;

    private Map<String, String> next_evolutions;





}
