package guru.qa.niffler.test.web;

import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class JdbcTest {

    @Test
    void createUserJdbcTransactionTest(){
        UserJson userJson = new UserJson(
                null,
               randomUsername(),
                "Ivan",
                "Ivanov",
                "Ivanov Ivan",
                CurrencyValues.EUR,
                null,
                null
        );

        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.createUserJdbcTransaction(userJson);

        System.out.println(userJson);
    }

    @Test
    void createUserRepositoryTest(){
        UserJson userJson = new UserJson(
                null,
                randomUsername(),
                "Ivan",
                "Ivanov",
                "Ivanov Ivan",
                CurrencyValues.EUR,
                null,
                null
        );

        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.createUserFromRepository(userJson);

        System.out.println(userJson);
    }

    @Test
    void addFriendTest(){
        UserJson requester = UserJson.fromEntity(
                new UserdataUserDaoJdbc().findByUsername("duck").get()
        );
        UserJson addressee = UserJson.fromEntity(
                new UserdataUserDaoJdbc().findByUsername("frog").get()
        );
        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.addFriend(requester, addressee);

    }

    @Test
    void createInvitationTest(){
        UserJson requester = UserJson.fromEntity(
                new UserdataUserDaoJdbc().findByUsername("duck").get()
        );
        UserJson addressee = UserJson.fromEntity(
                new UserdataUserDaoJdbc().findByUsername("Pok64").get()
        );
        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.createInvitation(requester, addressee);

    }

    @Test
    void createUserJdbcWithoutTransactionTest(){
        UserJson userJson = new UserJson(
                null,
                randomUsername(),
                "Ivan",
                "Ivanov",
                "Ivanov Ivan",
                CurrencyValues.EUR,
                null,
                null
        );

        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.createUserJdbcWithoutTransaction(userJson);

        System.out.println(userJson);
    }

    @Test
    void createUserSpringJdbcTransactionTest(){
        UserJson userJson = new UserJson(
                null,
                randomUsername(),
                "Ivan",
                "Ivanov",
                "Ivanov Ivan",
                CurrencyValues.EUR,
                null,
                null
        );

        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.createUserSpringJdbcTransaction(userJson);

        System.out.println(userJson);
    }

    @Test
    void createUserSpringJdbcWithoutTransactionTest(){
        UserJson userJson = new UserJson(
                null,
                randomUsername(),
                "Ivan",
                "Ivanov",
                "Ivanov Ivan",
                CurrencyValues.EUR,
                null,
                null
        );

        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.createUserSpringJdbcWithoutTransaction(userJson);

        System.out.println(userJson);
    }

    @Test
    void deleteUserTest(){
        UserJson userJson = new UserJson(
                UUID.fromString("96ab5df8-80ff-11ef-b0a4-0242ac110002"),
                "eagle",
                "Ivan",
                "Ivanov",
                "Ivanov Ivan",
                CurrencyValues.EUR,
                null,
                null
        );

        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.deleteUser(userJson);
    }
}
