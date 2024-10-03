package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.user where id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            AuthUserEntity entity = new AuthUserEntity();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUsername(rs.getString("username"));
                    entity.setPassword(rs.getString("password"));
                    entity.setEnabled(rs.getBoolean("enabled"));
                    entity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    entity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    entity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));

                    return Optional.of(
                            entity
                    );
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.user WHERE username = ?"
        )) {
            ps.setString(1, username);
            ps.execute();

            AuthUserEntity entity = new AuthUserEntity();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUsername(rs.getString("username"));
                    entity.setPassword(rs.getString("password"));
                    entity.setEnabled(rs.getBoolean("enabled"));
                    entity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    entity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    entity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    return Optional.of(
                            entity
                    );
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthUserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM public.user where id = ?"
        )) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}