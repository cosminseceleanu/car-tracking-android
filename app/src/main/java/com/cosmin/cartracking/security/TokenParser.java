package com.cosmin.cartracking.security;


import android.util.Base64;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;


public class TokenParser {
    private static final String secret = "aaaaaa";

    public boolean isExpired(String token) {
        try {
            return getExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public Date getExpiration(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public long getUserId(String token) {
        return getClaimsFromToken(token).get("id", Integer.class);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private String getKey() {
        byte [] keyBytes = Base64.encode(secret.getBytes(), Base64.DEFAULT);

        return new String(keyBytes);
    }
}
