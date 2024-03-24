package com.springboot.bloggingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 3715206715767873358L;
    private int commentId;
    @NotEmpty(message = "Input cannot be null and must have size greater than 0")
    @Pattern(regexp = "^[a-zA-Z0-9\s]{3,255}$", message = "Input string length must be greater than 3 and string must be alphanumeric only")
    private String content;
}
