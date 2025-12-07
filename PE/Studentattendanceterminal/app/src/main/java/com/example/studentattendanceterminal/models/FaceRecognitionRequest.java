package com.example.studentattendanceterminal.models;

public class FaceRecognitionRequest {
    private String faceImage;

    public FaceRecognitionRequest(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }
}

