package org.example.hrms.designation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDesignationRequest {

    @NotBlank
    private String title;

    @NotNull
    private Integer level;
}
