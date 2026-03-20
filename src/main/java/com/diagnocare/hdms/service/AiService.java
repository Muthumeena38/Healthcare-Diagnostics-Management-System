package com.diagnocare.hdms.service;

import com.diagnocare.hdms.model.Report;
import com.diagnocare.hdms.model.ReportChunk;
import com.diagnocare.hdms.repository.ReportChunkRepository;
import com.diagnocare.hdms.repository.ReportRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportChunkRepository reportChunkRepository;

    public String extractTextFromPdf(byte[] pdfBytes) throws Exception {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    public List<String> splitIntoChunks(String text, int chunkSize) {
        List<String> chunks = new java.util.ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));
            start = end;
        }
        return chunks;
    }

    public void storeReportChunks(Report report) {
        try {
            String text = extractTextFromPdf(report.getFileContent());
            List<String> chunks = splitIntoChunks(text, 500);
            for (int i = 0; i < chunks.size(); i++) {
                ReportChunk chunk = new ReportChunk();
                chunk.setReport(report);
                chunk.setChunkText(chunks.get(i));
                chunk.setChunkIndex(i);
                reportChunkRepository.save(chunk);
            }
        } catch (Exception e) {
            System.out.println("Chunk storage failed: " + e.getMessage());
        }
    }

    public String retrieveRelevantChunks(Long reportId, String question) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        List<ReportChunk> chunks = reportChunkRepository.findByReport(report);
        if (chunks.isEmpty()) return "";

        String questionLower = question.toLowerCase();
        List<ReportChunk> relevant = chunks.stream()
                .filter(c -> {
                    String text = c.getChunkText().toLowerCase();
                    String[] keywords = questionLower.split(" ");
                    for (String keyword : keywords) {
                        if (keyword.length() > 3 && text.contains(keyword)) return true;
                    }
                    return false;
                })
                .limit(5)
                .collect(Collectors.toList());

        if (relevant.isEmpty()) {
            relevant = chunks.stream().limit(5).collect(Collectors.toList());
        }

        return relevant.stream()
                .map(ReportChunk::getChunkText)
                .collect(Collectors.joining("\n\n"));
    }

    public String askAboutReport(Long reportId, Long patientId, String question) throws Exception {
        String context = retrieveRelevantChunks(reportId, question);

        if (context.isEmpty()) {
            return "No report data found. Please make sure your report has been uploaded.";
        }

        String prompt = "You are a medical report assistant for DiagnoCare. " +
                "STRICT RULES: " +
                "1. Do NOT diagnose any condition. " +
                "2. Do NOT say values are normal or abnormal. " +
                "3. Do NOT suggest any medicines or treatments. " +
                "4. Only explain what the medical terms and values mean in simple language. " +
                "5. Always remind the patient to consult their doctor for medical advice. " +
                "\n\nRelevant Report Data:\n" + context +
                "\n\nQuestion: " + question;

        String requestBody = "{"
                + "\"model\": \"llama-3.3-70b-versatile\","
                + "\"messages\": [{\"role\": \"user\", \"content\": " + escapeJson(prompt) + "}],"
                + "\"max_tokens\": 500"
                + "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + groqApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        System.out.println("Groq Response: " + responseBody);

        try {
            int contentStart = responseBody.indexOf("\"content\":\"") + 11;
            if (contentStart > 10) {
                StringBuilder content = new StringBuilder();
                int i = contentStart;
                while (i < responseBody.length()) {
                    char c = responseBody.charAt(i);
                    if (c == '\\' && i + 1 < responseBody.length()) {
                        char next = responseBody.charAt(i + 1);
                        if (next == '"') { content.append('"'); i += 2; continue; }
                        if (next == 'n') { content.append('\n'); i += 2; continue; }
                        if (next == 't') { content.append('\t'); i += 2; continue; }
                        if (next == '\\') { content.append('\\'); i += 2; continue; }
                    } else if (c == '"') {
                        break;
                    }
                    content.append(c);
                    i++;
                }
                String result = content.toString().trim();
                if (!result.isEmpty()) return result;
            }
        } catch (Exception e) {
            System.out.println("Parsing error: " + e.getMessage());
        }

        return "Sorry, I could not process your request. Please try again.";
    }

    private String escapeJson(String text) {
        return "\"" + text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                + "\"";
    }
}