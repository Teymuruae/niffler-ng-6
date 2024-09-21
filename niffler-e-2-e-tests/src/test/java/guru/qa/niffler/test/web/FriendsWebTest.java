package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
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

    @Test
    void friendShouldBePresentInFriendTable(@UsersQueueExtension.UserType(UsersQueueExtension.Type.WITH_FRIEND) UsersQueueExtension.StaticUser user) {
        getInsidePage(user.username(), user.password(), friends);
        friendsAllPeopleHeader.search(user.friend());
        friendsPage
                .assertFriendsTableName("My friends")
                .assertFriendInList(user.friend());
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UsersQueueExtension.UserType(UsersQueueExtension.Type.EMPTY) UsersQueueExtension.StaticUser user) {
        getInsidePage(user.username(), user.password(), friends);
        friendsAllPeopleHeader.searchFieldShouldBeEmpty();
        friendsPage.assertFriendsListIsEmpty();
    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UsersQueueExtension.UserType(UsersQueueExtension.Type.WITH_INCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        getInsidePage(user.username(), user.password(), friends);
        friendsAllPeopleHeader.search(user.income());
        friendsPage
                .assertFriendsTableName("Friend requests")
                .assertIncomeRequestInList(user.income());
    }

    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UsersQueueExtension.UserType(UsersQueueExtension.Type.WITH_OUTCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        getInsidePage(user.username(), user.password(), allPeople);
        friendsAllPeopleHeader.search(user.income());
        allPeoplePage.assertRightButtonTextByName(user.outcome(), "Waiting...");
    }
}