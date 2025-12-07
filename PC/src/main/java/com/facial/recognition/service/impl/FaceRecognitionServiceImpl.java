package com.facial.recognition.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.facial.recognition.dto.FaceEmbeddingResult;
import com.facial.recognition.dto.FaceRecognitionRequest;
import com.facial.recognition.dto.FaceRecognitionResult;
import com.facial.recognition.pojo.FaceData;
import com.facial.recognition.pojo.User;
import com.facial.recognition.repository.FaceDataRepository;
import com.facial.recognition.repository.UserRepository;
import com.facial.recognition.service.FaceEmbeddingClient;
import com.facial.recognition.service.FaceRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FaceRecognitionServiceImpl implements FaceRecognitionService {

    private static final double DEFAULT_THRESHOLD = 0.38; // 余弦相似度阈值，可按实际调优

    @Autowired
    private FaceEmbeddingClient faceEmbeddingClient;

    @Autowired
    private FaceDataRepository faceDataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public FaceRecognitionResult recognize(FaceRecognitionRequest request) {
        if (request == null || request.getFaceImage() == null || request.getFaceImage().isBlank()) {
            return buildError("请上传人脸图片");
        }

        byte[] imageBytes;
        try {
            imageBytes = decodeBase64(request.getFaceImage());
        } catch (IllegalArgumentException ex) {
            return buildError("图片Base64解析失败");
        }

        FaceEmbeddingResult embeddingResult;
        try {
            embeddingResult = faceEmbeddingClient.fetchEmbedding(imageBytes);
        } catch (Exception ex) {
            return buildError("调用人脸特征服务失败：" + ex.getMessage());
        }

        if (embeddingResult == null || !embeddingResult.isHasFace() || embeddingResult.getEmbedding() == null) {
            FaceRecognitionResult res = new FaceRecognitionResult();
            res.setHasFace(false);
            res.setMatched(false);
            res.setMessage("未检测到人脸");
            return res;
        }

        double[] query = toArray(embeddingResult.getEmbedding());

        List<FaceData> all = faceDataRepository.findAll();
        List<MatchCandidate> candidates = all.stream()
                .map(fd -> toCandidate(fd, query))
                .filter(c -> c != null)
                .sorted(Comparator.comparingDouble(MatchCandidate::similarity).reversed())
                .collect(Collectors.toList());

        FaceRecognitionResult result = new FaceRecognitionResult();
        result.setHasFace(true);
        result.setThreshold(DEFAULT_THRESHOLD);

        if (candidates.isEmpty()) {
            result.setMatched(false);
            result.setMessage("暂无人脸库数据");
            return result;
        }

        MatchCandidate best = candidates.get(0);
        result.setSimilarity(best.similarity);
        if (best.similarity >= DEFAULT_THRESHOLD) {
            result.setMatched(true);
            result.setUserId(best.userId);
            Optional<User> userOpt = userRepository.findById(best.userId);
            userOpt.ifPresent(u -> result.setUserName(u.getRealName()));
            result.setMessage("匹配成功");
        } else {
            result.setMatched(false);
            result.setMessage("未匹配到库内人脸");
        }

        return result;
    }

    private MatchCandidate toCandidate(FaceData fd, double[] query) {
        if (fd.getFaceTemplate() == null || fd.getFaceTemplate().isBlank()) {
            return null;
        }
        try {
            List<Double> list = objectMapper.readValue(fd.getFaceTemplate(), objectMapper.getTypeFactory().constructCollectionType(List.class, Double.class));
            double[] vec = toArray(list);
            double sim = cosineSimilarity(query, vec);
            return new MatchCandidate(fd.getUserId(), sim);
        } catch (Exception ex) {
            return null;
        }
    }

    private byte[] decodeBase64(String dataUrl) {
        String cleaned = dataUrl.trim();
        int commaIndex = cleaned.indexOf(',');
        if (commaIndex > 0) {
            cleaned = cleaned.substring(commaIndex + 1);
        }
        return Base64.getDecoder().decode(cleaned);
    }

    private double[] toArray(List<Double> list) {
        double[] arr = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    private double cosineSimilarity(double[] a, double[] b) {
        if (a == null || b == null || a.length == 0 || b.length == 0 || a.length != b.length) {
            return -1;
        }
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        if (na == 0 || nb == 0) return -1;
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    private FaceRecognitionResult buildError(String msg) {
        FaceRecognitionResult res = new FaceRecognitionResult();
        res.setHasFace(false);
        res.setMatched(false);
        res.setMessage(msg);
        return res;
    }

    private record MatchCandidate(Integer userId, double similarity) {}
}

