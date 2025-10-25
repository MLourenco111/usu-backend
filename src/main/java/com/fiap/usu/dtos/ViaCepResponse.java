package com.fiap.usu.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ViaCepResponse(
        @JsonProperty("cep") String cep,
        @JsonProperty("logradouro") String street,
        @JsonProperty("bairro") String neighborhood,
        @JsonProperty("localidade") String city,
        @JsonProperty("uf") String state,
        @JsonProperty("erro") Boolean error) {
}
