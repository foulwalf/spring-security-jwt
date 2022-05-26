package com.example.crud_api.services;

import com.example.crud_api.models.User;
import com.example.crud_api.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service @Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username);
        if (user == null) {
            throw  new UsernameNotFoundException("Utilisateur inexistant dans la base de données");
        }
        //log.info(String.valueOf(new  org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ADMIN")))));
        return new  org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ADMIN")));
    }
}
