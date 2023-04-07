package kr.ac.jejunu.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateStatementStrategy implements StatementStrategy {

    private User user;
    public UpdateStatementStrategy(User user) {
        this.user = user;
    }

    //근데 update를 만들고 보니까 update는 delete와는 다른 파라미터가 존재함
    //그래서 파라미터를 Object 타입으로 바꾸자!
    //근데 일일이 바꾸면 어려우니까 Refactoring

    @Override
    public PreparedStatement makeStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement
                ("update userinfo set name = ?, password = ? where id = ?");
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setLong(3, user.getId());
        return preparedStatement;
    }
}
