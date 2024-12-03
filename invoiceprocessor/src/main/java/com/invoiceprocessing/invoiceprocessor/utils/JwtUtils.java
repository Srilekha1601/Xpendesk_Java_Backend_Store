package com.invoiceprocessing.invoiceprocessor.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

	private static final String SECRET = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";
//	private static final long EXPIRATION_TIME = 1_800_000L;
//	private static final long EXPIRATION_TIME = 14_400_000L;
	private static final long EXPIRATION_TIME = 43_200_000L;

	public String getUserName(String token) {
		return extractClaims(token, Claims::getSubject);
	}

	public Date getExpirationTime(String token) {
		return extractClaims(token, Claims::getExpiration);
	}

//	public Integer getEmployeeId(String token) {
//		return Integer.parseInt(extractClaims(token, Claims::getId));
//	}

	public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpires(String token) {
		return getExpirationTime(token).before(new Date());
	}

	public Boolean validateUserToken(String token, UserDetails userDetails) {
		final String userName = getUserName(token);
		return (userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpires(token));
	}

	public String generateToken(String userName) {
		Map<String, Object> clame = new HashMap<String, Object>();
		return createToken(clame, userName);
	}

	private static String createToken(Map<String, Object> claim, String userName) {
		// TODO Auto-generated method stub
		return Jwts.builder().setClaims(claim).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private static Key getSignKey() {
		// TODO Auto-generated method stub
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
