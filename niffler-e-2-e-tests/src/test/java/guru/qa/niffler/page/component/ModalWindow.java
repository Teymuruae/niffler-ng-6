package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.page.FriendsPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class ModalWindow {
    private ElementsCollection modalWindowButtons = $$(".MuiDialogActions-root button.MuiButtonBase-root");

    @Step("Клик кнопки модального окна")
    public ModalWindow clickMoButton(String buttonName){
        modalWindowButtons.findBy(Condition.text(buttonName))
                .shouldBe(visible)
                .click();
        return this;
    }
}
