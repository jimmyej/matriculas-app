package com.courses.dtos.requests;

import com.courses.entities.ids.EnrollmentCourseId;
import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCourseRequest {
    @EmbeddedId
    private EnrollmentCourseId id;
}
