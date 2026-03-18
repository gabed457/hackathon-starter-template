// ============================================================================
// Example TypeORM Entity — Hello World
// ============================================================================
// Column names and types MUST match the data engineer's Python CREATE TABLE
// statement. Coordinate at kickoff.
//
// This hello world entity matches the "example" table created by
// data/scripts/setup_db.py. Replace with your own schema on day one.
// ============================================================================

import { Entity, PrimaryGeneratedColumn, Column } from "typeorm";

@Entity("example") // table name in app.db
export class Example {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  name: string;

  @Column({ type: "real" })
  score: number;
}
