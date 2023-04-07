package kr.ac.jejunu.user;


import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    //자바의 new 키워드는 의존성이 있다고 생각함 -> JeJuConnectionMaker이 변경되면 아래 코드도 영향을 받는다.
    private final JdbcContext jdbcContext;

    //의존성 제거하기 위해서는 나를 이용하는 얘들에게 의존성을 던짐 -> UserDaoTests에서 사용하는 얘들에게 던짐
    public UserDao(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public User findById(Long id) throws ClassNotFoundException, SQLException {
        StatementStrategy statementStrategy = new FindStatementStrategy(id);
        User user = jdbcContext.jdbcContextForFind(statementStrategy);
        return user;
    }

    public void insert(User user) throws SQLException {
        StatementStrategy statementStrategy = new InsertStatementStrategy(user);
        jdbcContext.jdbcContextForInsert(user, statementStrategy);
    }

    public void update(User user) throws SQLException {
        StatementStrategy statementStrategy = new UpdateStatementStrategy(user);
        jdbcContext.jdbcContextForUpdate(statementStrategy);
    }

    public void delete(Long id) throws SQLException {
        StatementStrategy statementStrategy = new DeleteStatementStrategy(id);
        jdbcContext.jdbcContextForUpdate(statementStrategy);
    }

//
//    private User jdbcContextForFind() throws SQLException {
//
//        return jdbcContext.jdbcContextForFind();
//    }
//
//
//    private void jdbcContextForInsert(User user, StatementStrategy statementStrategy) throws SQLException {
//        jdbcContext.jdbcContextForInsert(user, statementStrategy);
//    }

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

//    private void jdbcContextForUpdate(StatementStrategy statementStrategy) throws SQLException {
//
//        jdbcContext.jdbcContextForUpdate(statementStrategy);
//    }
}

//변하는것과 변하지 않는것을 감지하고 변화는 것을 Extract Method를 했는데 막상보니 얘는 또 다른걸로 바뀔수가있어
// 그래서 한단계 높은 class 레벨로 추상화 => * 인터페이스 *