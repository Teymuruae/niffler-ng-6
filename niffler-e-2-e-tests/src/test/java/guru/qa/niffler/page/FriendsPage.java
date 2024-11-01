package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.ModalWindow;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {
    private SelenideElement tableName = $("#simple-tabpanel-friends h2.MuiTypography-root.MuiTypography-h5");
    private SelenideElement friendsListTable = $("#friends");
    private SelenideElement requestsListTable = $("#requests");
    private ElementsCollection friendsListNames = friendsListTable.$$x(".//p[contains(@class, 'MuiTypography-body1') and not(@id)]");
    private ElementsCollection requestsListNames = requestsListTable.$$x(".//p[contains(@class, 'MuiTypography-body1') and not(@id)]");
    private SelenideElement noUsersYetText = $("p.MuiTypography-root.MuiTypography-h6");
    private ElementsCollection acceptDeclineButtons = $$("#requests button.MuiButtonBase-root");
    private ElementsCollection modalWindowButtons = $$(".MuiDialogActions-root button.MuiButtonBase-root");

    @Step("Проверка наименования таблицы")
    public FriendsPage assertFriendsTableName(String tableNameText) {
        tableName.shouldBe(visible).shouldHave(text(tableNameText));
        return this;
    }

    @Step("Проверка отображения принятого друга")
    public FriendsPage assertFriendInList(String friendName) {
        friendsListNames.findBy(Condition.text(friendName)).shouldBe(visible);
        return this;
    }

    @Step("Проверка отображения входящей заявки")
    public FriendsPage assertIncomeRequestInList(String requestFriendName) {
        requestsListNames.findBy(Condition.text(requestFriendName)).shouldBe(visible);
        return this;
    }

    @Step("Проверка пустого списка друзей")
    public FriendsPage assertFriendsListIsEmpty() {
        friendsListTable.shouldNotBe(exist);
        noUsersYetText.shouldBe(visible).shouldHave(text("There are no users yet"));
        return this;
    }

    @Step("Одобрение заявки")
    public FriendsPage accept(){
        acceptDeclineButtons.findBy(Condition.text("Accept"))
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Отклонение заявки")
    public FriendsPage decline(){
        acceptDeclineButtons.findBy(Condition.text("Decline"))
                .shouldBe(visible)
                .click();
      new ModalWindow().clickMoButton("Decline");
        return this;
    }
}