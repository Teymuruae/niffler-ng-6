package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
@Getter
public class ProfilePage extends BasePage<ProfilePage> {

    private final SelenideElement showArchiveSwitcher = $(".PrivateSwitchBase-input");
    private final ElementsCollection categoryNames = $$(".MuiChip-label");
    private final SelenideElement nameInputField = $("#name");
    private final SelenideElement saveButton = $("button[type='submit']");
    private final SelenideElement successSaveChangesMessage = $(".MuiAlert-message");
    private final SelenideElement uploadAvatarButton = $("#image__input");
//    private final SelenideElement uploadAvatarButton = $(".image__input-label .MuiButtonBase-root");
    private final SelenideElement avatar = $(".MuiGrid-root .MuiAvatar-root");


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
        } else if (!showArchive) {
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


    @Step("Update profle name to {newName}")
    public ProfilePage updateName(String newName) {
        setName(newName);
        saveButton.click();
        return this;
    }


    @Step("Check that name is {expectedName}")
    public ProfilePage checkName(String expectedName) {
        nameInputField.shouldHave(Condition.value(expectedName));
        return this;
    }

    @Step("Add avatar")
    public ProfilePage addAvatar(String path) {
        uploadAvatarButton.uploadFromClasspath(path);
        return this;
    }

    @Step("Assert avatar image")
    public ProfilePage assertAvatarImage(BufferedImage expected){
        BufferedImage actual = avatar.screenshotAsImage();
        assertFalse(new ScreenDiffResult(
                expected,
                actual),
                "Screen comparison failure");
        return this;
    }
}
