package com.nowcoder.community;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenTests {

    public static void main(String[] args) {


        Map<String, Object> claims = Map.of("username", "hello", "userId", 11);
        String secret = "secretPwdButItWasTooShortSoINeedToMakeItLonger";
//        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("secretPwdButItWasTooShortSoINeedToMakeItLonger"));

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret).compact();
        System.out.println(token);

        token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secret).compact();
        System.out.println(token);

        token = Jwts.builder().setClaims(claims).signWith(secretKey, SignatureAlgorithm.HS256).compact();
        System.out.println(token);

        Claims body = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        System.out.println(body);


        SecretKey secretKey1 = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        System.out.println(Encoders.BASE64.encode(secretKey1.getEncoded()));

        System.out.println(System.currentTimeMillis() + 3 * 60 * 1000L);

    }
}
