import pymysql
from config import db_config, create_table_path, movie_data_path


def get_connection():
    return pymysql.connect(**db_config)


def create_movie_table():
    sql = create_table_path.read_text(encoding="utf-8")

    con = get_connection()
    cursor = con.cursor()
    try:
        cursor.execute("DROP TABLE IF EXISTS movie;")
        cursor.execute(sql)
        con.commit()
        print("movie 테이블 생성 완료")
    except Exception as e:
        print("테이블 생성 중 오류: ", e)
        con.rollback()
    finally:
        cursor.close()
        con.close()


def _parse_movie_line(row: str):
    row = row.strip()
    if not row:
        return None

    raw_entrys = row.split("|")
    entrys = []
    for e in raw_entrys:
        if e != "":
            entrys.append(e)

    if len(entrys) != 9:
        print("이 행은 올바르지 않습니다: ", row)
        return None

    id = entrys[0]
    title = entrys[1]
    company = entrys[2]
    releasedate = entrys[3]
    country = entrys[4]
    totalscreen = entrys[5]
    profit = entrys[6]
    totalnum = entrys[7]
    grade = entrys[8]

    return id, title, company, releasedate, country, totalscreen, profit, totalnum, grade


def insert_movie_data():
    rows = []

    with movie_data_path.open("r", encoding="cp949") as f:
        for row in f:
            row = _parse_movie_line(row)
            if row is not None:
                rows.append(row)
    if not rows:
        print("삽입할 데이터가 없습니다.")
        return

    insert_sql = """
        INSERT INTO movie 
            (id, title, company, releasedate, country, totalscreen, profit, totalnum, grade) 
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s);
    """

    con = get_connection()
    cursor = con.cursor()
    try:
        cursor.executemany(insert_sql, rows)
        con.commit()
        print(f"{len(rows)}개의 데이터 삽입 완료")
    except Exception as e:
        print("데이터 삽입 중 오류: ", e)
        con.rollback()
    finally:
        cursor.close()
        con.close()


def init_db():
    create_movie_table()
    insert_movie_data()
