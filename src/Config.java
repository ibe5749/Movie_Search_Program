public class Config {
    public static final String db_host = "localhost";
    public static final String db_user = "root";
    public static final String db_password = "5749";
    public static final String db_name = "Movie_Search_Program";

    public static final String db_url = "jdbc:mysql://" + db_host + ":3306/" + db_name + "?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8";

    public static final String create_table_path = "create_table.txt";
    public static final String movie_data_path = "movie_data.txt";
}
