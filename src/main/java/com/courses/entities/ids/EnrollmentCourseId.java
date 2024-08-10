package com.courses.entities.ids;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class EnrollmentCourseId implements Serializable {
    @Serial
    private static final long serialVersionUID = -5230999504747358240L;
    private Long enrollmentId;
    private Long courseId;
}
