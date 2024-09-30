package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
//        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
//        Arrays.stream(context.getRequiredTestMethod().getParameters())
//                .filter(p -> AnnotationSupport.isAnnotated(p, User.class))
//                .findFirst()
                .ifPresent(anno -> {
                    if (anno.categories().length > 0) {
                        CategoryJson category =
                                new CategoryJson(
                                        null,
                                        RandomDataUtils.randomCategoryName(),
                                        anno.username(),
                                        false
                                );
                        CategoryJson createdCategory = spendDbClient.createCategory(category);
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                createdCategory
                        );
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        if (categoryJson != null) {
            spendDbClient.deleteCategory(categoryJson);

        }
    }
}