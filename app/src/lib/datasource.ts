import "reflect-metadata";
import path from "path";
import { DataSource } from "typeorm";

// Import your entities here:
// import { Example } from "@/entities/Example";

const AppDataSource = new DataSource({
  type: "better-sqlite3",
  database: path.join(process.cwd(), "..", "app.db"),
  synchronize: false, // The data engineer owns the schema. Flip to true temporarily if you need to iterate before the data engineer builds the database.
  logging: false,
  entities: [
    // Add your entity classes here:
    // Example,
  ],
});

let initialized = false;

/**
 * Returns an initialized DataSource. Uses a singleton pattern so the
 * connection is reused across hot reloads in development.
 */
export async function getDB(): Promise<DataSource> {
  if (!initialized) {
    await AppDataSource.initialize();
    initialized = true;
  }
  return AppDataSource;
}
