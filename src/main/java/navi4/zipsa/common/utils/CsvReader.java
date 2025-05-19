package navi4.zipsa.common.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static List<String[]> read(InputStream inputStream) throws IOException {
//        List<String[]> rows = new ArrayList<>();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            boolean isFirst = true;
//
//            while ((line = br.readLine()) != null) {
//                if (isFirst) {
//                    isFirst = false;
//                    continue;
//                }
//
//                String[] columns = line.split(",");
//                rows.add(columns);
//            }
//        } catch (IOException e) {
//            throw new IllegalArgumentException("파일 리딩 실패: " + e.getMessage());
//        }
//        return rows;

        boolean isFirstLine = true;
        List<String[]> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // 첫 줄 헤더는 건너뜀
                }
                result.add(line.split(","));
            }
        }
        return result;
    }
}
