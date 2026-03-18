import { NextResponse } from "next/server";
import * as ort from "onnxruntime-node";
import path from "path";
import fs from "fs";

// ============================================================================
// Option A — Pre-computed predictions (simpler)
// ============================================================================
// The data scientist runs batch predictions in Python and outputs a CSV.
// The data engineer loads those rows into a "predictions" table in app.db.
// See /api/examples for the pre-computed hello world.
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
// Option B — Live ONNX inference (working hello world below)
// ============================================================================

const MODEL_PATH = path.join(process.cwd(), "..", "data", "models", "model.onnx");
const LABELS: Record<number, string> = { 0: "low", 1: "high" };

let session: ort.InferenceSession | null = null;

async function getSession(): Promise<ort.InferenceSession> {
  if (!session) {
    session = await ort.InferenceSession.create(MODEL_PATH);
  }
  return session;
}

export async function GET() {
  return NextResponse.json({
    methods: {
      pre_computed: "GET /api/examples returns predictions from the database",
      live: 'POST /api/predict with {"score": 0.9} returns a live model prediction',
    },
  });
}

export async function POST(request: Request) {
  if (!fs.existsSync(MODEL_PATH)) {
    return NextResponse.json(
      { error: "Model not found. Run: cd data && python scripts/train_model.py" },
      { status: 500 },
    );
  }

  const { score } = await request.json();
  const sess = await getSession();
  const inputTensor = new ort.Tensor("float32", Float32Array.from([score]), [1, 1]);
  const results = await sess.run({ input: inputTensor });
  const prediction = Number(results.label.data[0]);

  return NextResponse.json({
    score,
    prediction,
    label: LABELS[prediction] ?? String(prediction),
  });
}
