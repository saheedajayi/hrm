package com.devodata.hrm.controllers;

import com.devodata.hrm.dtos.AddHealthRecordRequest;
import com.devodata.hrm.dtos.HealthRecordResponse;
import com.devodata.hrm.dtos.UpdateHealthRecordRequest;
import com.devodata.hrm.exceptions.HealthRecordNotFoundException;
import com.devodata.hrm.services.HealthRecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/healthRecords")
@AllArgsConstructor
public class HealthRecordController {

    private final HealthRecordService healthRecordService;



    @PostMapping("/add")
    public ResponseEntity<String> addHealthRecord(@RequestBody AddHealthRecordRequest newHealthRecord) {
        try {
            String response = healthRecordService.addHealthRecord(newHealthRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/{healthRecordId}")
    public ResponseEntity<HealthRecordResponse> getHealthRecord(@PathVariable Long healthRecordId) {
        try {
            HealthRecordResponse healthRecordResponse = healthRecordService.getHealthRecord(healthRecordId);
            return ResponseEntity.ok(healthRecordResponse);
        } catch (HealthRecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateHealthRecord(@RequestBody UpdateHealthRecordRequest updateHealthRecord) {
        try {
            String response = healthRecordService.updateHealthRecord(updateHealthRecord);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{healthRecordId}")
    public ResponseEntity<String> deleteHealthRecord(@PathVariable Long healthRecordId) {
        try {
            String response = healthRecordService.deleteHealthRecord(healthRecordId);
            return ResponseEntity.ok(response);
        } catch (HealthRecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
