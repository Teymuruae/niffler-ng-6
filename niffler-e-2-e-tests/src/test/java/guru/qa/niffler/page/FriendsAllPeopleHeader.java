package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsAllPeopleHeader extends BasePage<FriendsAllPeopleHeader>{

    private SelenideElement searchInput = $x("//input[@placeholder='Search' and contains(@class, 'MuiInputBase-input')]");
    public SearchField searchField = new SearchField(searchInput);

    private ElementsCollection headerScrollButtons = $$("[aria-label='People tabs'] [role='tab']");

    private void toPage(String pageName) {
        SelenideElement element = headerScrollButtons.findBy(Condition.text(pageName));
        if (element.is(Condition.attribute("aria-selected", "false"))) {
            element.click();
        }
    }

    public FriendsPage toFriendsPage(){
        toPage("Friends");
        return new FriendsPage();
    }

    public AllPeoplePage toAllPeoplePage(){
        toPage("All people");
        return new AllPeoplePage();
    }
}