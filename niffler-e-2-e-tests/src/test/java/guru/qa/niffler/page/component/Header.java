package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class Header {
    private final SelenideElement profileImageButton = $("[aria-label=\"Menu\"]");
    private final SelenideElement newSpendingButton = $("a[href=\"/spending\"]");
    private final SelenideElement nifflerButton = $x("//h1[text() = 'Niffler']");
    private final ElementsCollection profilePopUpMenuButtons = $$("[role=\"menu\"] .MuiButtonBase-root");
    private String friends = "Friends";
    private String allPeople = "All People";
    private String profile = "Profile";
    private String signOut = "Sign out";
    public void clickProfileMenuButton(String buttonName){
        profileImageButton.click();
        profilePopUpMenuButtons.findBy(Condition.text(buttonName)).click();
    }

    @Step("Переход на страницу friends")
    public FriendsPage toFriendsPage(){
        clickProfileMenuButton(friends);
        return new FriendsPage();
    }

    @Step("Переход на страницу allPeople")
    public AllPeoplePage toAllPeoplePage(){
        clickProfileMenuButton(allPeople);
        return new AllPeoplePage();
    }

    @Step("Переход на страницу profile")
    public ProfilePage toProfilePage(){
        clickProfileMenuButton(profile);
        return new ProfilePage();
    }

    @Step("logout")
    public LoginPage signOut(){
        clickProfileMenuButton(signOut);
        new ModalWindow().clickMoButton("Log out");
        return new LoginPage();
    }

    @Step("Переход на страницу создания трат")
    public EditSpendingPage addSpendingPage(){
        newSpendingButton.click();
        return new EditSpendingPage();
    }

    @Step("Переход на главную страницу")
    public MainPage toMainPage(){
        nifflerButton.click();
        return new MainPage();
    }
}