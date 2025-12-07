"""
FastAPI 微服务：接收图片文件，返回人脸特征向量（embedding）。
依赖：fastapi、uvicorn、insightface、opencv-python、numpy。
启动示例：
    uvicorn face_api:app --host 0.0.0.0 --port 8000
"""
from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import uvicorn
import cv2
import numpy as np
from insightface.app import FaceAnalysis
from insightface.utils import face_align


app = FastAPI(title="FaceMind Embedding Service", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 如果本地已下载模型，设置 root 指向包含 models/buffalo_l 的目录，避免远程下载
MODEL_ROOT = r"E:\Program Files\Insightface"

# 初始化模型（可根据需要调整 name / det_size / ctx_id）
fa = FaceAnalysis(name="buffalo_l", root=MODEL_ROOT)
fa.prepare(ctx_id=0, det_size=(640, 640))


@app.post("/embed")
async def embed(file: UploadFile = File(...)):
    data = await file.read()
    frame = cv2.imdecode(np.frombuffer(data, np.uint8), cv2.IMREAD_COLOR)
    if frame is None:
        raise HTTPException(status_code=400, detail="无法读取图像")

    faces = fa.get(frame)
    if not faces:
        return {"has_face": False}

    face = faces[0]
    embedding = face.embedding.tolist()
    bbox = [int(v) for v in face.bbox]
    
    return {
        "has_face": True,
        "bbox": bbox,
        "embedding": embedding,
    }


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)

