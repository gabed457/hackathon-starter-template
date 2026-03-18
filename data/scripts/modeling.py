"""
modeling.py — Data Scientist's training workflow

This script can be run directly or copied cell-by-cell into a Jupyter notebook.
It loads data from app.db, trains a model, and saves predictions.

Usage:
    cd data
    python scripts/modeling.py
"""

import os

DB_PATH = os.path.join(os.path.dirname(__file__), "..", "..", "app.db")
PREDICTIONS_PATH = os.path.join(os.path.dirname(__file__), "..", "predictions.csv")
MODEL_PATH = os.path.join(os.path.dirname(__file__), "..", "models", "model.onnx")


def load_data():
    """Load data from app.db into a DataFrame."""

    # Example — uncomment and modify:
    #
    # import pandas as pd
    # import sqlite3
    #
    # conn = sqlite3.connect(DB_PATH)
    # df = pd.read_sql("SELECT * FROM examples", conn)
    # conn.close()
    # return df

    pass


def train_model(df):
    """Train a model on the data."""

    # Example — uncomment and modify:
    #
    # from sklearn.model_selection import train_test_split
    # from sklearn.ensemble import RandomForestClassifier
    #
    # X = df[["value"]].values
    # y = df["category"].values
    #
    # X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    #
    # model = RandomForestClassifier(n_estimators=100, random_state=42)
    # model.fit(X_train, y_train)
    #
    # accuracy = model.score(X_test, y_test)
    # print(f"Test accuracy: {accuracy:.3f}")
    #
    # return model

    pass


def save_predictions(model, df):
    """Option A: Save batch predictions to CSV for the data engineer to load."""

    # Example — uncomment and modify:
    #
    # import pandas as pd
    #
    # X = df[["value"]].values
    # predictions = model.predict(X)
    # probabilities = model.predict_proba(X).max(axis=1)
    #
    # results = pd.DataFrame({
    #     "example_id": df["id"],
    #     "predicted_label": predictions,
    #     "confidence": probabilities.round(3),
    # })
    # results.to_csv(PREDICTIONS_PATH, index=False)
    # print(f"Predictions saved to {PREDICTIONS_PATH}")

    pass


def save_onnx(model):
    """Option B: Export model to ONNX format for live inference in Next.js."""

    # Example — uncomment and modify:
    # Requires: pip install skl2onnx onnxruntime
    #
    # from skl2onnx import convert_sklearn
    # from skl2onnx.common.data_types import FloatTensorType
    #
    # initial_type = [("input", FloatTensorType([None, 1]))]  # adjust shape to match your features
    # onnx_model = convert_sklearn(model, initial_types=initial_type)
    #
    # with open(MODEL_PATH, "wb") as f:
    #     f.write(onnx_model.SerializeToString())
    # print(f"ONNX model saved to {MODEL_PATH}")

    pass


if __name__ == "__main__":
    df = load_data()
    model = train_model(df)
    save_predictions(model, df)
    # save_onnx(model)  # Uncomment for Option B (live ONNX inference)
