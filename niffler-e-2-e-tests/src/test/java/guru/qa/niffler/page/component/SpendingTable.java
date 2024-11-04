package guru.qa.niffler.page.component;

import com.codeborne.selenide.*;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class SpendingTable extends BaseComponent<SpendingTable>{

    private final ElementsCollection spendingRows = self.$$("tbody tr");
    private final SelenideElement modalDialog = $("div[role='dialog']");

    public SpendingTable(){
        super($("#spendings"));
    }
    @Step("Выбор периода")
    public SpendingTable selectPeriod(DataFilterValues period) {
        self.$("#period").click();
        $("li[data-value='" + period.name() + "']").click();
        return this;
    }

    @Step("Редактирование траты")
    public EditSpendingPage editSpending(String description) {
        spendingRows.findBy(Condition.text(description)).$("button[aria-label='Edit spending']").click();
        return new EditSpendingPage();
    }

    @Step("Удаление траты")
    public SpendingTable deleteSpending(String description) {
        spendingRows.findBy(Condition.text(description)).$("input[type='checkbox']").click();
        self.$("#delete").click();
        modalDialog.$(Selectors.byTagAndText("button", "Delete")).click();
        return this;
    }

    @Step("Поиск траты ")
    public SpendingTable searchSpendingByDescription(String description) {
        spendingRows.findBy(Condition.text(description)).shouldBe(Condition.visible);
        return this;
    }

    @Step("Проверка наличия траты")
    public SpendingTable checkTableContains(String... expectedSpends) {
        for (String spend : expectedSpends) {
            spendingRows.find(Condition.text(spend)).shouldBe(Condition.visible);
        }
        return this;
    }

    @Step("Проверка размера списка трат")
    public SpendingTable checkTableSize(int expectedSize) {
        spendingRows.shouldHave(CollectionCondition.size(expectedSize));
        return this;
    }
}