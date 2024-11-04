package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.DateUtils;
import io.qameta.allure.Step;

import java.util.Date;

public class Calendar extends BaseComponent<Calendar> {
    public Calendar(SelenideElement self) {
     super(self);
    }

    @Step("Выбор даты в календаре")
    public Calendar selectDateInCalendar(Date date) {
        SelenideElement input = self.$x(".//input[@name='date']");
        input.clear();
        input
                .setValue(DateUtils.fromDateToString(date, "MM/dd/yyyy"));
        return this;
    }
}