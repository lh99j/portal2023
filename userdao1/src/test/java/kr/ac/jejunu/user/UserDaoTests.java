package kr.ac.jejunu.user;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

public class UserDaoTests {

    @Test
    public void get() throws SQLException, ClassNotFoundException {
        // UserDao코드에서 getConnection을 사용하는 녀석 -> 즉 얘에게 의존성을 던짐
        ConnectionMaker connectionMaker = new JeJuConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        Long id = 1L;
        String name = "lhj";
        String password = "1234";

        User user = userDao.findById(id);
        assertThat(user.getId(), is(id));
        assertThat(user.getName(), is(name));
        assertThat(user.getPassword(), is(password));
    }

    @Test
    public void insert() throws SQLException, ClassNotFoundException {
        User user = new User();
        String name = "lh99j";
        String password = "1111";

        user.setName(name);
        user.setPassword(password);
        ConnectionMaker connectionMaker = new JeJuConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        userDao.insert(user);
        assertThat(user.getId(), greaterThan(1L));

        User insertedUser = userDao.findById(user.getId());
        assertThat(insertedUser.getId(), is(user.getId()));
        assertThat(insertedUser.getName(), is(name));
        assertThat(insertedUser.getPassword(), is(password));
    }

    @Test
    public void getForHalla() throws SQLException, ClassNotFoundException {
        ConnectionMaker connectionMaker = new HallaConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        Long id = 1L;
        String name = "lhj";
        String password = "1111";

        User user = userDao.findById(id);
        assertThat(user.getId(), is(id));
        assertThat(user.getName(), is(name));
        assertThat(user.getPassword(), is(password));
    }

    @Test
    public void insertForHalla() throws SQLException, ClassNotFoundException {
        User user = new User();
        String name = "lh99j";
        String password = "1111";

        user.setName(name);
        user.setPassword(password);
        ConnectionMaker connectionMaker = new HallaConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        userDao.insert(user);
        assertThat(user.getId(), greaterThan(1L));

        User insertedUser = userDao.findById(user.getId());
        assertThat(insertedUser.getId(), is(user.getId()));
        assertThat(insertedUser.getName(), is(name));
        assertThat(insertedUser.getPassword(), is(password));
    }
}
