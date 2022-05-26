package com.example.crud_api.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.crud_api.models.Result;
import com.example.crud_api.models.User;
import com.example.crud_api.repositories.UserRepository;
import com.example.crud_api.services.UserService;
import com.example.crud_api.utilities.TokenMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.example.crud_api.security_constants.SecurityConstant.JWT_SECRET;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://127.0.0.1:5500/")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/add")
    public ResponseEntity<Result<Object>> signUp(@RequestBody User user) {
        ResponseEntity<Result<Object>> responseEntity;
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        Optional<User> addedUser = userRepository.findById(user.getId());
        if (addedUser.isPresent()) {
            responseEntity = new ResponseEntity<>(new Result<>("Utilisateur ajouté"), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new Result<>("Une erreur s'est produite"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    
    @PostMapping("/login")
    public ResponseEntity<Result<Object>> logIn(@RequestBody User user) {
        ResponseEntity<Result<Object>> responseEntity;
        Map<String, String> tokens = new HashMap<>();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authentication.isAuthenticated()) {
            org.springframework.security.core.userdetails.User authenticatedUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String secret = JWT_SECRET;

            String accessToken = TokenMethod.createAccesToken(authenticatedUser, "authentication", secret);
            String refreshToken = TokenMethod.refreshToken(authenticatedUser, "refreshing", secret, accessToken);
            //TokenMethod.refreshAccesToken(accessToken, secret);

            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            //tokens.put("refreshToken", refreshToken);

            responseEntity = new ResponseEntity<>(new Result<>("athentifié", tokens), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new Result<>("Non athentifié"), HttpStatus.FORBIDDEN);
        }
        return responseEntity;

    }
    @PostMapping("/refreshtoken")
    public void refreshUserAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader =  request.getHeader(AUTHORIZATION);
        String newAccessToken;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) userService.loadUserByUsername(username);
                newAccessToken = TokenMethod.createAccesToken(user, "authentication", JWT_SECRET);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", newAccessToken);
                tokens.put("refreshToken", refreshToken);
            } catch (Exception exception) {
                //log.info("Error in : {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("errorMessage", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
        }
    }

}
