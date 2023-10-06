package com.no3.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {
    // 실제 파일과 관련된 모든 정보를 가지고 있음

    private String fileName;
    private String uuid;
    private String folderPath;

    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath + "/" + uuid + "_" + fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    } // 전체 경로가 필요한 경우를 대비
    
    public String getThumbnailURL() {
        try {
            return URLEncoder.encode(folderPath + "/s_" + uuid + "_" + fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    } // 기존 이미지에 's_' 추가, 섬네일의 링크를 처리하기 위한 메서드

}
