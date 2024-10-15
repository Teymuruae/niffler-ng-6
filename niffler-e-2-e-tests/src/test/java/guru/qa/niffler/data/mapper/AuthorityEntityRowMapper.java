package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthorityEntityRowMapper instance = new AuthorityEntityRowMapper();
    private AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private AuthorityEntityRowMapper() {
    }

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity entity = new AuthorityEntity();
        entity.setId(rs.getObject("id", UUID.class));
        entity.setUser(authUserDao.findById(rs.getObject("user_id", UUID.class)).get());
        entity.setAuthority(Authority.valueOf(rs.getString("authority")));
        return entity;
    }
}
