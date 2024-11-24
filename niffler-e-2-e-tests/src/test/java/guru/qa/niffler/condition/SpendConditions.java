package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.utils.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class SpendConditions {
    private static final String
            CATEGORY = "Category",
            AMOUNT = "Amount",
            DESCRIPTION = "Description",
            DATE = "Date";

    @Nonnull
    public static WebElementsCondition spends(@Nonnull SpendJson... expectedSpends
    ) {
        return new WebElementsCondition() {

            Map<String, ?> expectedMap = new ConcurrentHashMap<>();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedSpends)) {
                    throw new IllegalArgumentException("No expected spends given");
                }
                if (expectedSpends.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedSpends.length, elements.size());
//                    return rejected(message, elements);
                    throw new SpendMismatchException(message);
                }

                for (int i = 0; i < elements.size(); i++) {
                    final List<WebElement> rowElements = elements.get(i).findElements(By.tagName("td"));

                    Map<String, ?> actualMap = setMap(rowElements);
                    expectedMap = setMap(expectedSpends[i]);

                    if (!expectedMap.equals(actualMap)) {
                        String expected = expectedMap.toString();
                        String actual = actualMap.toString();
                        String message = String.format("Actual and expected spend are not equals (expected: %s, actual: %s)",
                                expected, actual);
//                        return rejected(message, actual);
                        throw new SpendMismatchException(message);
                    }

                }
                return accepted();
            }

            //то, что будет выводиться в сообщении ошибки. То есть should have ...
            @Override
            public String toString() {
                return expectedMap.toString();
            }

        };
    }

    private static Map<String, ?> setMap(List<WebElement> rowElements) {
        return new ConcurrentHashMap<>() {{
            put(CATEGORY, rowElements.get(1).getText());
            put(AMOUNT, Double.parseDouble(rowElements.get(2).getText().split(" ")[0]));
            put(DESCRIPTION, rowElements.get(3).getText());
            put(DATE, rowElements.get(4).getText());
        }};
    }

    private static Map<String, ?> setMap(SpendJson spendJson) {
        final String date = DateUtils.getDateInFormat(spendJson.spendDate(), "MMM dd, yyyy");

        return new ConcurrentHashMap<>() {{
            put(CATEGORY, spendJson.category().name());
            put(AMOUNT, spendJson.amount());
            put(DESCRIPTION, spendJson.description());
            put(DATE, date);
        }};
    }

    // Custom exception class to handle spend mismatches
    public static class SpendMismatchException extends AssertionError {
        public SpendMismatchException(String message) {
            super(message);
        }
    }
}