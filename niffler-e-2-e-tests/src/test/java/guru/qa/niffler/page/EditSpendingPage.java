package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class EditSpendingPage {
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");
  private final SelenideElement amountInputField = $("#amount");
  private final SelenideElement categoryInputField = $("#category");
  private final SelenideElement calendarArea = $x("//label[text() = 'Date']//parent::div");

  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    return this;
  }

  public Calendar calendar = new Calendar(calendarArea);
  @Step("Сохранение изменений")
  public void save() {
    saveBtn.click();
  }

  @Step("Ввод названия категории: {category}")
  public EditSpendingPage setSpendingCategory(String category) {
    categoryInputField.setValue(category);
    return this;
  }

  @Step("Ввод суммы траты: {amount}")
  public EditSpendingPage setSpendingAmount(String amount) {
    amountInputField.setValue(amount);
    return this;
  }
}
