package com.example.crud_api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "user")
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id_user")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
}
