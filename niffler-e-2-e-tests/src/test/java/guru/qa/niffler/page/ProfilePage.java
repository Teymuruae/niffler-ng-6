package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage>{

private final SelenideElement showArchiveSwitcher = $(".PrivateSwitchBase-input");
    private final ElementsCollection categoryNames = $$(".MuiChip-label");
    private final SelenideElement nameInputField = $("#name");
    private final SelenideElement saveButton = $("button[type='submit']");
    private final SelenideElement successSaveChangesMessage = $(".MuiAlert-message");

    @Step("Проверка архивной категории")
    public boolean isCategoryActive(String category) {
        boolean result = false;
        SelenideElement firstParentElement = $$(".MuiChip-label").findBy(Condition.text(category)).parent();
        SelenideElement elementToCheckArchive = firstParentElement.parent().$("[aria-label='Unarchive category']");
        SelenideElement elementToCheckActive = firstParentElement.parent().$(".MuiBox-root");
        String firstParentElementClass = firstParentElement.getAttribute("class");
        if (firstParentElementClass.contains("MuiChip-filledPrimary") && elementToCheckActive.exists()) {
            result = true;
        } else if (firstParentElementClass.contains("MuiChip-filledDefault") && elementToCheckArchive.exists()) {
            result = false;
        }
        return result;
    }

    @Step("Переключение архивных категорий")
    public ProfilePage switchArchiveSwitcher(boolean showArchive) {
        if (showArchive) {
            if (!showArchiveSwitcher.is(Condition.checked)) {
                showArchiveSwitcher.click();
            }
        } else if(!showArchive){
            if (showArchiveSwitcher.is(Condition.checked)) {
                showArchiveSwitcher.click();
            }
        }
        return this;
    }

    @Step("Ввод имени: {name}")
    public ProfilePage setName(String name) {
        nameInputField.setValue(name);
        return this;
    }

    @Step("Клик кнопки сохранения")
    public ProfilePage clickSaveButton() {
        saveButton.click();
        return this;
    }

    @Step("Проверка сообщения успешного сохранения")
    public void assertSuccessMessage() {
        successSaveChangesMessage.shouldHave(Condition.text("Profile successfully updated"));
    }
}
