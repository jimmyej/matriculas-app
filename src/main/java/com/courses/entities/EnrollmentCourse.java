package com.courses.entities;


import com.courses.entities.ids.EnrollmentCourseId;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "enrollments_courses" )
public class EnrollmentCourse {
    @EmbeddedId
    private EnrollmentCourseId id;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrollmentCourse that = (EnrollmentCourse) o;
        return Objects.equals(id, that.id);
    }

}
