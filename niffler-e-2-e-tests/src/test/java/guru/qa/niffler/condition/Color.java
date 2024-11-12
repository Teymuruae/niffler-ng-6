package guru.qa.niffler.condition;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {
    green("rgba(53, 173, 123, 1)"),
    yellow("rgba(255, 183, 3, 1)");

    public final String rgb;
}