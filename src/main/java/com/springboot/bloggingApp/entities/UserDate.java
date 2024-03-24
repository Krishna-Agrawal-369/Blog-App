package com.springboot.bloggingApp.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDate {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate = LocalDate.now();
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate modifiedDate = LocalDate.now();
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String createdBy;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String modifiedBy;
}
