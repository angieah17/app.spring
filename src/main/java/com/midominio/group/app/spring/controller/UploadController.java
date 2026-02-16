package com.midominio.group.app.spring.controller;

import com.midominio.group.app.spring.service.PreguntaUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/preguntas")
@CrossOrigin(origins = "http://localhost:5173")
public class UploadController {

    private final PreguntaUploadService preguntaUploadService;

    public UploadController(PreguntaUploadService preguntaUploadService) {
        this.preguntaUploadService = preguntaUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        int creadas = preguntaUploadService.upload(file);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("mensaje", "Archivo procesado correctamente");
        body.put("preguntasCreadas", creadas);

        return ResponseEntity.ok(body);
    }
}
