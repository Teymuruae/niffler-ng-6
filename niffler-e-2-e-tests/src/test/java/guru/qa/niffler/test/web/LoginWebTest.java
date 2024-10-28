package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.Test;

import java.util.Random;

@WebTest
public class LoginWebTest {

    private static final Config CFG = Config.getInstance();
    private RegisterPage registerPage = new RegisterPage();
    private Faker faker = new Faker();
    private String password = faker.internet().password(3, 12);

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        String userName = faker.name().firstName() + new Random().nextInt(100);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickSignUpButton()
                .doRegister(userName, password, password)
                .clickSignInButton()
                .login(userName, password)
                .mainPageShouldBeDisplayed();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        String userName = faker.name().firstName() + new Random().nextInt(100);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(userName, password);
        new LoginPage().checkLoginErrorMessage();
    }

    @User(
            categories = {
                    @Category(name = "cat_1", archived = true),
                    @Category(name = "cat_2", archived = false)
            },
            spendings = {
                    @Spending(category = "cat_3", description = "some spend desc", amount = 100)
            }
    )
    @Test
    void successLoginTest(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .mainPageShouldBeDisplayed();
    }
}