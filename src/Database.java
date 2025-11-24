import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(Config.db_url, Config.db_user, Config.db_password);
    }

    public static void createMovieTable() {
        try {
            String sql = Files.readString(Paths.get(Config.create_table_path), StandardCharsets.UTF_8);

            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate("DROP TABLE IF EXISTS movie;");
                stmt.executeUpdate(sql);

                System.out.println("movie 테이블 생성 완료");

            }
        } catch (Exception e) {
            System.out.println("테이블 생성 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String[] parseMovieLine(String row) {
        row = row.trim();
        if (row.isEmpty()) {
            return null;
        }

        String[] raw_entrys = row.split("\\|");
        List<String> entrys = new ArrayList<>();

        for (String e : raw_entrys) {
            if (!e.isEmpty()) {
                entrys.add(e.trim());
            }
        }
        if (entrys.size() != 9) {
            System.out.println("이 행은 올바르지 않습니다: " + row);
            return null;
        }

        // id, title, company, releasedate, country, totalscreen, profit, totalnum, grade
        return entrys.toArray(new String[0]);
    }

    public static void insertMovieData() {
        List<String[]> rows = new ArrayList<>();

        Charset cp949 = Charset.forName("MS949");

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(Config.movie_data_path), cp949
                )
        )) {
            String row;
            while ((row = br.readLine()) != null) {
                String[] parsed = parseMovieLine(row);
                if (parsed != null) {
                    rows.add(parsed);
                }
            }
        } catch (Exception e) {
            System.out.println("movie_data.txt 읽는 중 오류: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (rows.isEmpty()) {
            System.out.println("삽입할 데이터가 없습니다.");
            return;
        }

        String insertSql = """
            INSERT INTO movie
            (id, title, company, releasedate, country, totalscreen, profit, totalnum, grade)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            conn.setAutoCommit(false);

            for (String[] r : rows) {
                String id = r[0];
                String title = r[1];
                String company = r[2];
                String releasedate = r[3];
                String country = r[4];
                String totalscreen = r[5];
                String profit = r[6];
                String totalnum = r[7];
                String grade = r[8];

                pstmt.setString(1, id);
                pstmt.setString(2, title);
                pstmt.setString(3, company);
                pstmt.setDate(4, Date.valueOf(releasedate));
                pstmt.setString(5, country);
                pstmt.setInt(6, Integer.parseInt(totalscreen));
                pstmt.setBigDecimal(7, new java.math.BigDecimal(profit));
                pstmt.setInt(8, Integer.parseInt(totalnum));
                pstmt.setString(9, grade);

                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();
            System.out.println(rows.size() + "개의 데이터 삽입 완료");

        } catch (Exception e) {
            System.out.println("데이터 삽입 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void initDb() {
        createMovieTable();
        insertMovieData();
    }
}
