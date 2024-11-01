package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@WebTest
public class FriendsWebTest {
    private Header header = new Header();
    private FriendsPage friendsPage = new FriendsPage();
    private AllPeoplePage allPeoplePage = new AllPeoplePage();
    private Friends_AllPeople_Header friendsAllPeopleHeader = new Friends_AllPeople_Header();
    private static final Config CFG = Config.getInstance();
    private String friends = "Friends";
    private String allPeople = "All People";

    private void getInsidePage(String username, String password, String pageName) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);
        header.clickProfileMenuButton(pageName);
    }

    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendTable(UserJson user) {
        getInsidePage(user.username(), user.testData().password(), friends);
        friendsAllPeopleHeader.search(user.testData().friends().get(0));
        friendsPage
                .assertFriendsTableName("My friends")
                .assertFriendInList(user.testData().friends().get(0));
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        getInsidePage(user.username(), user.testData().password(), friends);
        friendsAllPeopleHeader.searchFieldShouldBeEmpty();
        friendsPage.assertFriendsListIsEmpty();
    }

    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        getInsidePage(user.username(), user.testData().password(), friends);
        friendsAllPeopleHeader.search(user.testData().incomeInvitations().get(0));
        friendsPage
                .assertFriendsTableName("Friend requests")
                .assertIncomeRequestInList(user.testData().incomeInvitations().get(0));
    }

    @User
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        getInsidePage(user.username(), user.testData().password(), allPeople);
        friendsAllPeopleHeader.search(user.testData().outcomeInvitations().get(0));
        allPeoplePage.assertRightButtonTextByName(user.testData().outcomeInvitations().get(0), "Waiting...");
    }
}