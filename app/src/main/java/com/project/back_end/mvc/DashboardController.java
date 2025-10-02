package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



import java.util.Map;

@Controller
public class DashboardController {

    // @Autowired
    // private Service service;

    // /**
    //  * Admin Dashboard View
    //  * URL: /adminDashboard/{token}
    //  * Purpose: Verify admin token and load admin dashboard view
    //  */
    // @GetMapping("/adminDashboard/{token}")
    // public String adminDashboard(@PathVariable String token) {
    //     Map<String, Object> validationResult = service.validateToken(token, "admin");

    //     if (validationResult.isEmpty()) {
    //         return "admin/adminDashboard"; // Thymeleaf template
    //     } else {
    //         return "redirect:/"; // Redirect to login or home
    //     }
    // }

    // /**
    //  * Doctor Dashboard View
    //  * URL: /doctorDashboard/{token}
    //  * Purpose: Verify doctor token and load doctor dashboard view
    //  */
    // @GetMapping("/doctorDashboard/{token}")
    // public String doctorDashboard(@PathVariable String token) {
    //     Map<String, Object> validationResult = service.validateToken(token, "doctor");

    //     if (validationResult.isEmpty()) {
    //         return "doctor/doctorDashboard"; // Thymeleaf template
    //     } else {
    //         return "redirect:/"; // Redirect to login or home
    //     }
    // }
}
