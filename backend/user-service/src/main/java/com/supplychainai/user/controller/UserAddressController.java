package com.supplychainai.user.controller;

import com.supplychainai.user.model.UserAddress;
import com.supplychainai.user.repository.UserAddressRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-addresss")
public class UserAddressController {

    private final UserAddressRepository repository;

    public UserAddressController(UserAddressRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<UserAddress>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAddress> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAddress not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<UserAddress> create(@RequestBody UserAddress entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAddress> update(@PathVariable UUID id, @RequestBody UserAddress entity) {
        entity.setId(id);
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
