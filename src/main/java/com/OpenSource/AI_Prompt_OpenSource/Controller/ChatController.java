package com.OpenSource.AI_Prompt_OpenSource.Controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final WebClient ollama = WebClient.create("http://localhost:11434");
    
    private final String HISTORY_DIR = "uploads/history/";

    public ChatController() {
        // Create directory on startup if it doesn't exist
        new File(HISTORY_DIR).mkdirs();
    }

 // ChatController.java modifications

    @PostMapping("/history/save")
    public void saveHistory(@RequestBody Map<String, Object> historyData) {
        // Expecting userId in the request body
        String userId = historyData.get("userId").toString();
        String userDir = HISTORY_DIR + userId + "/";
        new File(userDir).mkdirs(); // Create user-specific folder

        String fileName = historyData.get("id").toString() + ".json";
        try (FileWriter writer = new FileWriter(userDir + fileName)) {
            writer.write(new com.google.gson.Gson().toJson(historyData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/history/list/{userId}")
    public List<Map<String, String>> listHistory(@PathVariable String userId) {
        File folder = new File(HISTORY_DIR + userId + "/");
        File[] files = folder.listFiles();
        if (files == null) return Collections.emptyList();

        return Arrays.stream(files)
                .map(f -> {
                    try {
                        String content = new String(Files.readAllBytes(f.toPath()));
                        JsonObject jsonObject = JsonParser.parseString(content).getAsJsonObject();
                        return Map.of(
                            "id", f.getName().replace(".json", ""),
                            "title", jsonObject.has("title") ? jsonObject.get("title").getAsString() : "Untitled"
                        );
                    } catch (Exception e) {
                        return Map.of("id", f.getName().replace(".json", ""), "title", "Untitled Chat");
                    }
                })
                .collect(Collectors.toList());
    }

 // Change the mapping to include userId
    @GetMapping("/history/{userId}/{id}")
    public String getHistory(@PathVariable String userId, @PathVariable String id) throws Exception {
        // Construct path including the userId directory
        Path path = Paths.get(HISTORY_DIR, userId, id + ".json");
        
        if (!Files.exists(path)) {
            throw new RuntimeException("Chat history not found for user: " + userId);
        }
        
        return new String(Files.readAllBytes(path));
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody ChatRequest request) {
        
        // UPDATED PROMPT: Allows code blocks and headers
        String systemInstructions = """
            System: You are a professional assistant.
            Rules:
            1. If the user asks for code, wrap it in Markdown code blocks (e.g., ```java).
            2. Use ## for headers.
            3. Provide clear explanations, but prioritize the code block.
            
            User Question: %s
            Assistant Output:""".formatted(request.prompt());

        Map<String, Object> payload = Map.of(
            "model", request.model(),
            "prompt", systemInstructions,
            "stream", true,
            "options", Map.of(
                "temperature", 0.2, 
                "num_predict", 1000   
            )
        );

        return ollama.post()
                .uri("/api/generate")
                .bodyValue(payload)
                .retrieve()
                .bodyToFlux(String.class);
    }
}

record ChatRequest(String model, String prompt) {}