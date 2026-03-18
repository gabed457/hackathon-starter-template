import { NextResponse } from "next/server";
import { getDB } from "@/lib/datasource";
import { Example } from "@/entities/Example";

export async function GET() {
  const db = await getDB();
  const examples = await db.getRepository(Example).find();
  return NextResponse.json(examples);
}
