package com.teamE.security.jwt;

import com.teamE.users.UserTokenInformation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil implements Serializable {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.RS512;
    private static final long JWT_TOKEN_VALIDITY = 30 * 24 * 60 * 60;
    private static final String DEVICE_KEY = "device";
    private static final String ROLE_KEY = "role";
    private static final String NAME_KEY = "name";
    private static final String SCOPE_KEY = "scope";
    private static final String ENCRYPTION = "RSA";

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public JwtUtil(@Value("${security.jwt.private}") String privateKeyString, @Value("${security.jwt.public}") String publicKeyString) {
        try {
            KeyFactory kf = KeyFactory.getInstance(ENCRYPTION);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));

            privateKey = kf.generatePrivate(privateKeySpec);
            publicKey = kf.generatePublic(publicKeySpec);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    String getDeviceInformationFromToken(String token) {
        return getStringClaimFromToken(token, DEVICE_KEY);
    }

    String getUserRolesFromToken(String token) {
        return getStringClaimFromToken(token, ROLE_KEY);
    }

    boolean isTokenValid(String token, UserDetails userDetails, String deviceInfo) {
        boolean isTokenNotExpired = isTokenExpired(getExpirationDateFromToken(token));
        boolean doesDeviceInfoMatch = doesDeviceInfoMatch(getDeviceInformationFromToken(token), deviceInfo);
        boolean doesEmailMatch = doesEmailMatch(getEmailFromToken(token), userDetails.getUsername());

        return isTokenNotExpired && doesDeviceInfoMatch && doesEmailMatch;
    }

    boolean doUserRolesMatch(String token, Collection<SimpleGrantedAuthority> authorities) {
        String roles = getUserRolesFromToken(token);
        Collection<String> authStrings = authorities.stream().map(Objects::toString).collect(Collectors.toList());
        String authString = StringUtils.join(authStrings, ',');

        return authString.equals(roles);
    }

    private boolean doesEmailMatch(String emailFromToken, String email) {
        return emailFromToken.equals(email);
    }

    private boolean doesDeviceInfoMatch(String infoFromToken, String infoFromHeader) {
        return infoFromToken.equals(infoFromHeader);
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.after(new Date());
    }

    public String generateToken(UserTokenInformation userTokenInformation, String deviceInformation) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(DEVICE_KEY, deviceInformation);
        claims.put(NAME_KEY, userTokenInformation.getName());
        claims.put(ROLE_KEY, userTokenInformation.getRole());
        claims.put(SCOPE_KEY, userTokenInformation.getScope());

        Date now = new Date();

        return Jwts.builder().setClaims(claims)
                .setSubject(userTokenInformation.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(privateKey, SIGNATURE_ALGORITHM).compact();
    }


    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }

    private String getStringClaimFromToken(String token, String key) {
        return getCustomClaimFromToken(token, key, String.class);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> getter) {
        final Claims claims = getAllClaimsFromToken(token);
        return getter.apply(claims);
    }

    private <T> T getCustomClaimFromToken(String token, String key, Class<T> returnClass) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get(key, returnClass);
    }
}
