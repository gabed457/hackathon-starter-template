// ============================================================================
// Example TypeORM Entity
// ============================================================================
// Column names and types MUST match the data engineer's Python CREATE TABLE
// statement. Coordinate at kickoff.
//
// After uncommenting:
// 1. Update the class name, table name, and columns to match your schema
// 2. Register this entity in src/lib/datasource.ts by adding it to the
//    entities array: import { Example } from "@/entities/Example";
// ============================================================================

// import { Entity, PrimaryGeneratedColumn, Column } from "typeorm";
//
// @Entity("examples") // table name in app.db
// export class Example {
//   @PrimaryGeneratedColumn()
//   id: number;
//
//   @Column()
//   name: string;
//
//   @Column({ type: "float", nullable: true })
//   value: number;
//
//   @Column({ type: "text", nullable: true })
//   category: string;
// }
