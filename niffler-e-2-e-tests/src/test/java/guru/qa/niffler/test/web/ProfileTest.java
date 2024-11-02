package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {

    private static final Config CFG = Config.getInstance();
    private ProfilePage profilePage = new ProfilePage();
    private Header header = new Header();

    @User(
            username = "duck",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), "12345");
        new Header()
                .clickProfileMenuButton("Profile");
        boolean isActiveCategory = profilePage
                .switchArchiveSwitcher(true)
                .isCategoryActive(category.name());
        Assertions.assertFalse(isActiveCategory);
    }

    @User(
            username = "duck",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), "12345");
        new Header()
                .clickProfileMenuButton("Profile");
        boolean isActiveCategory = profilePage
                .switchArchiveSwitcher(true)
                .isCategoryActive(category.name());
        Assertions.assertTrue(isActiveCategory);
    }

    @User
    @Test
    void editProfileTest(UserJson user) {
        String name = RandomDataUtils.randomName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());
               header
                .toProfilePage()
                .setName(name)
                .clickSaveButton()
                .assertSuccessMessage();
    }
}