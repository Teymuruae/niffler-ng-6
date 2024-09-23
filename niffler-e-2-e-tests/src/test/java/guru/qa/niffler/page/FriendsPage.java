package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {
   private SelenideElement tableName = $("#simple-tabpanel-friends h2.MuiTypography-root.MuiTypography-h5");
   private SelenideElement friendsListTable = $("#friends");
   private SelenideElement requestsListTable = $("#requests");

   private ElementsCollection friendsListNames = friendsListTable.$$x(".//p[contains(@class, 'MuiTypography-body1') and not(@id)]");
   private ElementsCollection requestsListNames = requestsListTable.$$x(".//p[contains(@class, 'MuiTypography-body1') and not(@id)]");
   private SelenideElement noUsersYetText = $("p.MuiTypography-root.MuiTypography-h6");

   public FriendsPage assertFriendsTableName(String tableNameText){
      tableName.shouldBe(visible).shouldHave(text(tableNameText));
      return this;
   }
   public FriendsPage assertFriendInList(String friendName){
       friendsListNames.findBy(Condition.text(friendName)).shouldBe(visible);
       return this;
   }

   public FriendsPage assertIncomeRequestInList(String requestFriendName){
      requestsListNames.findBy(Condition.text(requestFriendName)).shouldBe(visible);
      return this;
   }

   public FriendsPage assertFriendsListIsEmpty(){
      friendsListTable.shouldNotBe(exist);
      noUsersYetText.shouldBe(visible).shouldHave(text("There are no users yet"));
      return this;
   }
}