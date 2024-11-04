package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.UserDataApiClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserClient;
import io.qameta.allure.Step;
import org.apache.commons.lang3.time.StopWatch;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UserApiClient implements UserClient {
    private final static String DEFAULT_PASSWORD = "12345";
    private final static long WAIT_TIMEOUT_SECONDS = 5;

    private final UserDataApiClient userDataApiClient = new UserDataApiClient();
    private final AuthApiClient authApiClient = new AuthApiClient();

    @Step("Create user with username: {username}")
    @Override
    public @Nonnull UserJson createUser(@Nonnull String username, @Nonnull String password) {
        authApiClient.requestRegisterForm();
        authApiClient.registerUser(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        );

        StopWatch sw = StopWatch.createStarted();
        while (sw.getTime(TimeUnit.SECONDS) < WAIT_TIMEOUT_SECONDS) {
            try {
                final UserJson user = userDataApiClient.getCurrentUser(username);
                if (user != null) {
                    return user;
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        throw new AssertionError("User creation timed out after " + WAIT_TIMEOUT_SECONDS + " seconds.");
    }

    @Step("Create {count} income invitation(s) for {targetUser.username}")
    @Override
    public @Nonnull List<String> createIncomeInvitations(@Nonnull UserJson targetUser, int count) {
        List<String> result = new ArrayList<>();
        if (count > 0) {
            // check if user exists
            UserJson existingUser = userDataApiClient.getCurrentUser(targetUser.username());
            if (existingUser == null || existingUser.id() == null) {
                throw new RuntimeException("User not found: " + targetUser.username());
            }
            for (int i = 0; i < count; i++) {
                final UserJson newUser = createUser(randomUsername(), DEFAULT_PASSWORD);
                userDataApiClient.sendInvitation(newUser.username(), targetUser.username());
                result.add(newUser.username());
            }
        }
        return result;
    }

    @Step("Create {count} outcome invitation(s) for {targetUser.username}")
    @Override
    public @Nonnull List<String> createOutcomeInvitations(@Nonnull UserJson targetUser, int count) {
        List<String> result = new ArrayList<>();
        if (count > 0) {
            // check if user exists
            UserJson existingUser = userDataApiClient.getCurrentUser(targetUser.username());
            if (existingUser == null || existingUser.id() == null) {
                throw new RuntimeException("User not found: " + targetUser.username());
            }
            for (int i = 0; i < count; i++) {
                final UserJson newUser = createUser(randomUsername(), DEFAULT_PASSWORD);
                userDataApiClient.sendInvitation(targetUser.username(), newUser.username());
                result.add(newUser.username());
            }
        }
        return result;
    }

    @Step("Create {count} friend(s) for {targetUser.username}")
    @Override
    public @Nonnull List<String> createFriends(@Nonnull UserJson targetUser, int count) {
        List<String> result = new ArrayList<>();
        if (count > 0) {
            // check if user exists
            UserJson existingUser = userDataApiClient.getCurrentUser(targetUser.username());
            if (existingUser == null || existingUser.id() == null) {
                throw new RuntimeException("User not found: " + targetUser.username());
            }
            for (int i = 0; i < count; i++) {
                final UserJson newUser = createUser(randomUsername(), DEFAULT_PASSWORD);
                userDataApiClient.sendInvitation(newUser.username(), targetUser.username());
                userDataApiClient.acceptInvitation(targetUser.username(), newUser.username());
                result.add(newUser.username());
            }
        }
        return result;
    }
}
