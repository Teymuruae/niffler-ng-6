package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static guru.qa.niffler.data.tpl.Connections.holder;
public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUserId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthorityEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM public.authority where id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            AuthorityEntity entity = new AuthorityEntity();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUserId(rs.getObject("user_id", UUID.class));
                    entity.setAuthority(Authority.valueOf(rs.getString("authority")));

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
    public Optional<AuthorityEntity> findByUserId(UUID userId) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM public.authority where user_id = ?"
        )) {
            ps.setObject(1, userId);
            ps.execute();

            AuthorityEntity entity = new AuthorityEntity();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUserId(rs.getObject("user_id", UUID.class));
                    entity.setAuthority(Authority.valueOf(rs.getString("authority")));

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
    public List<AuthorityEntity> findAll() {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM public.authority"
        )) {
            ps.execute();

            List<AuthorityEntity> entities = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {

                while (rs.next()) {
                    AuthorityEntity entity = new AuthorityEntity();
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUserId(rs.getObject("user_id", UUID.class));
                    entity.setAuthority(Authority.valueOf(rs.getString("authority")));

                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthorityEntity entity) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM public.authority where user_id = ?"
        )) {
            ps.setObject(1, entity.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}