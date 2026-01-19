package com.example.backend.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.web.multipart.MultipartFile;

import com.example.backend.entity.ExelBody;

@Service
public class FileService {
    @Value("${filePath}")
    private String filePath;

    public List<String> list() {
        File dir = new File(filePath);
        File[] lists = dir.listFiles();

        return lists != null ? Arrays.stream(lists).map(l -> l.getName()).collect(Collectors.toList()) : null;
    }

    public void uploadFile(MultipartFile file) throws IOException {
        Path uploadDir = Paths.get(filePath);
        String filename = StringUtils.cleanPath(
                file.getOriginalFilename());

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        if (!filename.endsWith(".xls") && !filename.endsWith(".xlsx")) {
            throw new IllegalArgumentException("Only Excel files are allowed");
        }

        Path destination = uploadDir.resolve(
                file.getOriginalFilename());

        Files.copy(
                file.getInputStream(),
                destination,
                StandardCopyOption.REPLACE_EXISTING);

    }

    public Resource download(String filename) throws MalformedURLException {
        Path file = Paths.get(filePath).resolve(filename).normalize();

        return new UrlResource(file.toUri());
    }

    public ExelBody readExcel(String filename) throws IOException {

        ExelBody body = new ExelBody();
        List<Map<String, String>> rows = new ArrayList<>();

        Path path = Paths.get(filePath)
                .resolve(filename)
                .normalize();

        try (InputStream is = Files.newInputStream(path);
                Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                Map<String, String> rowData = new LinkedHashMap<>();

                for (Cell cell : row) {
                    int colIndex = cell.getColumnIndex();
                    String header = getCellString(headerRow.getCell(colIndex));
                    rowData.put(header, getCellString(cell));
                }

                rows.add(rowData);
            }
        }
        body.setFilename(filename);
        body.setRows(rows);
        return body;
    }

    private String getCellString(Cell cell) {
        if (cell == null)
            return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    public void writeExcel(ExelBody request) throws IOException {

        Path path = Paths.get(filePath)
                .resolve(request.getFilename())
                .normalize();

        try (
                InputStream is = Files.newInputStream(path);
                Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            // header → column index
            Map<String, Integer> headerIndex = new HashMap<>();
            for (Cell cell : headerRow) {
                headerIndex.put(
                        getCellString(cell),
                        cell.getColumnIndex());
            }

            int rowIndex = 1;

            for (Map<String, String> rowData : request.getRows()) {
                Row row = sheet.getRow(rowIndex);
                if (row == null)
                    row = sheet.createRow(rowIndex);

                for (Map.Entry<String, String> entry : rowData.entrySet()) {
                    Integer col = headerIndex.get(entry.getKey());
                    if (col == null)
                        continue;

                    Cell cell = row.getCell(col);
                    if (cell == null)
                        cell = row.createCell(col);

                    cell.setCellValue(entry.getValue());
                }
                rowIndex++;
            }

            try (OutputStream os = Files.newOutputStream(
                    path,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                workbook.write(os);
            }
        }
    }

}
