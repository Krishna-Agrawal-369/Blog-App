package com.springboot.bloggingApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.bloggingApp.entities.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 118074337041215775L;
    private int postId;
    @NotEmpty(message = "Input cannot be null and must have size greater than 0")
    @Pattern(regexp = "^[a-zA-Z\s]{3,255}$", message = "Input string length must be greater than 3 and string must be alphabetic only")
    private String title;
    @NotEmpty(message = "Input cannot be null and must have size greater than 0")
    @Pattern(regexp = "^[a-zA-Z\s]{3,255}$", message = "Input string length must be greater than 3 and string must be alphabetic only")
    private String content;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate = LocalDate.now();
    private UserDto user;
    private CategoryDto category;
    private List<CommentDto> comments;
}
