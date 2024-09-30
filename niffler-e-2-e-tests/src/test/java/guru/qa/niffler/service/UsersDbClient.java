package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.XaFunction;


public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int ISOLATION_LVL = Connection.TRANSACTION_SERIALIZABLE;
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson createUser(UserJson userJson) {
        return Databases.xaTransaction(
                new XaFunction<>(connection -> {
                    List<AuthorityEntity> authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).collect(Collectors.toList());

                    AuthUserEntity authUserEntity = new AuthUserEntity();
                    authUserEntity.setUsername(userJson.username());
                    authUserEntity.setPassword(pe.encode("12345"));
                    authUserEntity.setEnabled(true);
                    authUserEntity.setAccountNonExpired(true);
                    authUserEntity.setCredentialsNonExpired(true);
                    authUserEntity.setAccountNonLocked(true);
                    authUserEntity.setAuthorities(authorityEntities);

                    new AuthUserDaoJdbc(connection).createUser(authUserEntity);
                    return null;
                },
                        CFG.authJdbcUrl()
                ),
                new XaFunction<>(connection -> {
                    UserEntity userEntity =
                            new UserdataUserDaoJdbc(connection).createUser(UserEntity.fromJson(userJson));
                    return UserJson.fromEntity(userEntity);
                },
                        CFG.userdataJdbcUrl()
                ));
    }

    public void deleteUser(UserJson userJson) {
         Databases.xaTransaction(
                new XaFunction<>(connection -> {


                    AuthUserEntity authUserEntity = new AuthUserDaoJdbc(connection)
                            .findByUsername(userJson.username()).orElseThrow();

                    Optional<AuthorityEntity> authority = new AuthAuthorityDaoJdbc(connection)
                            .findByUserId(authUserEntity.getId());

                    if (authority.isPresent()) {
                        new AuthAuthorityDaoJdbc(connection).delete(authority.get());
                    }

                    new AuthUserDaoJdbc(connection).delete(authUserEntity);
                    return null;
                },
                        CFG.authJdbcUrl()
                ),

                 new XaFunction<>(connection -> {
                     UserEntity userEntity = UserEntity.fromJson(userJson);
                      new UserdataUserDaoJdbc(connection).delete(userEntity);
                      return null;
                 },
                         CFG.userdataJdbcUrl()
                 ));
    }
}