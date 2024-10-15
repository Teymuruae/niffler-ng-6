package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Optional;

import static guru.qa.niffler.data.Databases.xaTransaction;


public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();
    private final UserdataUserDao udUserDao = new UserdataUserDaoJdbc();
    private final AuthUserDao authUserSpringDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthoritySpringDao = new AuthAuthorityDaoSpringJdbc();
    private final UserdataUserDao udUserSpringDao = new UdUserDaoSpringJdbc();

    private final TransactionTemplate txTemplateOld = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            DataSources.dataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            DataSources.dataSource(CFG.userdataJdbcUrl())
                    )
            )
    );
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUserJdbcTransaction(UserJson user) {
        return txTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDao.create(authorityEntities);

                    return UserJson.fromEntity(
                            udUserDao.create(UserEntity.fromJson(user))
                    );
                }
        );
    }

    public UserJson createUserJdbcWithoutTransaction(UserJson user) {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDao.create(authorityEntities);

                    return UserJson.fromEntity(
                            udUserDao.create(UserEntity.fromJson(user))
                    );
    }

    public UserJson createUserSpringJdbcTransaction(UserJson user) {
        return txTemplate.execute(status -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserSpringDao.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthoritySpringDao.create(authorityEntities);

                    return UserJson.fromEntity(
                            udUserSpringDao.create(UserEntity.fromJson(user))
                    );
                }
        );
    }

    public UserJson createUserSpringJdbcWithoutTransaction(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserSpringDao.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(createdAuthUser);
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        authAuthoritySpringDao.create(authorityEntities);

        return UserJson.fromEntity(
                udUserSpringDao.create(UserEntity.fromJson(user))
        );
    }

    public UserJson createUserOld(UserJson user) {
        return UserJson.fromEntity(
                xaTransaction(
                        new Databases.XaFunction<>(
                                con -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(user.username());
                                    authUser.setPassword(pe.encode("12345"));
                                    authUser.setEnabled(true);
                                    authUser.setAccountNonExpired(true);
                                    authUser.setAccountNonLocked(true);
                                    authUser.setCredentialsNonExpired(true);
                                    new AuthUserDaoJdbc().create(authUser);
                                    new AuthAuthorityDaoJdbc().create(
                                            Arrays.stream(Authority.values())
                                                    .map(a -> {
                                                                AuthorityEntity ae = new AuthorityEntity();
                                                        ae.setUser(authUser);
                                                                ae.setAuthority(a);
                                                                return ae;
                                                            }
                                                    ).toArray(AuthorityEntity[]::new));
                                    return null;
                                },
                                CFG.authJdbcUrl()
                        ),
                        new Databases.XaFunction<>(
                                con -> {
                                    UserEntity ue = new UserEntity();
                                    ue.setUsername(user.username());
                                    ue.setFullname(user.fullname());
                                    ue.setCurrency(user.currency());
                                    new UserdataUserDaoJdbc().create(ue);
                                    return ue;
                                },
                                CFG.userdataJdbcUrl()
                        )
                ));
    }

    public void deleteUser(UserJson userJson) {
        Databases.xaTransaction(
                new Databases.XaFunction<>(connection -> {


                    AuthUserEntity authUserEntity = new AuthUserDaoJdbc()
                            .findByUsername(userJson.username()).orElseThrow();

                    Optional<AuthorityEntity> authority = new AuthAuthorityDaoJdbc()
                            .findByUserId(authUserEntity.getId());

                    if (authority.isPresent()) {
                        new AuthAuthorityDaoJdbc().delete(authority.get());
                    }

                    new AuthUserDaoJdbc().delete(authUserEntity);
                    return null;
                },
                        CFG.authJdbcUrl()
                ),

                new Databases.XaFunction<>(connection -> {
                    UserEntity userEntity = UserEntity.fromJson(userJson);
                    new UserdataUserDaoJdbc().delete(userEntity);
                    return null;
                },
                        CFG.userdataJdbcUrl()
                ));
    }
}
