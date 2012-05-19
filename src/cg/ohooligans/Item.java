package cg.ohooligans;

import org.jetbrains.annotations.NotNull;

/**
 * @author Sergey Mashkov
 */
public class Item {
    private final String title;
    private final int price;
    private final Category category;

    public Item(String title, int price, Category category) {
        if (title == null) {
            throw new IllegalArgumentException("title should not be null");
        }
        if (price < 0) {
            throw new IllegalArgumentException("price should not be negative");
        }
        if (category == null) {
            throw new IllegalArgumentException("category should not be null");
        }

        this.title = title;
        this.price = price;
        this.category = category;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    @NotNull
    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (category != item.category) return false;
        if (!title.equals(item.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
