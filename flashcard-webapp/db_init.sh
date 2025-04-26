#!/usr/bin/env bash
set -euo pipefail

# ─── CONFIGURATION ────────────────────────────────────────────────────────────
CONTAINER_NAME="mysql-server-4370" 
MYSQL_USER="root"
MYSQL_PASSWORD="mysqlpass"
DATABASE="flashcards_db"

# ─── RUN DDL & DATA SCRIPTS ──────────────────────────────────────────────────
docker exec -i "$CONTAINER_NAME" \
  mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" < sql/ddl.sql
echo "Schema applied."
docker exec -i "$CONTAINER_NAME" \
  mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" < sql/data.sql
echo "Base data seeded"

# ─── GENERATE FLASHCARDS ─────────────────────────────────────────────────────
python sql/createCards.py
echo "Flashcards generated (1000 rows inserted)."

echo "Database setup completed."
