package br.com.gustavo.pokemon.util;

import com.google.gson.JsonElement;
import lombok.Data;

@Data
public class ResponseHttpSpark {

    private StatusResponse status;
    private String message;
    private JsonElement data;

    public ResponseHttpSpark(StatusResponse success, String s) {

    }
}
