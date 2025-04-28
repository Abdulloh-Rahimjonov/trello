package uz.pdp.trello.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityController {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(registry -> {
                    registry
                            .requestMatchers(
                                    "/picture/**",
                                    "/register",
                                    "/login",
                                    "/logout",
                                    "/verified",
                                    "/verifiedPage"
                            ).permitAll()
//                            .requestMatchers("", "", "").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .formLogin(form ->
                        form.loginPage("/login").permitAll()
                                .defaultSuccessUrl("/market/addOrder", true))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
