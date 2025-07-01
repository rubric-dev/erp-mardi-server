package mardi.erp_mini.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final RefreshTokenRepository refreshTokenRepository;

    // application.yml에서 secret 값 가져와서 key에 저장
    public JwtTokenProvider(@Value("${security.jwt.token}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public AuthResponse.Login generateToken(Authentication authentication, Long userId, String email) {
        // 권한 가져오기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> auth = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + 900_000); // 15분 (15 * 60 * 1000)
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", auth)
                .claim("userId", userId)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date refreshTokenExpiresIn = new Date(now + 2_592_000_000L); // 30일 (30 * 24 * 60 * 60 * 1000)
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .claim("auth", auth)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        refreshTokenRepository.upsertValue(userId, refreshToken, email);

        return AuthResponse.Login.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type(auth.get(0))
                .email(email)
                .build();
    }

    public AuthResponse.Login generateToken(String email, List<GrantedAuthority> authorities, Long userId) {
        long now = (new Date()).getTime();

        List<String> auth = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Date accessTokenExpiresIn = new Date(now + 900_000); // 15분 (15 * 60 * 1000)
        String accessToken = Jwts.builder()
                .setSubject(email)
                .claim("auth", auth)
                .claim("userId", userId)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date refreshTokenExpiresIn = new Date(now + 2_592_000_000L); // 30일 (30 * 24 * 60 * 60 * 1000)
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .claim("auth", auth)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        refreshTokenRepository.upsertValue(userId, refreshToken, email);

        return AuthResponse.Login.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type(auth.get(0))
                .email(email)
                .build();
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal =
                new CustomUserDetails(claims.get("userId", Long.class), claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    @Transactional
    public AuthResponse.Login refresh(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByValue(token);

        if (refreshToken == null) throw new NotFoundException("invalid token");

        Claims claims = parseClaims(token);

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().replaceAll("[\\[\\]]", "").split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        return generateToken(refreshToken.getEmail(), new ArrayList<>(authorities), refreshToken.getUserId());
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token. token : {}", token);
            throw new ExpiredTokenException();
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }


    // accessToken
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
