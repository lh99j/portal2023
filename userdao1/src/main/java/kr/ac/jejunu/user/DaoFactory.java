package kr.ac.jejunu.user;


// 얘만 connectionMaker하고 의존성을 가지고 있음 -> 테스트가 얘한테 의존성을 던짐.
// UserDao가 있었다면 의존성이 계속 생겼겠지만(수정할일이 생기면 일일이 다 수정해야함). DaoFactory에게 맡겼더니 이 클래스만 바꿔주면 호출하는 얘들은 깔끔하게 정리됨.
// DaoFactory가 스프링 -> 이것이 스프링의 코어 (의존성 주입?)
// 스프링 하기 전
//public class DaoFactory {
//    public UserDao getUserDao() {
//        UserDao userDao = new UserDao(connectionMaker());
//        return userDao;
//    }
//
//    private ConnectionMaker connectionMaker() {
//        return new JeJuConnectionMaker();
//    }
//}

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.Driver;

//스프링 적용
@Configuration
public class DaoFactory {
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.classname}")
    String className;
    @Value("${db.url}")
    String url;

    // 스프링이 new 해주는 녀석을 * Bean * 이라고 함
    @Bean
    public UserDao userDao() throws ClassNotFoundException {
        UserDao userDao = new UserDao(jdbcContext());
        return userDao;
    }

    @Bean
    public JdbcContext jdbcContext() throws ClassNotFoundException {
        JdbcContext jdbcContext = new JdbcContext(dataSource());
        return jdbcContext;
    }


    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass((Class<? extends Driver>) Class.forName(className));
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
