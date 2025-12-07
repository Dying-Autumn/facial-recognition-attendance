package com.facial.recognition.dto;

/**
 * 人脸识别请求：上传一张人脸图片（Base64 DataURL）
 */
public class FaceRecognitionRequest {
    private String faceImage;

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }
}

