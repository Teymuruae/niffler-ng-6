package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, full_name, photo, photo_small) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)
        ) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setString(5, user.getFullname());
            userPs.setBytes(6, user.getPhoto());
            userPs.setBytes(7, user.getPhotoSmall());

            userPs.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
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
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM public.user where id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            UserEntity entity = new UserEntity();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUsername(rs.getString("username"));
                    entity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    entity.setFirstname(rs.getString("firstname"));
                    entity.setSurname(rs.getString("surname"));
                    entity.setFullname(rs.getString("full_name"));
                    entity.setPhoto(rs.getBytes("photo"));
                    entity.setPhotoSmall(rs.getBytes("photo_small"));
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
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement frPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) " +
                             "VALUES ( ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)
        ) {
            frPs.setObject(1, requester.getId());
            frPs.setObject(2, addressee.getId());
            frPs.setString(3, FriendshipStatus.ACCEPTED.name());
            frPs.setDate(4, new java.sql.Date(new Date().getTime()));
            frPs.addBatch();
            frPs.clearParameters();

            frPs.setObject(1, addressee.getId());
            frPs.setObject(2, requester.getId());
            frPs.setString(3, FriendshipStatus.ACCEPTED.name());
            frPs.setDate(4, new java.sql.Date(new Date().getTime()));
            frPs.addBatch();
            frPs.clearParameters();

            frPs.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement frPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) " +
                        "VALUES ( ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)
        ) {
            frPs.setObject(1, requester.getId());
            frPs.setObject(2, addressee.getId());
            frPs.setString(3, FriendshipStatus.PENDING.name());
            frPs.setDate(4, new java.sql.Date(new Date().getTime()));

            frPs.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}