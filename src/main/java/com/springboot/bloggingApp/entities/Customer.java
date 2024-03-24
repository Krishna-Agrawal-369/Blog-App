package com.springboot.bloggingApp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 7605188963923859544L;
    @NotEmpty(message = "Input cannot be null and must have size greater than 0")
    @Pattern(regexp = "^[a-zA-Z\s]{3,255}$", message = "Input string length must be greater than 3 and string must be alphabetic only")
    private String name;
    @Email
    private String email;
    @Min(1000)
    @Max(100000000)
    private int balance;
}
