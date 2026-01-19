package com.example.backend.entity;

import java.util.List;
import java.util.Map;

public class ExelBody {
    private String filename;

    // header → value
    private List<Map<String, String>> rows;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
    }
}