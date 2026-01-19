package com.example.backend.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.entity.ExelBody;
import com.example.backend.services.FileService;

@RestController
public class TestController {

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public ResponseEntity<?> getFile() {

        return new ResponseEntity<>(fileService.list(), HttpStatus.OK);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "User", required = false) String name) {
        return String.format("Hello %s", name);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Upload Successfully"));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> dowmload(@PathVariable String filename) throws MalformedURLException {
        Resource resource = fileService.download(filename);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/read")
    public ResponseEntity<?> read(@RequestParam String filename) throws IOException {
        ExelBody data = fileService.readExcel(filename);

        return ResponseEntity.ok(data);
    }

    @PostMapping("/write")
    public ResponseEntity<?> overwrite(
            @RequestBody ExelBody request) throws IOException {

        fileService.writeExcel(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

}
