package code.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading CSV files from the resources directory.
 * Provides methods to parse CSV data into structured records.
 */
public class CSVReader {

    /**
     * Reads a CSV file from the resources directory and returns its content as a list of string arrays.
     * Each array represents a row, and each element in the array represents a column value.
     * The first row (header) is skipped.
     *
     * @param resourcePath the path to the CSV file relative to the resources directory
     * @return a list of string arrays, where each array represents a row of data
     * @throws IOException if the file cannot be read
     */
    public static List<String[]> readFromResources(String resourcePath) throws IOException {
        List<String[]> records = new ArrayList<>();
        
        InputStream inputStream = openResource(resourcePath);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Split by comma and trim whitespace from each field
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                }
                
                records.add(values);
            }
        }
        
        return records;
    }

    /**
     * Tries to open a resource via the classpath, then falls back to the src/resources directory.
     */
    private static InputStream openResource(String resourcePath) throws IOException {
        InputStream fromClasspath = CSVReader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (fromClasspath != null) {
            return fromClasspath;
        }

        java.nio.file.Path[] candidates = new java.nio.file.Path[] {
                java.nio.file.Paths.get("src", "resources", resourcePath),
                java.nio.file.Paths.get("resources", resourcePath),
                java.nio.file.Paths.get("code", resourcePath),
                java.nio.file.Paths.get(resourcePath)
        };

        for (java.nio.file.Path path : candidates) {
            if (java.nio.file.Files.exists(path)) {
                return java.nio.file.Files.newInputStream(path);
            }
        }

        throw new IOException("Resource not found: " + resourcePath);
    }

    /**
     * Reads a CSV file from an absolute file path.
     * This method is useful for reading CSV files that are not in the resources directory.
     *
     * @param filePath the absolute path to the CSV file
     * @return a list of string arrays, where each array represents a row of data
     * @throws IOException if the file cannot be read
     */
    public static List<String[]> readFromPath(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Skip header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Split by comma and trim whitespace from each field
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                }
                
                records.add(values);
            }
        }
        
        return records;
    }
}
