package org.example.hrms.feedback;

import lombok.RequiredArgsConstructor;
import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.employee.EmployeeRepository;
import org.example.hrms.exception.ResourceNotFoundException;
import org.example.hrms.feedback.dto.CreateFeedbackRequest;
import org.example.hrms.feedback.dto.FeedbackResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmployeeRepository employeeRepository;

    public FeedbackResponse createFeedback(CreateFeedbackRequest request) {

        // validating that employee already exists
        EmployeeEntity employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));


        EmployeeEntity givenBy = null;
        if (request.getGivenById() != null) {
            givenBy = employeeRepository.findById(request.getGivenById()) //validating givenby employee exists
                    .orElseThrow(() -> new ResourceNotFoundException("GivenBy employee not found"));
        }

        FeedbackEntity feedback = FeedbackEntity.builder()
                .employee(employee)
                .givenBy(givenBy)
                .type(request.getType())
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        feedbackRepository.save(feedback);

        return mapToResponse(feedback);
    }

    public List<FeedbackResponse> getFeedbackForEmployee(Long employeeId) {

        return feedbackRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private FeedbackResponse mapToResponse(FeedbackEntity feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .employeeId(feedback.getEmployee().getId())
                .givenById(
                        feedback.getGivenBy() != null
                                ? feedback.getGivenBy().getId()
                                : null
                )
                .type(feedback.getType().name())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
