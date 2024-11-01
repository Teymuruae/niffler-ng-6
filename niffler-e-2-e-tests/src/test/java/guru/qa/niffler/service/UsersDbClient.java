package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;


public class UsersDbClient implements UserClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);

                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userdataUserRepository.create(userEntity(username))
                    );
                }
        );
    }

    @Override
    public List<String> createIncomeInvitations(UserJson targetUser, int count) {
        List<String> userNames = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                String username = randomUsername();
                xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, "12345");
                    authUserRepository.create(authUser);
                    UserEntity requester = userdataUserRepository.create(userEntity(username));
                    userdataUserRepository.sendInvitation(requester, targetEntity);
                    return null;
                });
                userNames.add(username);
            }
        }
        return userNames;
    }

    @Override
    public List<String> createOutcomeInvitations(UserJson requester, int count) {
        List<String> userNames = new ArrayList<>();
        if (count > 0) {
            String username = randomUsername();
            UserEntity requesterEntity = userdataUserRepository.findById(requester.id()).orElseThrow();
            xaTransactionTemplate.execute(() -> {
                AuthUserEntity authUser = authUserEntity(username, "12345");
                authUserRepository.create(authUser);
                UserEntity addressee = userdataUserRepository.create(userEntity(username));
                userdataUserRepository.sendInvitation(requesterEntity, addressee);
                return null;
            });
            userNames.add(username);
        }
        return userNames;
    }

    @Override
    public List<String> createFriends(UserJson targetUser, int count) {
        List<String> userNames = new ArrayList<>();
        if (count > 0) {
            String username = randomUsername();
            UserEntity targetEntity = userdataUserRepository.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, "12345");
                    authUserRepository.create(authUser);
                    UserEntity friendEntity = userdataUserRepository.create(userEntity(username));
                    userdataUserRepository.addFriend(targetEntity, friendEntity);
                    return null;
                });
            }
            userNames.add(username);
        }
        return userNames;
    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }
}