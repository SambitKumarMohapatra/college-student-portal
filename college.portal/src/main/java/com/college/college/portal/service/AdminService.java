package com.college.college.portal.service;

import com.college.college.portal.dto.AdminProfileDTO;
import com.college.college.portal.entity.Admin;
import com.college.college.portal.exception.ResourceNotFoundException;
import com.college.college.portal.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    public AdminProfileDTO getAdminByEmail(String email) {
        Admin a = adminRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + email));
        return mapToDTO(a);
    }

    public List<AdminProfileDTO> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AdminProfileDTO mapToDTO(Admin a) {
        return AdminProfileDTO.builder()
                .id(a.getId())
                .name(a.getName())
                .email(a.getEmail())
                .phone(a.getPhone())
                .adminRole(a.getAdminRole())
                .role(a.getRole().name())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
