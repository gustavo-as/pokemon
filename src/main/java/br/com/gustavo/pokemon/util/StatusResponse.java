package br.com.gustavo.pokemon.util;


public enum StatusResponse {
    SUCCESS ("Success"),
    ERROR ("Error");

    protected String status;

    StatusResponse(String status) {
        this.status = status;
    }
}
