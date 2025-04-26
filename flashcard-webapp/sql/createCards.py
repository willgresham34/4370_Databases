#!/usr/bin/env python3
import ast
import random
import mysql.connector

DB_CONFIG = {
    "host": "localhost",
    "user": "root",
    "password": "mysqlpass",
    "database": "flashcards_db",
    "port": 33306,
}
DATASET_PATH = "./sql/dataset.txt"
NUM_CARDS_TO_CREATE = 1000
SET_ID_MIN = 1
SET_ID_MAX = 20


def load_qa(path):
    qa_list = []
    with open(path, "r", encoding="utf-8") as f:
        for lineno, line in enumerate(f, start=1):
            text = line.strip()
            if not text:
                continue
            try:
                q, a = ast.literal_eval(text)
                qa_list.append((q, a))
            except Exception as e:
                print(f"Skipping line {lineno}: parse error ({e})")
    return qa_list


def main():
    qa_list = load_qa(DATASET_PATH)
    if not qa_list:
        print("No QA pairs loaded—exiting.")
        return

    conn = mysql.connector.connect(**DB_CONFIG)
    cursor = conn.cursor()
    insert_sql = """
        INSERT INTO Flashcards (setId, cardTerm, cardDesc)
        VALUES (%s, %s, %s)
    """

    print(f"Loaded {len(qa_list)} QA pairs. Generating {NUM_CARDS_TO_CREATE} cards…")
    for i in range(1, NUM_CARDS_TO_CREATE + 1):
        q, a = random.choice(qa_list)
        set_id = random.randint(SET_ID_MIN, SET_ID_MAX)
        cursor.execute(insert_sql, (set_id, q, a))

    conn.commit()
    cursor.close()
    conn.close()


if __name__ == "__main__":
    main()
