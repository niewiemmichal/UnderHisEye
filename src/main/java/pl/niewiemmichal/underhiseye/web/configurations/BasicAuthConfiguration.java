package pl.niewiemmichal.underhiseye.web.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class BasicAuthConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth, DaoAuthenticationProvider provider,
                             PasswordEncoder encoder) throws Exception {
        //auth.authenticationProvider(provider);
        auth.inMemoryAuthentication()
                .withUser("doctor").password(encoder.encode("doctor")).roles("DOCTOR")
                .and()
                .withUser("registrant").password(encoder.encode("doctor")).roles("REGISTRANT");
    }
}
