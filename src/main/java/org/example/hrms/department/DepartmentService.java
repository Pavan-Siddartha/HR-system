package org.example.hrms.department;

import lombok.RequiredArgsConstructor;
import org.example.hrms.department.dto.CreateDepartmentRequest;
import org.example.hrms.employee.EmployeeRepository;
import org.example.hrms.enums.EmploymentStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    public DepartmentEntity createDepartment(CreateDepartmentRequest request) {

        departmentRepository.findByName(request.getName())
                .ifPresent(d -> {
                    throw new IllegalArgumentException("Department already exists");
                });

        DepartmentEntity department = DepartmentEntity.builder()
                .name(request.getName())
                .active(true)
                .build();

        return departmentRepository.save(department);
    }

    public List<DepartmentEntity> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public DepartmentEntity getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
    }

    public DepartmentEntity updateDepartment(Long id, CreateDepartmentRequest request) {

        DepartmentEntity department = getDepartmentById(id);
        department.setName(request.getName());

        return departmentRepository.save(department);
    }

    public void deactivateDepartment(Long id) {

        DepartmentEntity department = getDepartmentById(id);
        boolean hasActiveEmployees =
                employeeRepository.existsByCurrentDepartment_IdAndEmploymentStatus(
                        id, EmploymentStatus.ACTIVE);

        if (hasActiveEmployees) {
            throw new IllegalStateException(
                    "Cannot deactivate department. Active employees are assigned to it."
            );
        }

        department.setActive(false);
        departmentRepository.save(department);
    }


    public void activateDepartment(Long id) {
        DepartmentEntity department = getDepartmentById(id);
        department.setActive(true);
        departmentRepository.save(department);
    }
}
