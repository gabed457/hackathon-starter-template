// Example: querying the database in a server component
//
// import { getDB } from "@/lib/datasource";
// import { Example } from "@/entities/Example";

export default async function Home() {
  // Example: fetch data from SQLite via TypeORM
  //
  // const db = await getDB();
  // const items = await db.getRepository(Example).find();

  return (
    <main>
      <h1>Hackathon</h1>
      <p>Ready to build.</p>
    </main>
  );
}
