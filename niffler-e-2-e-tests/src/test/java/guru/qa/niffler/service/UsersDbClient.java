package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int ISOLATION_LVL = Connection.TRANSACTION_SERIALIZABLE;
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authorityEntities);

        return UserJson.fromEntity(
                new UdUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                        .create(
                                UserEntity.fromJson(user)
                        )
        );
    }


    public UserJson createUser(UserJson user) {
        return UserJson.fromEntity(
                xaTransaction(
                        new XaFunction<>(
                                con -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(user.username());
                                    authUser.setPassword(pe.encode("12345"));
                                    authUser.setEnabled(true);
                                    authUser.setAccountNonExpired(true);
                                    authUser.setAccountNonLocked(true);
                                    authUser.setCredentialsNonExpired(true);
                                    new AuthUserDaoJdbc(con).create(authUser);
                                    new AuthAuthorityDaoJdbc(con).create(
                                            Arrays.stream(Authority.values())
                                                    .map(a -> {
                                                                AuthorityEntity ae = new AuthorityEntity();
                                                                ae.setUserId(authUser.getId());
                                                                ae.setAuthority(a);
                                                                return ae;
                                                            }
                                                    ).toArray(AuthorityEntity[]::new));
                                    return null;
                                },
                                CFG.authJdbcUrl()
                        ),
                        new XaFunction<>(
                                con -> {
                                    UserEntity ue = new UserEntity();
                                    ue.setUsername(user.username());
                                    ue.setFullname(user.fullname());
                                    ue.setCurrency(user.currency());
                                    new UserdataUserDaoJdbc(con).create(ue);
                                    return ue;
                                },
                                CFG.userdataJdbcUrl()
                        )
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