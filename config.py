from pathlib import Path

db_config = {
    "host": "localhost",
    "user": "root",
    "password": "5749",
    "db": "Movie_Search_Program",
    "charset": "utf8"
}

base = Path(__file__).resolve().parent
create_table_path = base / "create_table.txt"
movie_data_path = base / "movie_data.txt"