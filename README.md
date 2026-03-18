# Hackathon Starter Template

A hackathon starter template for a **distributed team of 4** working across three languages with one shared SQLite database. Designed for a 2-day hackathon where everyone works on their own machine and demos from a single laptop.

## The Stack

| Component          | Technology                        | Owner                  |
| ------------------ | --------------------------------- | ---------------------- |
| Frontend + Backend | Next.js 15 (TypeScript)           | Fullstack Engineer     |
| ORM                | TypeORM + better-sqlite3          | Fullstack Engineer     |
| Database           | SQLite (`app.db` at repo root)    | Data Engineer          |
| Data + ML          | Python (pandas, scikit-learn)     | Data Engineer / Scientist |
| API Testing        | Maven + REST Assured + TestNG     | QA Engineer            |
| ML Inference       | ONNX Runtime (optional)           | Data Scientist         |

## How It Connects

```
Data Engineer         Data Scientist          Fullstack Engineer       QA Engineer
   (Python)              (Python)               (Next.js)            (Java/Maven)
      │                     │                       │                      │
      │  creates tables     │                       │                      │
      │  generates data     │                       │                      │
      │  writes ─────────► app.db ◄──── reads ──────┤                      │
      │                        ▲                    │                      │
      │                        │                    │                      │
      │                  predictions.csv            │                      │
      │                  or model.onnx              │                      │
      │                        │                    │                      │
      │                        └── loaded into ─────┘                      │
      │                           the app                                  │
      │                                             │                      │
      │                               localhost:3000 ◄──── tests via ──────┘
      │                                             │      HTTP requests
```

**ML model integration has two paths:**

- **Pre-computed predictions (simpler):** Data scientist runs batch predictions in Python, outputs a CSV. Data engineer loads the CSV rows into a predictions table in `app.db`. The app queries predictions like any other data.
- **Live inference (more impressive):** Data scientist exports the trained model to ONNX format. Fullstack engineer loads the `.onnx` file in a Next.js API route using `onnxruntime-node` and runs predictions on demand. Optional — only use if the project needs real-time predictions.

## Folder Structure

```
hackathon/
├── app/                          ← Fullstack Engineer (TypeScript)
│   ├── package.json
│   ├── next.config.ts
│   ├── tsconfig.json
│   ├── .babelrc
│   ├── public/
│   └── src/
│       ├── app/
│       │   ├── layout.tsx
│       │   ├── page.tsx
│       │   ├── globals.css
│       │   └── api/predict/route.ts
│       ├── entities/
│       │   └── Example.ts
│       └── lib/
│           └── datasource.ts
├── data/                         ← Data Engineer + Data Scientist (Python)
│   ├── requirements.txt
│   ├── scripts/
│   │   ├── setup_db.py
│   │   └── modeling.py
│   ├── notebooks/
│   └── models/
├── tests/                        ← QA Engineer (Java/Maven)
│   ├── pom.xml
│   └── src/test/java/hackathon/
│       └── SmokeTest.java
├── app.db                        ← THE shared database
├── .gitignore
└── README.md
```

## Getting Started

### Fullstack Engineer

```bash
cd app
npm install
npm run dev
```

The app runs at [http://localhost:3000](http://localhost:3000) and reads `../app.db` (one level up from the `app/` folder). The database must exist before the app can query it — the data engineer creates it.

### Data Engineer

```bash
cd data
pip install -r requirements.txt
python scripts/setup_db.py
```

This creates `app.db` at the repo root. After updating the database, push it to Git so the rest of the team gets fresh data:

```bash
git add ../app.db
git commit -m "Update app.db"
git push
```

### Data Scientist

```bash
cd data
pip install -r requirements.txt
jupyter notebook
```

Work in `notebooks/`. Query `app.db` directly with sqlite3 or pandas:

```python
import pandas as pd
import sqlite3

conn = sqlite3.connect("../app.db")
df = pd.read_sql("SELECT * FROM your_table", conn)
```

### QA Engineer

```bash
cd tests
mvn test
```

Runs REST Assured tests against [http://localhost:3000](http://localhost:3000). The app must be running first and `app.db` must exist.

## How to View the Database

Three options:

1. **VS Code SQLite Viewer** — Install the extension, then click `app.db` in the file explorer. Tables appear as spreadsheet tabs.
2. **DB Browser for SQLite** — Free desktop app ([sqlitebrowser.org](https://sqlitebrowser.org)). Open `app.db` to browse, query, and edit.
3. **Python** — `pd.read_sql("SELECT * FROM table_name", sqlite3.connect("app.db"))` in a notebook or script.

## The Contract

The team must agree on the **database schema** at kickoff:

- Table names
- Column names
- Column types

This is the only required coordination. The data engineer's Python `CREATE TABLE` statements must match the fullstack engineer's TypeORM entity definitions. Once agreed, each person works independently in their own folder and language.

## The Workflow

1. **Agree on schema** — The whole team agrees on table names, column names, and types at kickoff.
2. **Data engineer creates `app.db`** — Runs `setup_db.py` to create tables and generate synthetic data. Commits and pushes `app.db`.
3. **Data scientist explores and trains models** — Works in Jupyter notebooks, queries `app.db`, trains models, outputs predictions to CSV or ONNX.
4. **Data engineer loads predictions** — Reads the data scientist's CSV and inserts prediction rows into `app.db`. Commits and pushes.
5. **Fullstack engineer builds UI and API** — Creates TypeORM entities matching the schema, builds pages and API routes that read from `app.db`.
6. **QA engineer writes tests and owns the demo** — Writes REST Assured tests against the API, does exploratory testing, prepares the demo flow.
7. **Demo day** — On the demo laptop: `git clone`, `cd app`, `npm install`, `npm run dev`. The database is already in the repo. Everything works offline.
