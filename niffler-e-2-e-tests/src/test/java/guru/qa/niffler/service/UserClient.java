package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UserClient {
    @Nonnull
    UserJson createUser(String username, String password);

    List<String> createIncomeInvitations(UserJson targetUser, int count);

    List<String> createOutcomeInvitations(UserJson targetUser, int count);

    List<String> createFriends(UserJson targetUser, int count);
}
