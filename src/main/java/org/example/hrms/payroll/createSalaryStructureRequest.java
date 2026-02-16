package org.example.hrms.payroll;

import lombok.Data;

@Data
public class createSalaryStructureRequest {

    private Long designationId;
    private double salary;
}
