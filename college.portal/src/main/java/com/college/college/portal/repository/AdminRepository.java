package com.college.college.portal.repository;

import com.college.college.portal.entity.Admin;
import com.college.college.portal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);
}
