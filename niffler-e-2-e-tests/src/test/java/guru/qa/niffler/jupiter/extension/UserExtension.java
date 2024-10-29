package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "12345";
    private final UserClient userClient = new UsersDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if ("".equals(anno.username())) {
                       final String username = RandomDataUtils.randomUsername();
                        UserJson testUser = userClient.createUser(username, defaultPassword);
                        List<String> incomeInvitations = userClient.createIncomeInvitations(testUser, anno.incomeInvitations());
                        List<String> outcomeInvitations = userClient.createOutcomeInvitations(testUser, anno.outcomeInvitations());
                        List<String> friends = userClient.createFriends(testUser, anno.friends());

                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                testUser.addTestData(new TestData(
                                        defaultPassword,
                                        new ArrayList<>(),
                                        new ArrayList<>(),
                                        incomeInvitations,
                                        outcomeInvitations,
                                        friends
                                ))
                        );
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
    }
}
