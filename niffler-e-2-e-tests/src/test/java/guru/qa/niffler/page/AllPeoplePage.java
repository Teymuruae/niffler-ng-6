package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {
    private SelenideElement allPeopleTable = $("#all");
    private ElementsCollection names = allPeopleTable.$$(".MuiTypography-body1");

    public AllPeoplePage assertRightButtonTextByName(String name, String text){
        allPeopleTable.$x(String.format(".//p[contains(@class, 'MuiTypography-root MuiTypography-body1') and text() = '%s']" +
                "//ancestor::tr//td[contains(@class, 'MuiTableCell-alignRight')]", name)).shouldHave(Condition.innerText(text));
        return this;
    }
}
