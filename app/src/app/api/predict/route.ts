import { NextResponse } from "next/server";

// ============================================================================
// Option A — Pre-computed predictions (simpler)
// ============================================================================
// The data scientist runs batch predictions in Python and outputs a CSV.
// The data engineer loads those rows into a "predictions" table in app.db.
// This endpoint just queries that table.
//
// import { getDB } from "@/lib/datasource";
// import { Prediction } from "@/entities/Prediction";
//
// export async function GET() {
//   const db = await getDB();
//   const predictions = await db.getRepository(Prediction).find();
//   return NextResponse.json(predictions);
// }

// ============================================================================
// Option B — Live ONNX inference (more impressive)
// ============================================================================
// The data scientist exports a trained model to ONNX format.
// This endpoint loads the .onnx file and runs predictions on demand.
// Install first: npm install onnxruntime-node
//
// import * as ort from "onnxruntime-node";
// import path from "path";
//
// let session: ort.InferenceSession | null = null;
//
// async function getSession() {
//   if (!session) {
//     const modelPath = path.join(process.cwd(), "..", "data", "models", "model.onnx");
//     session = await ort.InferenceSession.create(modelPath);
//   }
//   return session;
// }
//
// export async function POST(request: Request) {
//   const { features } = await request.json();
//   // features should be an array of numbers, e.g. [1.0, 2.5, 3.0, 4.1]
//
//   const session = await getSession();
//   const inputTensor = new ort.Tensor("float32", Float32Array.from(features), [1, features.length]);
//   const results = await session.run({ input: inputTensor });
//   const prediction = results.output.data;
//
//   return NextResponse.json({ prediction: Array.from(prediction) });
// }

// ============================================================================
// Placeholder — keeps the route valid and the smoke test passing
// ============================================================================

export async function GET() {
  return NextResponse.json({ status: "predict endpoint ready" });
}
