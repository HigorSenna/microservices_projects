package br.com.microservices.token.creator;

import br.com.microservices.core.model.ApplicationUser;
import br.com.microservices.core.property.JwtConfiguration;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenCreator {

    private final JwtConfiguration jwtConfiguration;

    //    (JWS) = Json Web Signed
    @SneakyThrows
    public SignedJWT createSignedJWS(Authentication authentication) {
        log.info("Iniciando criacao da assinatura do token");
        ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();
        JWTClaimsSet jwtClaimsSet = createJWTClaimSet(authentication, applicationUser);
        KeyPair keyPair = generateKeyPair();

        log.info("Construindo JWK a partir das chaves RSA");

        JWK jwk = new RSAKey.Builder( (RSAPublicKey) keyPair.getPublic()).keyID(UUID.randomUUID().toString()).build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).jwk(jwk).type(JOSEObjectType.JWT).build(), jwtClaimsSet);

        log.info("Assinando o token com a chave privada RSA");

        RSASSASigner singer = new RSASSASigner(keyPair.getPrivate());

        signedJWT.sign(singer);

        log.info("Token serializado '{}' ", signedJWT.serialize());
        return signedJWT;
    }

    public String encryptToken(SignedJWT signedJWT) throws JOSEException {
        log.info("Iniciando a criptografia");

        DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());

        JWEObject jweObject =
                new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256).contentType("JWT").build(), new Payload(signedJWT));

        log.info("Criptografando token com chave privada");

        jweObject.encrypt(directEncrypter);

        log.info("Token criptografado");

        return jweObject.serialize();
    }

    private JWTClaimsSet createJWTClaimSet(Authentication authentication, ApplicationUser applicationUser) {
        log.info("Criando o Objeto JWTClaimSet para o usuario '{}' ", applicationUser);

        return new JWTClaimsSet.Builder().subject(applicationUser.getUsername())
                .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()))
                .issuer("http://meudominio.com")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getExpiration()) * 1000))
                .build();
    }

    @SneakyThrows
    private KeyPair generateKeyPair() {
        log.info("Gerando RSA 2048 bits Keys");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.genKeyPair();
    }
}
