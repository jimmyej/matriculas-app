package com.courses.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table( name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unidirectional
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    private LocalDate enrollmentDate;
    private boolean status;

    public Enrollment(Long id, LocalDate enrollmentDate, boolean status) {
        this.id = id;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }
}
