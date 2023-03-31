package kr.ac.jejunu.user;


import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    //자바의 new 키워드는 의존성이 있다고 생각함 -> JeJuConnectionMaker이 변경되면 아래 코드도 영향을 받는다.

    private final DataSource dataSource;

    //의존성 제거하기 위해서는 나를 이용하는 얘들에게 의존성을 던짐 -> UserDaoTests에서 사용하는 얘들에게 던짐
    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User findById(Long id) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user;

        try {
            // 데이터 어딨어? mysql
            // mysql 클래스 로딩
            connection = dataSource.getConnection();

            // 쿼리 만들고
            preparedStatement = connection.prepareStatement
                    ("select id, name, password from userinfo where id = ?");
            preparedStatement.setLong(1, id);

            // 쿼리 실행하고
            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            // 결과를 사용자 정보에 매핑하고
            user = new User();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));
        } finally {
            // 자원해지 -> 만약 SQLException이 일어날시 밑의 코드는 실행하지 않기때문에 finally를 통해 무조건 실행하게 만듬
            // 오류가나서 자원해지를 하지 않는데 오류가 쌓이고 쌓이다보면 자원이 누적되어 오류
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // 결과리턴
        return user;
    }

    public void insert(User user) throws ClassNotFoundException, SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            // 쿼리 만들고
            preparedStatement = connection.prepareStatement
                    ("insert into userinfo (name, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());

            // 쿼리 실행하고
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            user.setId(resultSet.getLong(1));
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // 자원해지
    }

    //정확히 매칭되는 중복되는 코드가 있다면 Extract (옵션 + 커맨드 + M)
//    private Connection getConnection() throws ClassNotFoundException, SQLException {
//        // 데이터 어딨어? mysql
//        // mysql 클래스 로딩
//        Class.forName("com.mysql.cj.jdbc.Driver");
//
//        // Connection 맺고
//        Connection connection = DriverManager.getConnection
//                ("jdbc:mysql://localhost:3306/jeju", "root", "Flrnl3570@");
//        return connection;
//    }

    //모르면 추상화해라 (추상 클래스, 추상 매소드 등) -> 한래다 db가 올지 제주대 db가 올지 모르기에 추상화
//    abstract public Connection getConnection() throws ClassNotFoundException, SQLException ;

    //같은 클래스 추상화는 abstract, 다른 클래스에 사용한느 것을 추상화하면 interface
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // 데이터 어딨어? mysql
        // mysql 클래스 로딩

        // Connection 맺고
        return dataSource.getConnection();
    }

}
