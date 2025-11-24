import java.sql.*;
import java.util.Scanner;

public class Search {
    private static void printMovies(ResultSet rs) throws SQLException {
        boolean hasRows = false;

        while (rs.next()) {
            hasRows = true;
            String id = rs.getString("id");
            String title = rs.getString("title");
            String company = rs.getString("company");
            Date releasedate = rs.getDate("releasedate");
            String country   = rs.getString("country");
            int totalscreen = rs.getInt("totalscreen");
            java.math.BigDecimal profit = rs.getBigDecimal("profit");
            int totalnum = rs.getInt("totalnum");
            String grade = rs.getString("grade");

            System.out.printf("[%s] | %s | %s | %s | %s | %d | %, .2f | %d | %s%n%n", id, title, company, releasedate, country, totalscreen, profit, totalnum, grade);
        }

        if (!hasRows) {
            System.out.println("검색 결과가 없습니다.");
        }
    }

    public static void searchByTitle(Scanner sc) {
        System.out.print("제목을 입력하세요: ");
        String keyword = sc.nextLine().trim();

        String sql = """
            SELECT id, title, company, releasedate, country, totalscreen, profit, totalnum, grade
            FROM movie
            WHERE title LIKE ?
            ORDER BY id;
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                printMovies(rs);
            }

        } catch (Exception e) {
            System.out.println("제목 검색 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void searchByAudience(Scanner sc) {
        System.out.print("관객 수를 입력하세요: ");
        String input = sc.nextLine().trim();

        int num;
        try {
            num = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("정수를 입력해야 합니다.");
            return;
        }

        String sql = """
            SELECT id, title, company, releasedate, country, totalscreen, profit, totalnum, grade
            FROM movie
            WHERE totalnum > ?
            ORDER BY totalnum DESC;
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, num);

            try (ResultSet rs = pstmt.executeQuery()) {
                printMovies(rs);
            }

        } catch (Exception e) {
            System.out.println("관객 수 검색 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void searchByReleaseDate(Scanner sc) {
        System.out.print("시작 날짜를 입력하세요 (YYYY-MM-DD): ");
        String start = sc.nextLine().trim();
        System.out.print("끝 날짜를 입력하세요 (YYYY-MM-DD): ");
        String end = sc.nextLine().trim();

        String sql = """
            SELECT id, title, company, releasedate, country, totalscreen, profit, totalnum, grade
            FROM movie
            WHERE releasedate BETWEEN ? AND ?
            ORDER BY releasedate, title;
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(start));
            pstmt.setDate(2, Date.valueOf(end));

            try (ResultSet rs = pstmt.executeQuery()) {
                printMovies(rs);
            }

        } catch (Exception e) {
            System.out.println("개봉일 검색 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
