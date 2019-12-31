package com.teamE.security.configuration;

import com.teamE.security.SavedRequestAwareAuthenticationSuccessHandler;
import com.teamE.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.teamE.security.RestAuthenticationEntryPoint;

@EnableWebSecurity
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private JwtRequestFilter jwtRequestFilter;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    public SecurityJavaConfig(CustomUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter, RestAuthenticationEntryPoint authenticationEntryPoint, SavedRequestAwareAuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.successHandler = successHandler;
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                .authorizeRequests()
//                .antMatchers("/events/**").permitAll()
//                .antMatchers("/ads/**").permitAll()
                .antMatchers("/signUp", "/logIn").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successHandler).failureHandler(failureHandler()).and()
                .logout();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}

