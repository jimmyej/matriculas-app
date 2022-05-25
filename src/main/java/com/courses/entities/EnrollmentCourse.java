package com.courses.entities;


import com.courses.entities.ids.EnrollmentCourseId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "enrollments_courses" )
public class EnrollmentCourse {
    @EmbeddedId
    private EnrollmentCourseId id;
}
