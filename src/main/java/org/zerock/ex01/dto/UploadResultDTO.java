package org.zerock.ex01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO implements Serializable {

    private String fileName;
    private String uuid;
    private String folderPath;
    private String realImageUrl;
    private Long inum;
//    public String getImageURL() {
//        try {
//            return URLEncoder.encode("http://172.16.33.55:9111/getImages/"+folderPath + "/" + uuid + "_" + fileName, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}
