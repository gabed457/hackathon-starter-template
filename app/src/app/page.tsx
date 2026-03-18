import { getDB } from "@/lib/datasource";
import { Example } from "@/entities/Example";

export default async function Home() {
  const db = await getDB();
  const items = await db.getRepository(Example).find();

  return (
    <main>
      <h1>Hackathon</h1>
      <ul>
        {items.map((item) => (
          <li key={item.id}>
            {item.name}: {item.score}
          </li>
        ))}
      </ul>
    </main>
  );
}
