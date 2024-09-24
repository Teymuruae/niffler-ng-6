package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class Friends_AllPeople_Header {

    private SelenideElement searchInput = $x("//input[@placeholder='Search' and contains(@class, 'MuiInputBase-input')]");
    private SelenideElement inputClearButton = $("#input-clear");

    public Friends_AllPeople_Header search(String value){
        searchInput.setValue(value).pressEnter();
        return this;
    }

    public Friends_AllPeople_Header clearSearchField(){
        inputClearButton.click();
        return this;
    }

    public Friends_AllPeople_Header searchFieldShouldBeEmpty(){
        searchInput.shouldHave(Condition.value(""));
        inputClearButton.shouldNotBe(Condition.exist);
        return this;
    }
}
