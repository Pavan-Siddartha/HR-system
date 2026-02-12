package org.example.hrms.feedback.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.hrms.enums.FeedbackType;

@Getter
@Setter
public class CreateFeedbackRequest {

    @NotNull
    private Long employeeId;

    private Long givenById;

    @NotNull
    private FeedbackType type;

    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 1000)
    private String comment;
}
