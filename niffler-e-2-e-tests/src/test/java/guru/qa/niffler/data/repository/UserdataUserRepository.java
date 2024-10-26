package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {
    UserEntity create(UserEntity user);

    UserEntity update(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    void addFriend(UserEntity requester, UserEntity addressee);

    Optional<UserEntity> findByUsername(String username);

    void sendInvitation(UserEntity requester, UserEntity addressee);

    void remove(UserEntity user);
}