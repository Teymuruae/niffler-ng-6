package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Step("Создание траты")
    @Nullable
    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;

        try {
            response =
                    spendApi.addSpend(spend)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.CREATED_201, response.code());
        return response.body();
    }

    @Step("Редактирование траты")
    @Nullable
    public SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;

        try {
            response =
                    spendApi.editSpend(spend)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Получение одной траты")
    @Nonnull
    public SpendJson getSpend(String id, String userName) {
        final Response<SpendJson> response;

        try {
            response =
                    spendApi.getSpend(id, userName)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Получение всех трат")
    @Nonnull
    public List<SpendJson> getAllSpends(String userName,
                                        @Nullable CurrencyValues currencyValues,
                                        @Nullable String from,
                                        @Nullable String to) {
        final Response<List<SpendJson>> response;

        try {
            response =
                    spendApi.getSpends(userName, currencyValues, from, to)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Удаление категории")
    @Nullable
    public SpendJson removeSpend(String userName, List<String> ids) {
        final Response<SpendJson> response;

        try {
            response =
                    spendApi.deleteSpends(userName, ids)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Создание категории")
    @Nullable
    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;

        try {
            response =
                    spendApi.addCategory(category)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Редактирование категории")
    @Nullable
    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;

        try {
            response =
                    spendApi.updateCategory(category)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body();
    }

    @Step("Получение всех категорий по пользователю")
    @Nonnull
    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;

        try {
            response =
                    spendApi.getCategories(username, excludeArchived)
                            .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        Assertions.assertEquals(HttpStatus.OK_200, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }
}