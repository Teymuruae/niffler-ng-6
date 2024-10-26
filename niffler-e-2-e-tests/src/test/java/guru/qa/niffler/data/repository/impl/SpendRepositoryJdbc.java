package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendRepositoryJdbc implements SpendRepository {

    private static final Config CFG = Config.getInstance();
    private static final SpendDao spendDao = new SpendDaoJdbc();
    private static final CategoryDao categoryDao = new CategoryDaoJdbc();


    @Override
    public SpendEntity create(SpendEntity spend) {
        UUID catId = spend.getCategory().getId();
        Optional<CategoryEntity> categoryEntity = findCategoryByUsernameAndCategoryName(spend.getCategory().getUsername(),
                spend.getCategory().getName());
        if (catId == null && categoryEntity.isPresent()) {
            spend.getCategory().setId(categoryEntity.get().getId());
        } else if (catId == null && !categoryEntity.isPresent()) {
            spend.setCategory(createCategory(spend.getCategory()));
        }
        return spendDao.create(spend);
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        try (PreparedStatement spendPs = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "UPDATE spend SET spend_date, currency, amount, description)" +
                        "VALUES ( ?, ?, ?, ?")) {
            spendPs.setDate(1, new java.sql.Date(spend.getSpendDate().getTime()));
            spendPs.setString(2, spend.getCurrency().name());
            spendPs.setDouble(3, spend.getAmount());
            spendPs.setString(4, spend.getDescription());

            spendPs.executeUpdate();
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        return spendDao.findSpendById(id);
    }

    @Override
    public List<SpendEntity> findAllByUsernameAndSpendDescription(String username, String description) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend where username = ? AND description = ?"
        )) {
            ps.setString(1, username);
            ps.setString(2, description);
            ps.execute();

            List<SpendEntity> entities = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {

                while (rs.next()) {
                    SpendEntity entity = new SpendEntity();
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUsername(rs.getString("username"));
                    entity.setSpendDate(rs.getDate("spend_date"));
                    entity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    entity.setAmount(rs.getDouble("amount"));
                    entity.setDescription(rs.getString("description"));
                    entity.setCategory(
                            new CategoryDaoJdbc().findCategoryById(rs
                                    .getObject("category_id", UUID.class)).orElseThrow());
                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(SpendEntity spend) {
        spendDao.deleteSpend(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }
}
