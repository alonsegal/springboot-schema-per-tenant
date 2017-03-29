package com.alonsegal.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;

/**
 * Created by stephan on 20.03.16.
 */
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;
    private final UserDetails user;

    public JwtAuthenticationResponse(String token, UserDetails user) {
        this.token = token;
        this.user = user;
    }
}
