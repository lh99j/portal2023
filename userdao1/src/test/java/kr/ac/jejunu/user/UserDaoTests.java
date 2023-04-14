package kr.ac.jejunu.user;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.Is.is;

public class UserDaoTests {
    //static을 쓰는 이유는 이 인스턴스를 new로 생성하는게 아니라 재사용하기 위해서 static키워드 사용
    private static UserDao userDao;
    @BeforeAll //테스트 케이스를 수행하기전에 실행되는 코드
    public static void setup(){
        //Spring
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
        userDao = applicationContext.getBean("userDao", UserDao.class); //Dependency LookUp(new 키워드 X)
    }

    @Test
    public void get() throws SQLException, ClassNotFoundException {
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
        
        userDao.insert(user);
        assertThat(user.getId(), greaterThan(1L));

        User insertedUser = userDao.findById(user.getId());
        assertThat(insertedUser.getId(), is(user.getId()));
        assertThat(insertedUser.getName(), is(name));
        assertThat(insertedUser.getPassword(), is(password));
    }

    @Test
    public void update() throws SQLException, ClassNotFoundException {
        User user = new User();
        String name = "lh99j";
        String password = "1111";

        user.setName(name);
        user.setPassword(password);
        userDao.insert(user);

        String updatedName = "updatedLhj";
        String updatedPassword = "2222";

        user.setName(updatedName);
        user.setPassword(updatedPassword);
        userDao.update(user);

        User updatedUser = userDao.findById(user.getId());

        assertThat(updatedUser.getName(), is(updatedName));
        assertThat(updatedUser.getPassword(), is(updatedPassword));

    }

    @Test
    public void delete() throws SQLException, ClassNotFoundException {
        User user = new User();
        String name = "lh99j";
        String password = "1111";

        user.setName(name);
        user.setPassword(password);
        userDao.insert(user);
        userDao.update(user);

        userDao.delete(user.getId());

        User deletedUser = userDao.findById(user.getId());

        assertThat(deletedUser, IsNull.nullValue());

    }
//    @Test
//    public void getForHalla() throws SQLException, ClassNotFoundException {
//        ConnectionMaker connectionMaker = new HallaConnectionMaker();
//        UserDao userDao = new UserDao(connectionMaker);
//        Long id = 1L;
//        String name = "lhj";
//        String password = "1111";
//
//        User user = userDao.findById(id);
//        assertThat(user.getId(), is(id));
//        assertThat(user.getName(), is(name));
//        assertThat(user.getPassword(), is(password));
//    }
//
//    @Test
//    public void insertForHalla() throws SQLException, ClassNotFoundException {
//        User user = new User();
//        String name = "lh99j";
//        String password = "1111";
//
//        user.setName(name);
//        user.setPassword(password);
//        ConnectionMaker connectionMaker = new HallaConnectionMaker();
//        UserDao userDao = new UserDao(connectionMaker);
//        userDao.insert(user);
//        assertThat(user.getId(), greaterThan(1L));
//
//        User insertedUser = userDao.findById(user.getId());
//        assertThat(insertedUser.getId(), is(user.getId()));
//        assertThat(insertedUser.getName(), is(name));
//        assertThat(insertedUser.getPassword(), is(password));
//    }
}
