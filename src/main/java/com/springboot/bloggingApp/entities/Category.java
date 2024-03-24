package com.springboot.bloggingApp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@Table
public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = 953674430986395083L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    @NotEmpty(message = "Input cannot be null and must have size greater than 0")
    @Pattern(regexp = "^[a-zA-Z0-9\s]{3,255}$", message = "Input string length must be greater than 3 and string must be alphanumeric only")
    private String categoryTitle;
    @NotEmpty(message = "Input cannot be null and must have size greater than 0")
    @Pattern(regexp = "^[a-zA-Z0-9\s]{3,255}$", message = "Input string length must be greater than 3 and string must be alphanumeric only")
    private String description;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
}
