package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class JdbcTest {

    @Test
    void createUserTest(){
        UserJson userJson = new UserJson(
                null,
                "anabolik",
                "Ivan",
                "Ivanov",
                "Ivanov Ivan",
                CurrencyValues.EUR,
                null,
                null
        );

        UsersDbClient usersDbClient = new UsersDbClient();
        usersDbClient.createUser(userJson);
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
