package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private static UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    @Override
    public UserEntity create(UserEntity user) {
        return userdataUserDao.create(user);
    }

    @Override
    public UserEntity update(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "UPDATE public.user SET currency=?, firstname=?, surname=?, full_name=?, photo=?, photo_small=? " +
                        "WHERE id=?",
                Statement.RETURN_GENERATED_KEYS
        )) {

            ps.setString(1, user.getCurrency().name());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getSurname());
            ps.setString(4, user.getFullname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setObject(7, user.getId());

            ps.executeUpdate();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
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
    public Optional<UserEntity> findByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
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

    @Override
    public void remove(UserEntity user) {
        userdataUserDao.delete(user);
    }
}