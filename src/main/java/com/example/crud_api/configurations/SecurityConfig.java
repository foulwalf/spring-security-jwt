package com.example.crud_api.configurations;

import com.example.crud_api.filters.AuthenticationFilter;
import com.example.crud_api.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private  final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        final AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        //authenticationFilter.setFilterProcessesUrl("/user/login");
        httpSecurity.cors().and();
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(STATELESS);
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/user/login", "/user/refreshtoken").permitAll();
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/filiere/**").hasAuthority("ADMIN");
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/etudiant/**").hasAuthority("ADMIN");
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/niveau/**").hasAuthority("ADMIN");
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        httpSecurity.addFilter(authenticationFilter);
        httpSecurity.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
