# app.py
from database import init_db
from search import search_by_title, search_by_audience, search_by_release_date


def menu():
    print("========================================")
    print("(0) 종료")
    print("(1) 릴레이션 생성 및 데이터 추가")
    print("(2) 제목을 이용한 검색")
    print("(3) 관객수를 이용한 검색")
    print("(4) 개봉일을 이용한 검색")
    print("========================================")


def main():
    while True:
        menu()
        choice = input("원하는 번호를 입력 하시오: ").strip()

        if choice == "0":
            print("프로그램을 종료합니다.")
            break
        elif choice == "1":
            init_db()
        elif choice == "2":
            search_by_title()
        elif choice == "3":
            search_by_audience()
        elif choice == "4":
            search_by_release_date()
        else:
            print("잘못된 입력입니다. 다시 선택하세요.")


if __name__ == "__main__":
    main()
