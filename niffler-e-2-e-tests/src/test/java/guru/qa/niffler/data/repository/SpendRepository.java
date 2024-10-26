package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {
    SpendEntity create(SpendEntity spend);

    SpendEntity update(SpendEntity spend);

    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    Optional<SpendEntity> findById(UUID id);

    List<SpendEntity> findAllByUsernameAndSpendDescription(String username, String description);

    void remove(SpendEntity spend);

    void removeCategory(CategoryEntity category);
}