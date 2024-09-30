package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;


public class SpendDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int ISOLATION_LVL = Connection.TRANSACTION_SERIALIZABLE;

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findSpendById(id);
                },
                CFG.spendJdbcUrl()
        );
    }

    public List<SpendEntity> findAllSpendsByUsername(String username) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findAllByUsername(username);
                },
                CFG.spendJdbcUrl()
        );
    }

    public void deleteSpend(SpendEntity spend) {
        transaction(connection -> {
                    new SpendDaoJdbc(connection).deleteSpend(spend);
                },
                CFG.spendJdbcUrl()
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(
                            new CategoryDaoJdbc(connection).create(categoryEntity));
                },
                CFG.spendJdbcUrl(),
                ISOLATION_LVL
        );
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryById(id);
                },
                CFG.spendJdbcUrl(),
                ISOLATION_LVL
        );
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName);
                },
                CFG.spendJdbcUrl(),
                ISOLATION_LVL
        );
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findAllByUsername(username);
                },
                CFG.spendJdbcUrl(),
                ISOLATION_LVL
        );
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
                },
                CFG.spendJdbcUrl(),
                ISOLATION_LVL
        );
    }
}
