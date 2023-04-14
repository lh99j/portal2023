package kr.ac.jejunu.user;


import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    private final JdbcContext jdbcContext;

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

}

//변하는것과 변하지 않는것을 감지하고 변화는 것을 Extract Method를 했는데 막상보니 얘는 또 다른걸로 바뀔수가있어
// 그래서 한단계 높은 class 레벨로 추상화 => * 인터페이스 *