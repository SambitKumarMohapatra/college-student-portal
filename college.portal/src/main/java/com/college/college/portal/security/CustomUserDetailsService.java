package com.college.college.portal.security;

import com.college.college.portal.entity.Admin;
import com.college.college.portal.entity.Faculty;
import com.college.college.portal.entity.Student;
import com.college.college.portal.repository.AdminRepository;
import com.college.college.portal.repository.FacultyRepository;
import com.college.college.portal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        //Try Student
        var student=studentRepository.findByEmail(email);
        if(student.isPresent()){
            Student s=student.get();
            return new org.springframework.security.core.userdetails.User(
                    s.getEmail(),
                    s.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_"+s.getRole().name()))
            );
        }
        //Try Faculty
        var faculty = facultyRepository.findByEmail(email);
        if (faculty.isPresent()) {
            Faculty f = faculty.get();
            return new org.springframework.security.core.userdetails.User(
                    f.getEmail(),
                    f.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + f.getRole().name()))
            );
        }
        // Try Admin
        var admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            Admin a = admin.get();
            return new org.springframework.security.core.userdetails.User(
                    a.getEmail(),
                    a.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + a.getRole().name()))
            );
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
    
}
