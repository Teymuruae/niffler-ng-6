package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserDaoJdbc implements UserdataUserDao {

    private final Connection connection;

    public UserdataUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO public.user (username, currency, firstname, surname, full_name, photo, photo_small) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setString(5, user.getFullname());
            ps.setBytes(6, user.getPhoto());
            ps.setBytes(7, user.getPhotoSmall());

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
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
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
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.user WHERE username = ?"
        )) {
            ps.setString(1, username);
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
    public List<UserEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM public.user"
        )) {
            ps.execute();

            List<UserEntity> entities = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {

                while (rs.next()) {
                    UserEntity entity = new UserEntity();
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUsername(rs.getString("username"));
                    entity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    entity.setFirstname(rs.getString("firstname"));
                    entity.setSurname(rs.getString("surname"));
                    entity.setFullname(rs.getString("full_name"));
                    entity.setPhoto(rs.getBytes("photo"));
                    entity.setPhotoSmall(rs.getBytes("photo_small"));

                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntity user) {
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