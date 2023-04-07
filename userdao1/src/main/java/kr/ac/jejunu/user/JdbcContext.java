package kr.ac.jejunu.user;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcContext {
    private final DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    User jdbcContextForFind(StatementStrategy statementStrategy) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            // 데이터 어딨어? mysql
            // mysql 클래스 로딩
            connection = dataSource.getConnection();
            preparedStatement = statementStrategy.makeStatement(connection);

            // 쿼리 만들고
//            preparedStatement = connection.prepareStatement
//                    ("select id, name, password from userinfo where id = ?");
//            preparedStatement.setLong(1, id);

            // 쿼리 실행하고
            resultSet = preparedStatement.executeQuery();

            //delete에서 지워진 id를 접근하려 하기때문에 에러가 났음
            // 그걸 해결하기 위해 if문
            if (resultSet.next()) {
                // 결과를 사용자 정보에 매핑하고
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
            }


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
        return user;
    }

    void jdbcContextForInsert(User user, StatementStrategy statementStrategy) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            // 쿼리 만들고
            preparedStatement = statementStrategy.makeStatement(connection);
//            preparedStatement = connection.prepareStatement
//                    ("insert into userinfo (name, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
//            preparedStatement.setString(1, user.getName());
//            preparedStatement.setString(2, user.getPassword());

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
    }

    void jdbcContextForUpdate(StatementStrategy statementStrategy) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dataSource.getConnection();

            // 쿼리 만들고
            //근데 얘만 변하고 나머지는 안변해. 그래서 Extract레벨로 추상화 하려했는데 또 얘가 다른용도의 얘랑 쓰려고하니 바뀔수가있네?
            // 그래서 한단계 높은 클래스 추상화로 바꿔야겠네! => * class level * -> * Interface *
            preparedStatement = statementStrategy.makeStatement(connection);
//            preparedStatement = connection.prepareStatement("delete from userinfo where id = ?");
            //            preparedStatement.setLong(1, id);

            // 쿼리 실행하고
            preparedStatement.executeUpdate();

        } finally {
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
    }
}