package navi4.zipsa.common.utils;

import navi4.zipsa.common.dto.Base64Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

// TODO: 이거 왜 만들었었지
public class ImageExtractor {

    public static Base64Image extractImageToBase64(MultipartFile imageFile) {
        try{
            String base64 = Base64.getEncoder().encodeToString(imageFile.getBytes());
            String originalFilename = imageFile.getOriginalFilename();
            String contentType = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
            return new Base64Image(base64, contentType, originalFilename);
        } catch (Exception e) {
            throw new IllegalArgumentException("이미지 추출 실패");
        }
    }
}
