# Hackathon Starter Template

A local-first hackathon starter for a distributed team of 4. Three languages, one shared database, zero cloud infrastructure.

**First time? Find your role below and follow the steps. The whole team should be up and running in under 5 minutes.**

## The Team

| Role               | Language   | Folder   | What you own                                                     |
| ------------------ | ---------- | -------- | ---------------------------------------------------------------- |
| Data Engineer      | Python     | `data/`  | Database schema, synthetic data, loading predictions into app.db |
| Data Scientist     | Python     | `data/`  | Jupyter notebooks, model training, predictions CSV or ONNX model |
| Fullstack Engineer | TypeScript | `app/`   | Next.js application, pages, API routes, TypeORM entities         |
| QA Engineer        | Java       | `tests/` | REST Assured API tests, exploratory testing, demo preparation    |

## How It Works

The entire system connects through one file: `app.db` at the repo root. It's a SQLite database — a single file, no server, no setup. The Data Engineer creates and populates it using Python. The Next.js app reads from it using TypeORM. It's committed to Git so everyone gets the same data on `git pull`.

The data flows in one direction. The Data Engineer creates tables and generates synthetic data, writing `app.db`. The Data Scientist reads `app.db` from Jupyter, trains models, and outputs predictions as CSV or ONNX. The Data Engineer loads those predictions back into `app.db`. The Fullstack Engineer's app reads `app.db` and serves it through pages and API routes. The QA Engineer tests those API routes with REST Assured.

ML integration has two paths. Pre-computed predictions: the Data Scientist outputs a CSV, the Data Engineer loads its rows into `app.db`, and the app queries them like normal data. Live inference: the Data Scientist exports a model to ONNX format, and the Fullstack Engineer loads the `.onnx` file in a Next.js API route using `onnxruntime-node` for real-time predictions. Pre-computed is simpler. Live inference is more impressive for demos. Both paths are demonstrated in this template — pre-computed predictions are in the `/api/examples` endpoint, and live ONNX inference is in the `POST /api/predict` endpoint.

The only coordination required: the team agrees on the database schema (table names, column names, types) at kickoff. That's a 15-minute conversation. After that, everyone works independently in their folder.

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
│       │   └── api/
│       │       ├── examples/route.ts
│       │       └── predict/route.ts
│       ├── entities/
│       │   └── Example.ts
│       └── lib/
│           └── datasource.ts
├── data/                         ← Data Engineer + Data Scientist (Python)
│   ├── requirements.txt
│   ├── MODEL_CONTRACT.md
│   ├── scripts/
│   │   ├── setup_db.py
│   │   ├── modeling.py
│   │   └── train_model.py
│   ├── notebooks/
│   └── models/
│       └── model.onnx
├── tests/                        ← QA Engineer (Java/Maven)
│   ├── pom.xml
│   └── src/test/java/hackathon/
│       └── SmokeTest.java
├── app.db                        ← THE shared database
├── .gitignore
└── README.md
```

## Verify the Full Stack

**Run these steps in order. Each role takes about 1-2 minutes. By the end, you'll have data flowing from Python through SQLite into a Next.js app, verified by Java API tests.**

### Step 1: Data Engineer — Create the Database

Prerequisites: Python 3.8+ (`python3 --version` to check)

```
cd data
pip install -r requirements.txt
python scripts/setup_db.py
```

You should see: `Created <path>/app.db`

Verify the data exists:

```
python -c "import sqlite3; conn = sqlite3.connect('../app.db'); print(conn.execute('SELECT * FROM example').fetchall()); conn.close()"
```

You should see: `[(1, 'Alice', 0.92), (2, 'Bob', 0.67), (3, 'Charlie', 0.85)]`

*If the script errors on import, make sure you're in the `data/` directory.*

### Step 2: Data Scientist — Verify Data Access

Prerequisites: Step 1 complete, Python 3.8+

```
cd data
python scripts/modeling.py
```

You should see: A pandas DataFrame printed with 3 rows, followed by `Loaded 3 rows from app.db`.

To verify Jupyter works (optional):

```
jupyter notebook
```

Create a new notebook in `notebooks/` and run:

```python
import sqlite3, pandas as pd
conn = sqlite3.connect("../../app.db")
df = pd.read_sql("SELECT * FROM example", conn)
conn.close()
df
```

You should see: A rendered DataFrame table with Alice, Bob, and Charlie.

*If `ModuleNotFoundError` for pandas, re-run `pip install -r requirements.txt`.*

### Step 3: Fullstack Engineer — Start the App

Prerequisites: Step 1 complete, Node.js 18+ (`node --version` to check)

```
cd app
npm install
npm run dev
```

You should see: `- Local: http://localhost:3000`

Open http://localhost:3000 in your browser.

You should see: A heading "Hackathon" with two sections. The first shows pre-computed predictions from the database (Alice, Bob, Charlie with scores). The second has an input and button for live ONNX predictions — enter a score, click Predict, and see the model's response.

Verify the API:

```
curl http://localhost:3000/api/examples
```

You should see: A JSON array with 3 objects.

Verify live predictions:

```
curl -X POST http://localhost:3000/api/predict \
  -H "Content-Type: application/json" \
  -d '{"score": 0.9}'
```

You should see: `{"score":0.9,"prediction":1,"label":"high"}`

*If the page shows no data, check that `app.db` exists at the repo root (one level above `app/`). If you get a native module error for better-sqlite3, make sure you have Node 18+ and C++ build tools installed.*

**Keep the dev server running for Step 4.**

### Step 4: QA Engineer — Run the Tests

Prerequisites: Steps 1 and 3 complete, app running at localhost:3000, Java 17+ and Maven installed (`java --version` and `mvn --version` to check)

```
cd tests
mvn test
```

You should see:

```
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

*If you get connection refused, the app isn't running — go back to Step 3. If Maven can't download dependencies, check your internet connection (first run needs to fetch JARs).*

### All steps passed?

The full stack works. Data flows from Python → SQLite → Next.js → REST Assured. Delete the hello world code and start building your project.

## Viewing the Database

Install both of these VS Code extensions:

- **SQLite Viewer** (Florian Klampfer) — click `app.db` in the file explorer and it opens as a spreadsheet. No SQL needed.
- **SQLite** (alexcvzz) — write and run SQL queries. Open the command palette (`Cmd+Shift+P`), run "SQLite: Open Database", select `app.db`. A "SQLITE EXPLORER" section appears at the bottom of the Explorer sidebar. Right-click a table to view data or write queries. Run queries with `Cmd+Shift+Q`.

Other options:

- **Desktop app**: Download DB Browser for SQLite (free) from sqlitebrowser.org
- **Python**: `pd.read_sql("SELECT * FROM tablename", sqlite3.connect("app.db"))`

## Day-to-Day Workflow

1. Team agrees on the database schema at kickoff (table names, column names, types)
2. Data Engineer updates `setup_db.py` with real tables and data, runs it, commits `app.db`
3. Data Scientist pulls, explores data in Jupyter, trains models, outputs `predictions.csv` or `model.onnx`
4. Data Engineer loads predictions into `app.db`, commits and pushes
5. Fullstack Engineer pulls, creates TypeORM entities matching the schema, builds UI and API routes
6. QA Engineer writes REST Assured tests against the API, does exploratory testing, prepares the demo
7. Demo day: clone on demo laptop → `cd app && npm install && npm run dev` → everything works offline

## The Contract

The Data Engineer's Python `CREATE TABLE` statements and the Fullstack Engineer's TypeORM entity definitions must have matching table names, column names, and types. This is the only cross-team dependency. Agree on it at kickoff, write it on a shared doc or Slack message, and both sides implement against it.