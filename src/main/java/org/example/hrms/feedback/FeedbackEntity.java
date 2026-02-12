package org.example.hrms.feedback;

import jakarta.persistence.*;
import lombok.*;
import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.enums.FeedbackType;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Employee receiving feedback
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    // Optional: who gave feedback
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "given_by_id")
    private EmployeeEntity givenBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackType type;

    @Column(nullable = false)
    private Integer rating; // 1–5

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
