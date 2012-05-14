package cg.ohooligans;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import java.util.Collections;
import java.util.List;

public class MainActivity extends TabActivity {

    public static final String ITEMS_URL = "http://files.sergey-mashkov.net/full.xml";
    private final OrderManager mgr = new OrderManager();
    private List<Item> allItems = Collections.emptyList();

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

        allItems = new DbHelper(this).loadItems(null);
        if (allItems.isEmpty()) {
            refreshItems();
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void reset() {
        getMgr().clear();
        updateCurrent();
    }

    public void refreshItems() {
        final ProgressDialog p = new ProgressDialog(this);
        new ItemDownloadTask() {
            @Override
            protected void onPostExecute(List<Item> items) {
                new DbHelper(MainActivity.this).replaceItems(items);
                allItems = items;
                reset();
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
        return allItems;
    }

    public void updateCurrent() {
        if (getCurrentActivity() instanceof UpdatableActivity) {
            ((UpdatableActivity)getCurrentActivity()).update();
        }
    }
}
