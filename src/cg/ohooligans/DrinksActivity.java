package cg.ohooligans;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * @author Sergey Mashkov
 */
public class DrinksActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        ArrayList<Item> items = new ArrayList<Item>();
        new ItemsHandler(items, Category.DRINKS).handle(getResources(), R.raw.drinks);

        ((ListView)findViewById(R.id.items)).setAdapter(new ItemAdapter(items, MainActivity.getMgr()));
    }
}