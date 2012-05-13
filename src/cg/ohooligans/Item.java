package cg.ohooligans;

/**
 * @author Sergey Mashkov
 */
public class Item {
    private final String title;
    private final int price;
    private final Category category;

    public Item(String title, int price, Category category) {
        this.title = title;
        this.price = price;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }
}
