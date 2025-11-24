import java.util.Scanner;

public class App {

    private static void menu() {
        System.out.println("========================================");
        System.out.println("(0) 종료");
        System.out.println("(1) 릴레이션 생성 및 데이터 추가");
        System.out.println("(2) 제목을 이용한 검색");
        System.out.println("(3) 관객수를 이용한 검색");
        System.out.println("(4) 개봉일을 이용한 검색");
        System.out.println("========================================");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            menu();
            System.out.print("원하는 번호를 입력 하시오: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else if (choice.equals("1")) {
                Database.initDb();
            } else if (choice.equals("2")) {
                Search.searchByTitle(sc);
            } else if (choice.equals("3")) {
                Search.searchByAudience(sc);
            } else if (choice.equals("4")) {
                Search.searchByReleaseDate(sc);
            } else {
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }

        sc.close();
    }
}
