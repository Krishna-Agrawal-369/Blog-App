package com.springboot.bloggingApp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends UserDate implements Serializable {
    @Serial
    private static final long serialVersionUID = 5555179306491877478L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @NotEmpty(message = "Input cannot be null and must have size greater than 0")
    @Pattern(regexp = "^[a-zA-Z\s]{3,255}$", message = "Input string length must be greater than 3 and string must be alphabetic only")
    private String userName;
    @NotEmpty(message = "Input cannot be null and must have size greater than 0")
    @Pattern(regexp = "^[a-zA-Z0-9@#!$%^&*\s]{8,20}$", message = "Input string length must be greater than 8 and string must be password type only")
    private String password;
    @Email
    private String email;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
}
