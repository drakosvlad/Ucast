package com.Ucast;

import com.Ucast.auth.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Autowired
//    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public JwtAuthenticationFilter jwtFilter() throws Exception {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and().csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers(
                                "/login",
                                "/registration",
                                "/author-registration",
                                "/authors",
                                "/author/**",
                                "/podcasts/**",
                                "/podcast/**",
                                "/static/**",
                                "/register-admin",
                                "/approve/*",
                                "/disapprove/*",
                                "/show-all-authors",
                                "/show-all-users",
                                "/channel/**",
                                "/increment-listened/**",
                                "/get-favorite-podcasts",
                                "/add-review/**",
                                "/getReviews/**",
                                "/favorite-podcast/**",
                                "/user/**",
                                "/user-info"
                        ).permitAll()
                            .anyRequest().authenticated()
                .and()
                    .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class).cors(); // is that all?
    }
}
