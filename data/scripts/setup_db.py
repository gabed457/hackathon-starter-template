"""
setup_db.py — Data Engineer's database creation script

Creates app.db at the repo root. The Next.js app reads this file through
TypeORM. Table names, column names, and types MUST match the TypeORM entity
definitions in app/src/entities/.

Usage:
    cd data
    python scripts/setup_db.py

After running, commit and push the database so the rest of the team has it:
    git add ../app.db
    git commit -m "Update app.db"
    git push
"""

import os
import sqlite3

DB_PATH = os.path.join(os.path.dirname(__file__), "..", "..", "app.db")


def create_tables(cursor):
    """Create tables in app.db. Column names must match TypeORM entities."""

    # Hello world table — replace with your own schema on day one
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS example (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            score REAL NOT NULL
        )
    """)

    # Example — uncomment and modify for additional tables:
    #
    # cursor.execute("""
    #     CREATE TABLE IF NOT EXISTS predictions (
    #         id INTEGER PRIMARY KEY AUTOINCREMENT,
    #         example_id INTEGER NOT NULL,
    #         predicted_label TEXT NOT NULL,
    #         confidence REAL,
    #         FOREIGN KEY (example_id) REFERENCES example(id)
    #     )
    # """)


def generate_data(cursor):
    """Insert synthetic data into tables."""

    # Hello world data — replace with your own synthetic data on day one
    rows = [
        ("Alice", 0.92),
        ("Bob", 0.67),
        ("Charlie", 0.85),
    ]
    cursor.executemany(
        "INSERT INTO example (name, score) VALUES (?, ?)",
        rows,
    )


def load_predictions(cursor):
    """Load pre-computed predictions from the data scientist's CSV."""

    # Example — uncomment when the data scientist has produced predictions.csv:
    #
    # import csv
    # predictions_path = os.path.join(os.path.dirname(__file__), "..", "predictions.csv")
    # with open(predictions_path, "r") as f:
    #     reader = csv.DictReader(f)
    #     for row in reader:
    #         cursor.execute(
    #             "INSERT INTO predictions (example_id, predicted_label, confidence) VALUES (?, ?, ?)",
    #             (int(row["example_id"]), row["predicted_label"], float(row["confidence"])),
    #         )

    pass


def main():
    # Delete existing database and start fresh
    if os.path.exists(DB_PATH):
        os.remove(DB_PATH)

    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()

    create_tables(cursor)
    generate_data(cursor)
    load_predictions(cursor)

    conn.commit()
    conn.close()

    print(f"Database created at {os.path.abspath(DB_PATH)}")


if __name__ == "__main__":
    main()
