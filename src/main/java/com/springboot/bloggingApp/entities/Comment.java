package com.springboot.bloggingApp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 6987037997876167883L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;
    @NotEmpty(message = "Input cannot be null and must have size greater than 0")
    @Pattern(regexp = "^[a-zA-Z0-9\s]{3,255}$", message = "Input string length must be greater than 3 and string must be alphanumeric only")
    private String content;
    @ManyToOne
    private Post post;
}
