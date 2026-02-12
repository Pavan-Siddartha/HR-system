package org.example.hrms.department;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hrms.department.dto.CreateDepartmentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<List<DepartmentEntity>> createDepartments(
            @RequestBody List<CreateDepartmentRequest> requests
    ) {
        List<DepartmentEntity> createdDepartments = requests.stream()
                .map(departmentService::createDepartment)
                .toList();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdDepartments);
    }



    @GetMapping
    public ResponseEntity<List<DepartmentEntity>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public DepartmentEntity getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PutMapping("/{id}")
    public DepartmentEntity updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody CreateDepartmentRequest request) {
        return departmentService.updateDepartment(id, request);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivateDepartment(@PathVariable Long id) {
        departmentService.deactivateDepartment(id);
    }

    @PatchMapping("/{id}/activate")
    public void activateDepartment(@PathVariable Long id) {
        departmentService.activateDepartment(id);
    }
}
