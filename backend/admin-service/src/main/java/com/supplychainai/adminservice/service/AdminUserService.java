package com.supplychainai.adminservice.service;

import com.supplychainai.adminservice.dto.AdminUserRequest;
import com.supplychainai.adminservice.model.AdminUser;
import com.supplychainai.adminservice.repository.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AdminUserService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserService.class);

    private final AdminUserRepository repository;

    public AdminUserService(AdminUserRepository repository) {
        this.repository = repository;
    }

    public List<AdminUser> findAll() {
        return repository.findAll();
    }

    public AdminUser findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Admin user not found: " + id));
    }

    @Transactional
    public AdminUser create(AdminUserRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        AdminUser user = new AdminUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(request.isEnabled());
        AdminUser saved = repository.save(user);
        log.info("Admin user created: {}", saved.getUsername());
        return saved;
    }

    @Transactional
    public AdminUser update(UUID id, AdminUserRequest request) {
        AdminUser user = findById(id);
        if (!user.getUsername().equals(request.getUsername()) && repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (!user.getEmail().equals(request.getEmail()) && repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(request.isEnabled());
        AdminUser saved = repository.save(user);
        log.info("Admin user updated: {}", saved.getUsername());
        return saved;
    }

    @Transactional
    public void delete(UUID id) {
        AdminUser user = findById(id);
        repository.delete(user);
        log.info("Admin user deleted: {}", user.getUsername());
    }

    @Transactional
    public void toggleEnabled(UUID id) {
        AdminUser user = findById(id);
        user.setEnabled(!user.isEnabled());
        repository.save(user);
        log.info("Admin user {} status toggled to {}", user.getUsername(), user.isEnabled());
    }

    public long count() {
        return repository.count();
    }
}
