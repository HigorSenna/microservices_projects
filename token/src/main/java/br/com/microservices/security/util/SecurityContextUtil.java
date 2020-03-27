package br.com.microservices.security.util;

import br.com.microservices.core.model.ApplicationUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class SecurityContextUtil {

    private SecurityContextUtil() {
    }

    //Adicionando o valor no contexto do spring, posso pegar em qualquer classe
    public static void setSecurityContext(SignedJWT signedJWT) {
        try {
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String username =  claims.getSubject();
            if(username == null) {
                throw new JOSEException("Username vazio no JWT");
            }

            List<String> authorities = claims.getStringListClaim("authorities");
            ApplicationUser applicationUser =
                    ApplicationUser.builder()
                            .id(claims.getLongClaim("userId"))
                            .username(username)
                            .role(String.join(",", authorities))
                    .build();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(applicationUser, null, createAuthorities(authorities));

            usernamePasswordAuthenticationToken.setDetails(signedJWT.serialize());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (Exception ex) {
            log.error("Erro ao setar security context", ex);
            SecurityContextHolder.clearContext();
        }
    }

    private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }
}
