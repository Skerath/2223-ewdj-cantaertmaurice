package Aalst_Cantaert_Maurice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().and().
                authorizeHttpRequests(requests ->
                        requests.requestMatchers("/login**").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/403**").permitAll()
                                .requestMatchers("/book/**").permitAll()
                                .requestMatchers("/registerbook")
                                .access(new WebExpressionAuthorizationManager("hasRole('ROLE_ADMIN')"))
                                .requestMatchers("/update/*")
                                .access(new WebExpressionAuthorizationManager("hasRole('ROLE_ADMIN')"))
                                .requestMatchers("/*")
                                .access(new WebExpressionAuthorizationManager("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')"))
                                .requestMatchers("/api/**").permitAll() // Permit all requests under /api
                )
                .formLogin(form ->
                        form.defaultSuccessUrl("/", true)
                                .loginPage("/login")
                                .usernameParameter("username").passwordParameter("password"))
                .exceptionHandling().accessDeniedPage("/403");
        return http.build();
    }
}