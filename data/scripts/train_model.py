"""Hello world ONNX model: predicts "high" (1) or "low" (0) from a score."""

import os
import numpy as np
from sklearn.linear_model import LogisticRegression
from skl2onnx import convert_sklearn
from skl2onnx.common.data_types import FloatTensorType
import onnxruntime as ort

# Generate training data: 100 random scores, label 1 if > 0.75
np.random.seed(42)
scores = np.random.rand(100, 1).astype(np.float32)
labels = (scores > 0.75).astype(int).ravel()

# Train
model = LogisticRegression()
model.fit(scores, labels)

# Export to ONNX (zipmap=False gives raw tensor outputs instead of dictionaries)
initial_type = [("input", FloatTensorType([None, 1]))]
options = {type(model): {"zipmap": False}}
onnx_model = convert_sklearn(model, initial_types=initial_type, options=options)

MODEL_DIR = os.path.join(os.path.dirname(__file__), "..", "models")
MODEL_PATH = os.path.join(MODEL_DIR, "model.onnx")
os.makedirs(MODEL_DIR, exist_ok=True)

with open(MODEL_PATH, "wb") as f:
    f.write(onnx_model.SerializeToString())
print("Trained model and saved to data/models/model.onnx")

# Test prediction
sess = ort.InferenceSession(MODEL_PATH)
result = sess.run(None, {"input": np.array([[0.9]], dtype=np.float32)})
pred = int(result[0][0])
print(f"Test prediction for score=0.9: {pred} ({'high' if pred == 1 else 'low'})")
