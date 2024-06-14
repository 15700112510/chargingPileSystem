package com.example.chargingPileSystem.util;

import com.example.chargingPileSystem.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;
import java.util.Objects;

public class JwtUtil {

    // 生成JWT
    public static String creatToken(Map<String, Object> claims, Integer ttlMillis) {

        JwtBuilder jwtBuilder = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, JwtConstant.SECRET);

        //设置过期时间
        if (Objects.nonNull(ttlMillis)) {
            jwtBuilder.setExpiration(new java.util.Date(System.currentTimeMillis() + ttlMillis)).compact();
        }

        return jwtBuilder.compact();
    }

    // 解析JWT
    public static Claims parseJwt(String token) {
        return Jwts.parser().setSigningKey(JwtConstant.SECRET).parseClaimsJws(token).getBody();
    }
}
