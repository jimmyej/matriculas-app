package com.courses.entities.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EnrollmentCourseId implements Serializable {
    private static final long serialVersionUID = -5230999504747358240L;
    private Long enrollmentId;
    private Long courseId;
}
