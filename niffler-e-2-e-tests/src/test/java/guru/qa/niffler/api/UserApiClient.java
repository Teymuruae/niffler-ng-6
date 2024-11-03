package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

@ParametersAreNonnullByDefault
public class UserApiClient extends RestClient {

    private final UserApi userApi;

    public UserApiClient(){
        super(CFG.userdataUrl());
        this.userApi = retrofit.create(UserApi.class);
    }

    @Step("Получение данных пользователя {userName}")
    @Nonnull
    public UserJson getCurrentUser(String userName) {
        final Response<UserJson> response;

        try {
            response =
                    userApi.getCurrentUser(userName)
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
                    userApi.getAllUsers(userName, searchQuery)
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
                    userApi.updateUser(user)
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
                    userApi.friends(userName, searchQuery)
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
                    userApi.sendInvitation(username, targetUsername)
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
                    userApi.acceptInvitation(username, targetUsername)
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
                    userApi.declineInvitation(username, targetUsername)
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
                    userApi.removeFriend(username, targetUsername)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }
}