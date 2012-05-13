package cg.ohooligans;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    private static final OrderManager mgr = new OrderManager();

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

        getTabHost().setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if ("result".equals(s)) {

                }
            }
        });
    }

    public static OrderManager getMgr() {
        return mgr;
    }
}
