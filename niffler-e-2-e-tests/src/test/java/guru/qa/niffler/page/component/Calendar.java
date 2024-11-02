package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.DateUtils;
import io.qameta.allure.Step;

import java.util.Date;

public class Calendar {
    private final SelenideElement self;

    public Calendar(SelenideElement self) {
        this.self = self;
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