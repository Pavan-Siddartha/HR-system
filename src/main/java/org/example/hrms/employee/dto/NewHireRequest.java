package org.example.hrms.employee.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.hrms.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
public class NewHireRequest {

    @NotBlank
    private String firstName;

    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private LocalDate dateOfJoin;

    @NotNull
    private Long departmentId;

    @NotNull
    private Long designationId;

    private Long managerId; // optional

}
