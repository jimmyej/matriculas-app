package com.courses.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {
    private long id;
    private String name;
    private String acronym;
    private LocalDate duration;
    private LocalDateTime updatedAt;
    private boolean status;
}