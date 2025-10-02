package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // Inject secret from application.properties
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey signingKey;

    public TokenService(AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @PostConstruct
    public void init() {
        this.signingKey = getSigningKey();
    }

    /**
     * Generate JWT token with 7 days expiration.
     */
    public String generateToken(String identifier) {
        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 604800000L)) // 7 days
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract identifier (username/email) from JWT token.
     */
    public String extractIdentifier(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Validate JWT token against user type (admin, doctor, patient).
     */
    public boolean validateToken(String token, String userType) {
        try {
            String identifier = extractIdentifier(token);

            switch (userType.toLowerCase()) {
                case "admin":
                    return adminRepository.findByUsername(identifier).isPresent();

                case "doctor":
                    return doctorRepository.findByEmail(identifier).isPresent();

                case "patient":
                    return patientRepository.findByEmail(identifier) != null;

                default:
                    return false;
            }
        } catch (Exception e) {
            return false; // invalid or expired token
        }
    }

    /**
     * Build signing key from secret.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extract claims from token.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
