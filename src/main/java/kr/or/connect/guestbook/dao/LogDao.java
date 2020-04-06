package kr.or.connect.guestbook.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kr.or.connect.guestbook.dto.Log;

@Repository //dao에 붙이는 애노테이션
public class LogDao {
    private NamedParameterJdbcTemplate jdbc;
    private SimpleJdbcInsert insertAction;

    public LogDao(DataSource dataSource) {
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("log")
                .usingGeneratedKeyColumns("id"); //primary key인 id 자동 입력!
    }

    public Long insert(Log log) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(log);
        return insertAction.executeAndReturnKey(params).longValue(); // insert문을 내부적으로 생성해서 실행후 생성된 id값 반환
    }

}