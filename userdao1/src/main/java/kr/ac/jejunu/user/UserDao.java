package kr.ac.jejunu.user;


import java.sql.*;

public class UserDao {
    //자바의 new 키워드는 의존성이 있다고 생각함 -> JeJuConnectionMaker이 변경되면 아래 코드도 영향을 받는다.

    private final ConnectionMaker connectionMaker;

    //의존성 제거하기 위해서는 나를 이용하는 얘들에게 의존성을 던짐 -> UserDaoTests에서 사용하는 얘들에게 던짐
    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public User findById(Long id) throws ClassNotFoundException, SQLException {

        // 데이터 어딨어? mysql
        // mysql 클래스 로딩
        Connection connection = connectionMaker.getConnection();

        // 쿼리 만들고
        PreparedStatement preparedStatement = connection.prepareStatement
                ("select id, name, password from userinfo where id = ?");
        preparedStatement.setLong(1, id);

        // 쿼리 실행하고
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        // 결과를 사용자 정보에 매핑하고
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));

        // 자원해지
        resultSet.close();
        preparedStatement.close();
        connection.close();

        // 결과리턴
        return user;
    }

    public void insert(User user) throws ClassNotFoundException, SQLException {

        Connection connection = connectionMaker.getConnection();

        // 쿼리 만들고
        PreparedStatement preparedStatement = connection.prepareStatement
                ("insert into userinfo (name, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());

        // 쿼리 실행하고
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        user.setId(resultSet.getLong(1));

        // 자원해지
        resultSet.close();
        preparedStatement.close();
        connection.close();
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
        return connectionMaker.getConnection();
    }

}
