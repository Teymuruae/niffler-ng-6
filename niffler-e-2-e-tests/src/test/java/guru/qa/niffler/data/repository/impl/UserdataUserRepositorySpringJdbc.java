package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private static UserdataUserDao userdataUserDao = new UdUserDaoSpringJdbc();

    @Override
    public UserEntity create(UserEntity user) {
        return userdataUserDao.create(user);
    }

    @Override
    public UserEntity update(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        jdbcTemplate.update(
                "UPDATE \"user\" SET  currency=?, firstname=?, surname=?, photo=?, photo_small=?, full_name=? " +
                        "WHERE id=?",
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getPhoto(),
                user.getPhotoSmall(),
                user.getFullname(),
                user.getId()
        );
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        String query = "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) " +
                "VALUES ( ?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        jdbcTemplate.update(
                query,
                requester.getId(),
                addressee.getId(),
                FriendshipStatus.ACCEPTED.name(),
                new java.sql.Date(new Date().getTime())
        );

        jdbcTemplate.update(
                query,
                addressee.getId(),
                requester.getId(),
                FriendshipStatus.ACCEPTED.name(),
                new java.sql.Date(new Date().getTime())
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        jdbcTemplate.update(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) " +
                        "VALUES ( ?, ?, ?, ?)",
                requester.getId(),
                addressee.getId(),
                FriendshipStatus.PENDING.name(),
                new java.sql.Date(new Date().getTime())
        );
    }

    @Override
    public void remove(UserEntity user) {
        userdataUserDao.delete(user);
    }
}