package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
    AuthorityEntity create(AuthorityEntity entity);

    Optional<AuthorityEntity> findById(UUID id);

    Optional<AuthorityEntity> findByUserId(UUID id);

    void delete(AuthorityEntity user);
}
