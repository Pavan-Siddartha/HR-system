package org.example.hrms.payroll;

import lombok.RequiredArgsConstructor;
import org.example.hrms.designation.DesignationEntity;
import org.example.hrms.designation.DesignationRepository;
import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.employee.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final SalaryStrucRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollRepository payrollRepository;
    private final DesignationRepository designationRepository;

    @Transactional
    public PayrollEntity generatePayroll(
            Long employeeId,
            int month,
            int year,
            double deduction
    ) {

        if (payrollRepository.existsByEmployee_IdAndMonthAndYear(employeeId, month, year)) {
            throw new IllegalStateException("Payroll already generated for this month");
        }

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        SalaryStrucEntity structure = salaryRepository
                .findByDesignation_Id(employee.getCurrentDesignation().getId())
                .orElseThrow(() -> new IllegalStateException("Salary structure not defined"));

        double salary = structure.getSalary();
        double actualSalary = salary - deduction;

        if (actualSalary < 0) {
            throw new IllegalArgumentException("Deduction cannot exceed salary");
        }

        PayrollEntity payroll = PayrollEntity.builder()
                .employee(employee)
                .month(month)
                .year(year)
                .salary(salary)
                .deduction(deduction)
                .actualSalary(actualSalary)
                .build();

        return payrollRepository.save(payroll);
    }

    public PayrollEntity getPayroll(Long employeeId, int month, int year) {

        return payrollRepository
                .findByEmployee_IdAndMonthAndYear(employeeId, month, year)
                .orElseThrow(() -> new IllegalStateException("Payroll not found"));
    }


    public SalaryStrucEntity getSalaryStructure(Long employeeId) {

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        return salaryRepository
                .findByDesignation_Id(employee.getCurrentDesignation().getId())
                .orElseThrow(() -> new IllegalStateException("Salary structure not defined"));
    }

    @Transactional
    public SalaryStrucEntity createSalaryStructure(Long designationId, double salary) {

        if (salaryRepository.findByDesignation_Id(designationId).isPresent()) {
            throw new IllegalStateException("Salary structure already exists for this designation");
        }

        DesignationEntity designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new IllegalArgumentException("Designation not found"));

        SalaryStrucEntity structure = SalaryStrucEntity.builder()
                .designation(designation)
                .salary(salary)
                .build();

        return salaryRepository.save(structure);
    }
}
