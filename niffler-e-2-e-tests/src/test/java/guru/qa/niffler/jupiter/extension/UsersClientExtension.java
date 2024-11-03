package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class UsersClientExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(UserClient.class)) {
                field.setAccessible(true);
                field.set(testInstance, new UsersDbClient());
            }
        }
    }
}