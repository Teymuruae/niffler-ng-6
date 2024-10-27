package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendClient {
    SpendJson createSpend(SpendJson spend);

    Optional<SpendJson> findSpendById(UUID id);
    SpendJson updateSpend(SpendJson spend);

    List<SpendJson> findSpendByUsernameAndDescription(String username, String description);

    void removeSpend(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

    Optional<CategoryJson> findCategoryById(UUID id);

    Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name);

    void removeCategory(CategoryJson category);
}
