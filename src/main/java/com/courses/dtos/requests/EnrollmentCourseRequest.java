package com.courses.dtos.requests;


import com.courses.entities.ids.EnrollmentCourseId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCourseRequest {
    @EmbeddedId
    private EnrollmentCourseId id;
}
