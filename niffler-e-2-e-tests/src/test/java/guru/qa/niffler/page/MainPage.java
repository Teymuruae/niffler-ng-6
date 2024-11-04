package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage extends BasePage<MainPage>{
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final ElementsCollection titlesText = $$("h2");
  private SelenideElement searchInput = $x("//input[@placeholder='Search' and contains(@class, 'MuiInputBase-input')]");

  public EditSpendingPage editSpending(String spendingDescription) {
    search(spendingDescription);
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    search(spendingDescription);
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public MainPage mainPageShouldBeDisplayed(){
    titlesText.find(Condition.text("Statistics")).shouldBe(visible);
    titlesText.find(Condition.text("History of Spendings")).shouldBe(visible);
    return this;
  }

  public MainPage search(String value){
    searchInput.setValue(value).pressEnter();
    return this;
  }
}
