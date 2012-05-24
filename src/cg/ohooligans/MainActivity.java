package cg.ohooligans;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainActivity extends TabActivity {

    public static final String ITEMS_URL = "http://files.sergey-mashkov.net/full.xml.gz";

    private OrderManager mgr = new OrderManager();
    private HooligansMenu menu = HooligansMenu.empty();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec discounts = tabHost.newTabSpec("discounts");
        discounts.setIndicator("Скидки");
        discounts.setContent(new Intent(this, DiscountsActivity.class));

        TabHost.TabSpec foods = tabHost.newTabSpec("foods")
            .setIndicator("Еда")
            .setContent(new Intent(this, FoodActivity.class));

        TabHost.TabSpec drink = tabHost.newTabSpec("drink")
            .setIndicator("Выпивка")
            .setContent(new Intent(this, DrinksActivity.class));

        TabHost.TabSpec result = tabHost.newTabSpec("result")
            .setIndicator("Результат")
            .setContent(new Intent(this, ResultsActivity.class));

        tabHost.addTab(discounts);
        tabHost.addTab(foods);
        getTabHost().addTab(drink);
        getTabHost().addTab(result);
    }

    @Override
    protected void onPostCreate(Bundle icicle) {
        super.onPostCreate(icicle);

        try {
            List<Item> allItems = new DbHelper(this).loadItems(null);
            List<String> favorites = new DbHelper(this).loadFavorites();

            this.menu = HooligansMenu.create(allItems, favorites);
            if (allItems.isEmpty()) {
                refreshItems();
            }
        } catch (Throwable t) {
            Toast.makeText(this, t.getClass().getName() + ": " + t.getMessage(), 10000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshMenuItem:
                refreshItems();
                return true;
            case R.id.resetMenuItem:
                reset();
                return true;
            case R.id.addItem:
                if (isValidCategoryTab()) {
                    Intent intent = new Intent(this, ItemAddActivity.class);
                    intent.putExtra("category", getCurrentCategory().name());
                    startActivityForResult(intent, 0);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            String itemTitle = (String)data.getExtras().get("item");
            if (itemTitle != null) {
                Item item = menu.find(itemTitle);
                if (item != null) {
                    if (!menu.getFavorites().contains(item)) {
                        new DbHelper(this).addFavorite(item.getTitle());
                        menu.getFavorites().add(item);

                        getMgr().increase(item);

                        updateCurrent();
                    }
                }
            }
        }
    }

    private void reset() {
        getMgr().clear();
        updateCurrent();
    }

    public void refreshItems() {
        final ProgressDialog p = new ProgressDialog(this);
        new ItemDownloadTask() {
            @Override
            protected void onPostExecute(HooligansMenu menu) {
                new DbHelper(MainActivity.this).replaceItems(menu.getItems());
                new DbHelper(MainActivity.this).replaceFavorites(menu.getFavorites());

                MainActivity.this.menu = MainActivity.this.menu.mergeWithNewer(menu);

                updateCurrent();
                p.hide();
            }
        }.execute(ITEMS_URL);
        p.setIndeterminate(true);
        p.setTitle("Загрузка списка...");
        p.show();
    }

    public OrderManager getMgr() {
        return mgr;
    }

    public List<Item> getAllItems() {
        return menu.getItems();
    }

    public Collection<Item> getFavorites() {
        return menu.getFavorites();
    }

    public void updateCurrent() {
        if (getCurrentActivity() instanceof UpdatableActivity) {
            ((UpdatableActivity)getCurrentActivity()).update();
        }
    }

    public boolean isValidCategoryTab() {
        String tab = getTabHost().getCurrentTabTag();
        return Arrays.asList("foods", "drink").indexOf(getTabHost().getCurrentTabTag()) != -1;
    }

    public Category getCurrentCategory() {
        String tab = getTabHost().getCurrentTabTag();
        if ("foods".equals(tab)) {
            return Category.FOOD;
        } else if ("drink".equals(tab)) {
            return Category.DRINKS;
        }

        throw new IllegalStateException("Invalid tab");
    }
}
