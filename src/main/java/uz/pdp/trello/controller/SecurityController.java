package uz.pdp.trello.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class  SecurityController {

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
                            .requestMatchers("/admin" , "/admin/**").hasRole("ADMIN")
                            .requestMatchers("/status**").hasAnyRole("MAINTAINER","ADMIN")
                            .requestMatchers("/task/addTask**").hasAnyRole("MAINTAINER","ADMIN")
                            .anyRequest().authenticated();
                })
                .formLogin(form ->
                        form.loginPage("/login").permitAll()
                                .defaultSuccessUrl("/task", true))
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
