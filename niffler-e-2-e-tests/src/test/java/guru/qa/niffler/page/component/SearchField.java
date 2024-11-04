package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class SearchField extends BaseComponent<SearchField> {

    private SelenideElement inputClearButton = $("#input-clear");
    public SearchField(SelenideElement self) {
        super(self);
    }

    @Step("Поиск")
    public SearchField search(String query){
        self.setValue(query).pressEnter();
        return this;
    }

    @Step("Очищение поля поиска")
    public SearchField clearIfNotEmpty(){
        if(!self.is(Condition.value(""))){
            if(inputClearButton.exists()){
                inputClearButton.click();
            } else {
                self.clear();
            }
        }
        return this;
    }

    public SearchField searchFieldShouldBeEmpty() {
        self.shouldHave(Condition.value(""));
        inputClearButton.shouldNotBe(Condition.exist);
        return this;
    }
}