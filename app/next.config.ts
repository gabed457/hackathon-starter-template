import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  serverExternalPackages: ["better-sqlite3", "typeorm", "reflect-metadata", "onnxruntime-node"],
};

export default nextConfig;
