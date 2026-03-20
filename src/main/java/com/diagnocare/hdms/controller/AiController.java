package com.diagnocare.hdms.controller;

import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Autowired
    private AiService aiService;

    @Autowired
    private com.diagnocare.hdms.repository.ReportRepository reportRepository;

    @PostMapping("/ask/{reportId}")
    public ResponseEntity<?> askAboutReport(
            @PathVariable Long reportId,
            @RequestParam Long patientId,
            @RequestParam String question) {
        try {
            String answer = aiService.askAboutReport(reportId, patientId, question);
            return ResponseEntity.ok(Map.of("answer", answer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/store-chunks/{reportId}")
    public ResponseEntity<?> storeChunks(@PathVariable Long reportId) {
        try {
            Report report = reportRepository.findById(reportId)
                    .orElseThrow(() -> new RuntimeException("Report not found"));
            aiService.storeReportChunks(report);
            return ResponseEntity.ok(Map.of("message", "Chunks stored successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}