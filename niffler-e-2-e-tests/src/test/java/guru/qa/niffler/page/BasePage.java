package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@Getter
public abstract class BasePage<T extends BasePage<?>> {

  protected final Header header = new Header();

  protected final SelenideElement alert = $("[role='alert']");

  @Step("Check that alert contains text {message}")
  @SuppressWarnings("unchecked")
  public T checkAlert(String message) {
    alert.shouldHave(text(message));
    return (T) this;
  }
}