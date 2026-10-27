package com.eloyroldan.tp_final_alojamiento.backend.DTO;

/// Lo unico que devuelve un login exitoso: el JWT a usar en el header Authorization.
//Lombok
public class LoginResponse {
    private final String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
