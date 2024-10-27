package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private static final Config CFG = Config.getInstance();
    private static final SpendDao spendDao = new SpendDaoSpringJdbc();
    private static final CategoryDao categoryDao = new CategoryDaoSpringJdbc();


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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update(
                "UPDATE spend SET spend_date, currency, amount, description)" +
                        "VALUES ( ?, ?, ?, ?",

                new java.sql.Date(spend.getSpendDate().getTime()),
                spend.getCurrency().name(),
                spend.getAmount(),
                spend.getDescription()
        );
        return spend;
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return
                jdbcTemplate.query(
                        "SELECT * FROM \"spend\" WHERE username = ? AND description = ?",
                        SpendEntityRowMapper.instance,
                        username, description
                );
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