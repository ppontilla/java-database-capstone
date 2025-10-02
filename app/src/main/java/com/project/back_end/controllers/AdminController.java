package com.project.back_end.controllers;

import com.project.back_end.model.Admin;
import com.project.back_end.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final AdminService adminService;

    // Constructor injection for service
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Admin login endpoint
     * Accepts an Admin object (username, password) and validates credentials.
     * If valid, returns JWT token; otherwise, error message.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        return adminService.validateAdmin(admin);
    }
}
