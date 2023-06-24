package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtil {
    /**
     * 过期时间
     */
    private static final long EXPIRE_TIME =  604800 * 1000;
    /**
     * token秘钥
     */
    private static final String TOKEN_SECRET = "c369a1e4-43ee-4e1e-b130-2b952f1ba9ad";

    /**
     * 签名方法
     * @Param userName  userName
     * @Param role  role
     * @return String role
     */
    public static String sign(String userName, String role) {

        try {
            // 过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 秘钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // headers
            Map<String, Object> herders = new HashMap<>(2);
            herders.put("typ", "JWT");
            herders.put("alg", "HS256");

            return JWT.create()
                    .withHeader(herders)
                    .withClaim("userName", userName)
                    .withClaim("role", role)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception exception) {
            exception.printStackTrace();

            return exception.getMessage();
        }
    }

    /**
     * token 校验方法
     * @Param userName  userName
     * @return String role
     */
    public static String verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT verify = verifier.verify(token);
            String userid= verify.getClaim("userName").asString();


            return userid;
        } catch (Exception exception) {

            exception.printStackTrace();
            return null;
        }
    }
}
