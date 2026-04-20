package com.college.college.portal.repository;

import com.college.college.portal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRollNumber(String rollNumber);
    List<Student> findByBranch(String branch);
    List<Student> findBySemester(Integer semester);
    List<Student> findByBranchAndSemester(String branch, Integer semester);
}
