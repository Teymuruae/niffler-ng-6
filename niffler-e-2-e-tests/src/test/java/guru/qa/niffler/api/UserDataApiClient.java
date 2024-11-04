package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

@ParametersAreNonnullByDefault
public class UserDataApiClient extends RestClient {

    private final UserDataApi userDataApi;

    public UserDataApiClient() {
        super(CFG.userdataUrl());
        this.userDataApi = retrofit.create(UserDataApi.class);
    }

    @Step("Получение данных пользователя {userName}")
    @Nonnull
    public UserJson getCurrentUser(String userName) {
        final Response<UserJson> response;

        try {
            response =
                    userDataApi.getCurrentUser(userName)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Получение списка пользователей")
    @Nonnull
    public List<UserJson> getAllUsers(String userName,
                                      String searchQuery) {
        final Response<List<UserJson>> response;

        try {
            response =
                    userDataApi.getAllUsers(userName, searchQuery)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Редактирование пользователя {user.username}")
    @Nullable
    public UserJson updateUser(UserJson user) {
        final Response<UserJson> response;

        try {
            response =
                    userDataApi.updateUser(user)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Получение списка друзей")
    @Nonnull
    public List<UserJson> getAllFriends(String userName,
                                        @Nullable String searchQuery) {
        final Response<List<UserJson>> response;

        try {
            response =
                    userDataApi.friends(userName, searchQuery)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Отправка заявки на дружбу")
    @Nullable
    public UserJson sendInvitation(String username, String targetUsername) {
        final Response<UserJson> response;

        try {
            response =
                    userDataApi.sendInvitation(username, targetUsername)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Подтверждение заявки на дружбу")
    @Nullable
    public UserJson acceptInvitation(String username, String targetUsername) {
        final Response<UserJson> response;

        try {
            response =
                    userDataApi.acceptInvitation(username, targetUsername)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Отклонение заявки на дружбу")
    @Nullable
    public UserJson declineInvitation(String username, String targetUsername) {
        final Response<UserJson> response;

        try {
            response =
                    userDataApi.declineInvitation(username, targetUsername)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Удаление друга {targetUsername} у {username}")
    @Nullable
    public UserJson removeFriend(String username, String targetUsername) {
        final Response<UserJson> response;

        try {
            response =
                    userDataApi.removeFriend(username, targetUsername)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }
}