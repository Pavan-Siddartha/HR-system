package org.example.hrms.employee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hrms.employee.dto.NewHireRequest;
import org.example.hrms.employee.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    //  Hire Employee
    @PostMapping("/hire")
    public ResponseEntity<EmployeeEntity> hireEmployee(@Valid @RequestBody NewHireRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(employeeService.hireEmployee(dto));
    }

    // Get Employee By ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeEntity> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // Get All Employees
    @GetMapping
    public ResponseEntity<List<EmployeeEntity>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // Promote Employee
    @PutMapping("/{id}/promote/{designationId}")
    public ResponseEntity<EmployeeEntity> promoteEmployee(
            @PathVariable Long id,
            @PathVariable Long designationId) {

        return ResponseEntity.ok(
                employeeService.promoteEmployee(id, designationId)
        );
    }

    //  Transfer Department
    @PutMapping("/{id}/transfer/{departmentId}")
    public ResponseEntity<EmployeeEntity> transferEmployee(
            @PathVariable Long id,
            @PathVariable Long departmentId) {

        return ResponseEntity.ok(
                employeeService.transferEmployee(id, departmentId)
        );
    }

    // Resign Employee
    @PutMapping("/{id}/resign")
    public ResponseEntity<EmployeeEntity> resignEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(
                employeeService.resignEmployee(id)
        );
    }

    //  Update Profile (phone/email)
    @PutMapping("/{id}/update")
    public ResponseEntity<EmployeeEntity> updateProfile(
            @PathVariable Long id,
            @RequestBody NewHireRequest dto) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, dto)
        );
    }
}
