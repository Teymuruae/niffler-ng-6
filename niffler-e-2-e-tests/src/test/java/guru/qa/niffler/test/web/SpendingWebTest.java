package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.Bubble;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import jaxb.userdata.Currency;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@WebTest
public class SpendingWebTest {

    private final StatComponent statComponent = new StatComponent();
    private final Config CFG = Config.getInstance();
    private final SpendingTable spendingTable = new SpendingTable();
    private Header header = new Header();

    @User(
            username = "duck",
            categories = @Category(
                    archived = true
            ),
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void addSpendTest(UserJson user) {
        String category = RandomDataUtils.randomCategoryName();
        String description = RandomDataUtils.randomSentence(2);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());
        header
                .addSpendingPage()
                .setSpendingCategory(category)
                .setNewSpendingDescription(description)
                .setSpendingAmount("55")
                .calendar
                .selectDateInCalendar(new Date());
        new EditSpendingPage().save();
        new MainPage().checkThatTableContainsSpending(description);
    }

    @User
    @Test
    void userCanCreateSpending(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader()
                .addSpendingPage()
                .createSpend("Museum", "Art", "300")
                .checkAlert("New spending is successfully created");
        new SpendingTable()
                .checkTableContains("Museum");
    }

    @User(
            spendings =
                    {
                            @Spending(
                                    category = "Обучение",
                                    description = "Обучение Advanced 2.0",
                                    amount = 79990
                            ),
                            @Spending(
                                    category = "Транспорт",
                                    description = "Такси",
                                    amount = 1200
                            ),
                            @Spending(
                                    category = "Еда",
                                    description = "Чипсы",
                                    amount = 79
                            )
                    }
    )
    @ScreenShotTest("img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());
//        Selenide.sleep(3000);
//        BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());
//        assertFalse(new ScreenDiffResult(
//                expected,
//                actual
//        ), "Screen comparison failure");

        statComponent.checkBubbles(Color.green, Color.yellow);
//        Bubble transport = new Bubble(Color.green, "Транспорт 1200 ₽");
//        Bubble education = new Bubble(Color.yellow, "Обучение 79990 ₽");
//        Bubble education2 = new Bubble(Color.yellow, "Обучение 79654 ₽");
//        statComponent.checkBubbles(
//               transport,education
//        );
//        statComponent.checkBubblesInAnyOrder(education,transport, education2);
//        statComponent.checkBubblesContains(transport, education);
    }


    @SneakyThrows
    @ScreenShotTest("img/expected-stat.png")
    void screenShotTest(BufferedImage expected){
        Selenide.open("https://timesheet-dev.aplanadc.ru/login");

        BufferedImage actual = ImageIO.read($("#logo").screenshot());
        assertFalse(new ScreenDiffResult(
                expected,
                actual
        ));

    }
    @User(
            spendings =
                    {
                            @Spending(
                                    category = "Обучение",
                                    description = "Обучение Advanced 2.0",
                                    amount = 79990
//                            ),
//                            @Spending(
//                                    category = "Транспорт",
//                                    description = "Такси",
//                                    amount = 1200
//                            ),
//                            @Spending(
//                                    category = "Еда",
//                                    description = "Чипсы",
//                                    amount = 79
                            )
                    }
    )
    @Test
    void checkSpendTableTest(UserJson user) {
        CategoryJson categoryJson = new CategoryJson( null, "Обучение" , "", true);
        SpendJson spend = new SpendJson(null, new Date(), categoryJson, CurrencyValues.RUB, 79990D,
                "Обучение Advanced1 2.0", "");
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());
    spendingTable.checkSpendTableRows(spend);
//    spendingTable.checkSpendTableRows(user.testData().spendings().toArray(SpendJson[]::new));
    }
}