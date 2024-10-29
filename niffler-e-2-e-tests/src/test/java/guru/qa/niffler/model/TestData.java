package guru.qa.niffler.model;

import java.util.List;

public record TestData(
        String password,
        List<CategoryJson> categories,
        List<SpendJson> spendings,
        List<String> incomeInvitations,
        List<String> outcomeInvitations,
        List<String> friends
) {
}
