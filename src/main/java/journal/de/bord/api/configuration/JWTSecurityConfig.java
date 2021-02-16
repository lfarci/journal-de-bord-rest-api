package journal.de.bord.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorization -> authorization
                        .antMatchers(HttpMethod.GET, "/").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/**").hasAuthority("SCOPE_read")
                        .antMatchers(HttpMethod.POST, "/api/**").hasAuthority("SCOPE_write")
                        .antMatchers(HttpMethod.PUT, "/api/**").hasAuthority("SCOPE_write")
                        .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority("SCOPE_write")
                        .anyRequest().authenticated())
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt());
    }
}