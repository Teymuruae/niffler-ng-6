package guru.qa.niffler.condition;

import com.codeborne.selenide.*;
import guru.qa.niffler.page.component.Bubble;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class StatConditions {

    @Nonnull
    public static WebElementCondition color(Color expectedColor) {
        //name - то, что будет выводиться в сообщении ошибки. То есть should have color ...
        return new WebElementCondition("color " + expectedColor.rgb) {
            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                final String rgba = element.getCssValue("background-color");
                return new CheckResult(
                        expectedColor.rgb.equals(rgba),
                        rgba
                );
            }
        };
    }

    @Nonnull
    public static WebElementsCondition color(@Nonnull Color... expectedColors) {
        return new WebElementsCondition() {

            private final String expectedRgba = Arrays.stream(expectedColors).map(c -> c.rgb).toList().toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }
                if (expectedColors.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedColors.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                final List<String> actualRgbaList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = expectedColors[i];
                    final String rgba = elementToCheck.getCssValue("background-color");
                    actualRgbaList.add(rgba);
                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba);
                    }
                }

                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    final String message = String.format(
                            "List colors mismatch (expected: %s, actual: %s)", expectedRgba, actualRgba
                    );
                    return rejected(message, actualRgba);
                }
                return accepted();
            }

            //то, что будет выводиться в сообщении ошибки. То есть should have ...
            @Override
            public String toString() {
                return expectedRgba;
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubbles(@Nonnull Bubble... bubbles
    ) {
        return new WebElementsCondition() {
            final List<Map> expectedBubblesList = new ArrayList<>();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(bubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (bubbles.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", bubbles.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;

                final List<Map> actualBubblesList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color expectedColor = bubbles[i].color();
                    final String expectedText = bubbles[i].text();
                    final String actualRgba = elementToCheck.getCssValue("background-color");
                    final String actualText = elementToCheck.getText();

                    Map<String, String> expectedMap = new ConcurrentHashMap<>() {{
                        put("rgb", expectedColor.rgb);
                        put("text", expectedText);
                    }};

                    Map<String, String> actualMap = new ConcurrentHashMap<>() {{
                        put("rgb", actualRgba);
                        put("text", actualText);
                    }};
                    expectedBubblesList.add(expectedMap);
                    actualBubblesList.add(actualMap);

                    if (passed) {
                        passed = expectedColor.rgb.equals(actualRgba) && expectedText.equals(actualText);
                    }
                }

                if (!passed) {
                    final String actualBubbles = actualBubblesList.toString();
                    final String expectedBubbles = expectedBubblesList.toString();
                    Selenide.sleep(3000);
                    final String message = String.format(
                            "List bubbles mismatch (expected: %s, actual: %s)", expectedBubbles, actualBubbles
                    );
                    return rejected(message, actualBubbles);
                }
                return accepted();
            }

            //то, что будет выводиться в сообщении ошибки. То есть should have ...
            @Override
            public String toString() {
                return expectedBubblesList.toString();
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesInAnyOrder(@Nonnull Bubble... bubbles
    ) {
        return new WebElementsCondition() {
            final List<Map<String, String>> expectedBubblesList = new ArrayList<>();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(bubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (bubbles.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", bubbles.length, elements.size());
                    return rejected(message, elements);
                }

                final List<Map<String, String>> actualBubblesList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color expectedColor = bubbles[i].color();
                    final String expectedText = bubbles[i].text();
                    final String actualRgba = elementToCheck.getCssValue("background-color");
                    final String actualText = elementToCheck.getText();

                    Map<String, String> expectedMap = new ConcurrentHashMap<>() {{
                        put("rgb", expectedColor.rgb);
                        put("text", expectedText);
                    }};

                    Map<String, String> actualMap = new ConcurrentHashMap<>() {{
                        put("rgb", actualRgba);
                        put("text", actualText);
                    }};
                    expectedBubblesList.add(expectedMap);
                    actualBubblesList.add(actualMap);

                }

                boolean passed = actualBubblesList.containsAll(expectedBubblesList);

                if (!passed) {
                    final String actualBubbles = actualBubblesList.toString();
                    final String expectedBubbles = expectedBubblesList.toString();
                    Selenide.sleep(3000);
                    final String message = String.format(
                            "List bubbles mismatch (expected: %s, actual: %s)", expectedBubbles, actualBubbles
                    );
                    return rejected(message, actualBubbles);
                }
                return accepted();
            }

            //то, что будет выводиться в сообщении ошибки. То есть should have ...
            @Override
            public String toString() {
                return expectedBubblesList.toString();
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesContains(@Nonnull Bubble... bubbles
    ) {
        return new WebElementsCondition() {

            final List<Map> expectedBubblesList = new ArrayList<>();
            final List<Map> actualBubblesList = new ArrayList<>();
            final List<Map> diffBubblesList = new ArrayList<>();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(bubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }

                if (bubbles.length > elements.size()) {
                    final String message = String.format("Expected bubbles list size are more than actual (expected: %d, actual: %d)",
                            bubbles.length, elements.size());
                    return rejected(message, elements);
                }


                for (int i = 0; i < elements.size(); i++) {
                    Map<String, String> actualMap = elementToMap(elements.get(i));
                    actualBubblesList.add(actualMap);
                }

                for (int i = 0; i < bubbles.length; i++) {
                    Map<String, String> expectedMap = bubbleToMap(bubbles[i]);
                    expectedBubblesList.add(expectedMap);
                }

                for (int i = 0; i < expectedBubblesList.size(); i++) {
                    boolean passed = actualBubblesList.contains(expectedBubblesList.get(i));
                    if (!passed) {
                        diffBubblesList.add(expectedBubblesList.get(i));
                    }
                }

                if (diffBubblesList.size() > 0) {
                    final String actualBubbles = actualBubblesList.toString();
                    final String diffBubbles = diffBubblesList.toString();

                    Selenide.sleep(3000);
                    final String message = String.format(
                            "Bubbles %s not in actual list: %s", diffBubbles, actualBubbles
                    );
                    return rejected(message, actualBubbles);
                }
                return accepted();
            }

            //то, что будет выводиться в сообщении ошибки. То есть should have ...
            @Override
            public String toString() {
                return expectedBubblesList.toString();
            }
        };
    }

    private static Map<String, String> bubbleToMap(Bubble bubble) {
        return new ConcurrentHashMap<>() {{
            put("rgb", bubble.color().rgb);
            put("text", bubble.text());
        }};
    }

    private static Map<String, String> elementToMap(WebElement element) {
        return new ConcurrentHashMap<>() {{
            put("rgb", element.getCssValue("background-color"));
            put("text", element.getText());
        }};
    }
}
