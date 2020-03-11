package br.com.microservices.auth.security.config;

import br.com.microservices.auth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import br.com.microservices.core.property.JwtConfiguration;
import br.com.microservices.token.config.SecurityTokenConfig;
import br.com.microservices.token.creator.TokenCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityCredentialsConfig extends SecurityTokenConfig {

    private final TokenCreator tokenCreator;
    private final UserDetailsService userDetailsService;

    public SecurityCredentialsConfig(JwtConfiguration jwtConfiguration,
                                     TokenCreator tokenCreator, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {

        super(jwtConfiguration);
        this.tokenCreator = tokenCreator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(),jwtConfiguration, tokenCreator));
        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
