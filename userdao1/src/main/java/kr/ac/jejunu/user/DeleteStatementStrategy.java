package kr.ac.jejunu.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteStatementStrategy implements StatementStrategy {
    private Long id;
    public DeleteStatementStrategy(Long id) {
        this.id = id;
    }

    //delete부분에서 변하는 부분만 따로 추상화를 진행하였음.
    @Override
    public PreparedStatement makeStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("delete from userinfo where id = ?");
        preparedStatement.setLong(1, id);
        return preparedStatement;
    }
}
