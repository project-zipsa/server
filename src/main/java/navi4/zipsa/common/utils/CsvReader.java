package navi4.zipsa.common.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static List<String[]> read(String filePath) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirst = true;

            while ((line = br.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                String[] columns = line.split(",");
                rows.add(columns);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 리딩 실패: " + e.getMessage());
        }
        return rows;
    }
}
