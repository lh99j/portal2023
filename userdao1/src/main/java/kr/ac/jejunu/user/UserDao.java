package kr.ac.jejunu.user;


import javax.sql.DataSource;
import java.sql.*;

public class UserDao {
    private final JdbcContext jdbcContext;

    public UserDao(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public User findById(Long id) throws SQLException {
        //변하지 않는 것은 Object가 늘어나지 않지만 변하는 녀석은 Object가 늘어남
// StatementStrategy 얘들은 매소드가 하나밖에 없기때문에 굳이 클래스로 늘릴 필요가 없다.

//        StatementStrategy statementStrategy = new StatementStrategy(){
//            //마치 파라미터로 매소드를 넘기는것과 같은 느낌 => * 마지 함수형 언어 *처럼
//            //매소드를 인자로 던져놓고 필요할때 호출한다 => Js의 콜백이랑 유사
//            // * Template Callback Method Pattern *
//            //하지만 이렇게하면 코드가 길어지고 보기싫기에 자바 5.0부터 나온 람다식 사용
//            @Override
//            public PreparedStatement makeStatement(Connection connection) throws SQLException {
//                return null;
//            }
//        };

        //람다식 사용
        StatementStrategy statementStrategy = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("select id, name, password from userinfo where id = ?");
            preparedStatement.setLong(1, id);

            return preparedStatement;
        };

        return jdbcContext.jdbcContextForFind(statementStrategy);
    }

    public void insert(User user) throws SQLException {
//        StatementStrategy statementStrategy = new InsertStatementStrategy(user);
//        jdbcContext.jdbcContextForInsert(user, statementStrategy);

        StatementStrategy statementStrategy = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("insert into userinfo (name, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());

            return preparedStatement;
        };

        jdbcContext.jdbcContextForInsert(user, statementStrategy);
    }

    public void update(User user) throws SQLException {
//        StatementStrategy statementStrategy = new UpdateStatementStrategy(user);
//        jdbcContext.jdbcContextForUpdate(statementStrategy);

        StatementStrategy statementStrategy = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("update userinfo set name = ?, password = ? where id = ?");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setLong(3, user.getId());
            return preparedStatement;
        };

        jdbcContext.jdbcContextForUpdate(statementStrategy);
    }

    public void delete(Long id) throws SQLException {
//        StatementStrategy statementStrategy = new DeleteStatementStrategy(id);
//        jdbcContext.jdbcContextForUpdate(statementStrategy);

        StatementStrategy statementStrategy = connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from userinfo where id = ?");
            preparedStatement.setLong(1, id);
            return preparedStatement;
        };
        jdbcContext.jdbcContextForUpdate(statementStrategy);
    }

}

//변하는것과 변하지 않는것을 감지하고 변화는 것을 Extract Method를 했는데 막상보니 얘는 또 다른걸로 바뀔수가있어
// 그래서 한단계 높은 class 레벨로 추상화 => * 인터페이스 *