package kr.or.connect.guestbook.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

//DB 설정 클래스. TransactionManagementConfigurer인터페이스를 구현한다.
@Configuration
@EnableTransactionManagement //트랜잭션과 관련된 설정을 자동으로 해주는 애노테이션
public class DBConfig implements TransactionManagementConfigurer {
    private String driverClassName = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/connectdb?useUnicode=true&characterEncoding=utf8";
    private String username = "root";
    private String password = "0000";

    //DB연결을 위한 dataSouce 등록
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    // 트랜잭션을 처리할 transactionManger을 반환하는 메서드
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManger();
    }

    @Bean
    public PlatformTransactionManager transactionManger() {
        return new DataSourceTransactionManager(dataSource());
    }
}