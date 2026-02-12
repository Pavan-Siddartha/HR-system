package org.example.hrms.feedback;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hrms.feedback.dto.CreateFeedbackRequest;
import org.example.hrms.feedback.dto.FeedbackResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(
            @Valid @RequestBody CreateFeedbackRequest request
    ) {
        FeedbackResponse response = feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<FeedbackResponse>> getEmployeeFeedback(
            @PathVariable Long employeeId
    ) {
        return ResponseEntity.ok(
                feedbackService.getFeedbackForEmployee(employeeId)
        );
    }
}
