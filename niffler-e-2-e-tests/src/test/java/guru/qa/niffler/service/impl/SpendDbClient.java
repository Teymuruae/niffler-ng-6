package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;


public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();
    private static final int ISOLATION_LVL = Connection.TRANSACTION_SERIALIZABLE;
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );
    private final SpendRepository spendRepository = new SpendRepositoryJdbc();

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    return SpendJson.fromEntity(
                            spendRepository.create(spendEntity)
                    );
                }
        );
    }

    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
                    return Optional.of(SpendJson.fromEntity(spendRepository.findById(id).get()));
                }
        );
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() ->
                SpendJson.fromEntity(spendRepository.update(SpendEntity.fromJson(spend))));
    }

    @Override
    public List<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        return xaTransactionTemplate.execute(() -> {
            List<SpendJson> spends = new ArrayList<>();
            spendRepository.findAllByUsernameAndSpendDescription(username, description).forEach(spend ->
                    spends.add(SpendJson.fromEntity(spend)));
            return spends;
        });
    }

    @Override
    public void removeSpend(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            spendRepository.remove(SpendEntity.fromJson(spend));
            return null;
        });
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(
                    spendRepository.createCategory(categoryEntity));
        });
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            return Optional.of(CategoryJson.fromEntity(spendRepository.findCategoryById(id).get()));
        });
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return xaTransactionTemplate.execute(() -> {
            return Optional.of(CategoryJson.fromEntity(spendRepository
                    .findCategoryByUsernameAndCategoryName(username, categoryName).get()));
        });
    }

    @Override
    public void removeCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            spendRepository.removeCategory(CategoryEntity.fromJson(category));
            return null;
        });
    }
}
