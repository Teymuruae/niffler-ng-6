package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class UserApiClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UserApi userApi = retrofit.create(UserApi.class);

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

    public List<UserJson> getAllSpends(String userName,
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

    public List<UserJson> getAllFriends(String userName,
                                        String searchQuery) {
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