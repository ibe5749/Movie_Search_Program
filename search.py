from database import get_connection


def _print_movies(rows):
    if not rows:
        print("검색 결과가 없습니다.")
        return

    for r in rows:  # r: (id, title, company, releasedate, country, totalscreen, profit, totalnum, grade)
        print(f"[{r[0]}] | {r[1]} | {r[3]} | {r[4]} | {r[5]} | {r[7]} | {r[8]} \n")


def search_by_title():
    keyword = input("제목을 입력하세요: ").strip()

    sql = """
        SELECT id, title, company, releasedate, country, totalscreen, profit, totalnum, grade
        FROM movie
        WHERE title LIKE %s
        ORDER BY id;
    """

    con = get_connection()
    cursor = con.cursor()
    try:
        cursor.execute(sql, (f"%{keyword}%",))
        rows = cursor.fetchall()
        _print_movies(rows)
    except Exception as e:
        print("제목 검색 중 오류: ", e)
    finally:
        cursor.close()
        con.close()


def search_by_audience():
    try:
        num = int(input("관객 수를 입력하세요: ").strip())
    except ValueError:
        print("정수를 입력해야 합니다.")
        return

    sql = """
        SELECT id, title, company, releasedate, country, totalscreen, profit, totalnum, grade
        FROM movie
        WHERE totalnum > %s
        ORDER BY totalnum DESC;
    """

    con = get_connection()
    cursor = con.cursor()
    try:
        cursor.execute(sql, (num,))
        rows = cursor.fetchall()
        _print_movies(rows)
    except Exception as e:
        print("관객 수 검색 중 오류: ", e)
    finally:
        cursor.close()
        con.close()


def search_by_release_date():
    start = input("시작 날짜를 입력하세요 (YYYY-MM-DD): ").strip()
    end = input("끝 날짜를 입력하세요 (YYYY-MM-DD): ").strip()

    sql = """
        SELECT id, title, company, releasedate, country, totalscreen, profit, totalnum, grade
        FROM movie
        WHERE releasedate BETWEEN %s AND %s
        ORDER BY id;
    """

    con = get_connection()
    cursor = con.cursor()
    try:
        cursor.execute(sql, (start, end))
        rows = cursor.fetchall()
        _print_movies(rows)
    except Exception as e:
        print("개봉일 검색 중 오류: ", e)
    finally:
        cursor.close()
        con.close()
