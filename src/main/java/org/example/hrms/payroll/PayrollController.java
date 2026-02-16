package org.example.hrms.payroll;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payroll")
@AllArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping("/structure")
    public SalaryStrucEntity createSalaryStructure(
            @RequestBody createSalaryStructureRequest request
    ) {
        return payrollService.createSalaryStructure(
                request.getDesignationId(),
                request.getSalary()
        );
    }
    @GetMapping("/structure/{employeeId}")
    public SalaryStrucEntity getSalaryStructure(
            @PathVariable Long employeeId
    ) {
        return payrollService.getSalaryStructure(employeeId);
    }

    @PostMapping("/generate/{employeeId}")
    public PayrollEntity generatePayroll(
            @PathVariable Long employeeId,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam double deduction
    ) {
        return payrollService.generatePayroll(employeeId, month, year, deduction);
    }

    @GetMapping("/{employeeId}")
    public PayrollEntity getPayroll(
            @PathVariable Long employeeId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return payrollService.getPayroll(employeeId, month, year);
    }


}
