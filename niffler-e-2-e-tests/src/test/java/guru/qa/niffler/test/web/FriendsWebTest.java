package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.*;
import guru.qa.niffler.page.component.Header;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {
    private Header header = new Header();
    private FriendsPage friendsPage = new FriendsPage();
    private AllPeoplePage allPeoplePage = new AllPeoplePage();
    private FriendsAllPeopleHeader friendsAllPeopleHeader = new FriendsAllPeopleHeader();
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
        friendsAllPeopleHeader.searchField.search(user.testData().friends().get(0));
        friendsPage
                .assertFriendsTableName("My friends")
                .assertFriendInList(user.testData().friends().get(0));
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        getInsidePage(user.username(), user.testData().password(), friends);
        friendsAllPeopleHeader.searchField.searchFieldShouldBeEmpty();
        friendsPage.assertFriendsListIsEmpty();
    }

    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        getInsidePage(user.username(), user.testData().password(), friends);
        friendsAllPeopleHeader.searchField.search(user.testData().incomeInvitations().get(0));
        friendsPage
                .assertFriendsTableName("Friend requests")
                .assertIncomeRequestInList(user.testData().incomeInvitations().get(0));
    }

    @User
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        getInsidePage(user.username(), user.testData().password(), allPeople);
        friendsAllPeopleHeader.searchField.search(user.testData().outcomeInvitations().get(0));
        allPeoplePage.assertRightButtonTextByName(user.testData().outcomeInvitations().get(0), "Waiting...");
    }

    @User(incomeInvitations = 1)
    @Test
    void acceptFriendInvitationTest(UserJson user) {
        getInsidePage(user.username(), user.testData().password(), friends);
        friendsAllPeopleHeader.searchField.search(user.testData().incomeInvitations().get(0));
        friendsPage
                .assertFriendsTableName("Friend requests")
                .assertIncomeRequestInList(user.testData().incomeInvitations().get(0))
                .accept()
                .assertFriendsTableName("My friends")
                .assertFriendInList(user.testData().incomeInvitations().get(0));
    }

    @User(incomeInvitations = 1)
    @Test
    void declineFriendInvitationTest(UserJson user) {
        getInsidePage(user.username(), user.testData().password(), friends);
        friendsAllPeopleHeader.searchField.search(user.testData().incomeInvitations().get(0));
        friendsPage
                .assertFriendsTableName("Friend requests")
                .assertIncomeRequestInList(user.testData().incomeInvitations().get(0))
                .decline();
        friendsAllPeopleHeader.searchField.search(user.testData().incomeInvitations().get(0));
        friendsPage.assertFriendsListIsEmpty();
        header.signOut();
        getInsidePage(user.testData().incomeInvitations().get(0), "12345", friends);
        friendsAllPeopleHeader.searchField.search(user.username());
        friendsPage.assertFriendsListIsEmpty();
        friendsAllPeopleHeader.toAllPeoplePage();
        friendsAllPeopleHeader.searchField.search(user.username());
        allPeoplePage.assertRightButtonTextByName(user.username(), "Add friend");
    }
}