package com.demo.verifit.controller;

import com.demo.verifit.service.VerifitAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VerifitAttendanceController {
    @Autowired
    private VerifitAttendanceService verifitAttendanceService;
    @PostMapping("/addAttendance")
    public ResponseEntity<String> addAttendance(@RequestBody String userId) {
        verifitAttendanceService.addAttendanceRecord(userId);
        return ResponseEntity.ok("Attendance record added successfully");
    }

    @GetMapping("/checkDiscountEligibility")
    public ResponseEntity<Boolean> checkDiscountEligibility(@RequestParam String userId) {
        return ResponseEntity.ok(verifitAttendanceService.isDiscountEligible(userId));
    }

    @GetMapping("/getUserCurrentStreak")
    public ResponseEntity<Integer> getUserCurrentStreak(@RequestParam String userId) {
        return ResponseEntity.ok(verifitAttendanceService.getUserCurrentStreak(userId));
    }
}
