package org.example.hrms.feedback.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FeedbackResponse {

    private Long id;
    private Long employeeId;
    private Long givenById;
    private String type;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
