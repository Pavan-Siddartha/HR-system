package org.example.hrms.designation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DesignationRepository extends JpaRepository<DesignationEntity, Long> {

    Optional<DesignationEntity> findByTitle(String title);

}
