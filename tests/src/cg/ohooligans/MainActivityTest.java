package cg.ohooligans;

import android.test.ActivityInstrumentationTestCase2;

import java.util.*;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class cg.ohooligans.MainActivityTest \
 * cg.ohooligans.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super("cg.ohooligans", MainActivity.class);
    }

    public void testItemsMenu1() throws Exception {
        HooligansMenu menu = HooligansMenu.create(
                list("A", "B", "C"),
                set("A")
        );

        assertFalse(menu.isEmpty());
        assertEquals(1, menu.getFavorites().size());
        assertEquals(3, menu.getItems().size());

        menu.getFavorites().add(createF("B"));

        assertEquals(2, menu.getFavorites().size());
    }

    public void testItemsMenuFavs() throws Exception {
        HooligansMenu menu = HooligansMenu.create(
            list("A", "B", "C"),
            set("A", "X")
        );

        assertFalse(menu.isEmpty());
        assertEquals(1, menu.getFavorites().size());
        assertEquals(3, menu.getItems().size());
    }

    public void testOrderManagerNoDiscounts() throws Exception {
        OrderManager manger = new OrderManager();
        manger.setBusinessLunch(false);
        manger.setDiscount(0);

        Item a = createF("A");
        manger.increase(a);
        manger.increase(a);

        assertEquals(100, manger.computeSum());
    }

    public void testOrderManagerNoDiscountsDifferentInstances() throws Exception {
        OrderManager manger = new OrderManager();
        manger.setBusinessLunch(false);
        manger.setDiscount(0);

        manger.increase(createF("A"));
        manger.increase(createF("A"));

        assertEquals(100, manger.computeSum());
    }

    public void testOrderManagerNoDiscountsZero() throws Exception {
        OrderManager manger = new OrderManager();
        manger.setBusinessLunch(false);
        manger.setDiscount(0);

        manger.increase(createF("A"));
        manger.decrease(createF("A"));

        assertEquals(0, manger.computeSum());
    }

    public void testOrderManagerBL() throws Exception {
        OrderManager manger = new OrderManager();
        manger.setBusinessLunch(true);
        manger.setDiscount(0);

        manger.increase(createF("A"));
        manger.increase(create("B", Category.DRINKS));

        assertEquals(35 + 50, manger.computeSum());
    }

    public void testOrderManagerNoDiscountsBLAndDiscount() throws Exception {
        OrderManager manger = new OrderManager();
        manger.setBusinessLunch(true);
        manger.setDiscount(10);

        manger.increase(createF("A"));
        manger.increase(create("B", Category.DRINKS));

        assertEquals(35 + 45, manger.computeSum());
    }

    public void testDLItems() throws Exception {

    }

    private static List<Item> list(String ... names) {
        ArrayList<Item> items = new ArrayList<Item>(names.length);
        for (String name : names)
            items.add(createF(name));
        return items;
    }

    private static Set<String> set(String ... names) {
        return new HashSet<String>(Arrays.asList(names));
    }

    private static Item createF(String name) {
        return create(name, Category.FOOD);
    }

    private static Item create(String name, Category category) {
        return new Item(name, 50, category);
    }

    private static Item create(String name, Category category, int price) {
        return new Item(name, price, category);
    }
}
