package com.courses.entities.ids;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class EnrollmentCourseId implements Serializable {
    private static final long serialVersionUID = -5230999504747358240L;
    private Long enrollmentId;
    private Long courseId;
}
