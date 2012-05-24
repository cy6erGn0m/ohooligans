package cg.ohooligans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Mashkov aka cy6erGn0m
 * @since 24.05.12
 */
public class ItemAddActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.items);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        List<Item> all = getAllItems();

        ArrayList<Item> items = new ArrayList<Item>(all);
        Collections.sort(items);
//        for (Item item : all) {
//            if (item.getCategory() == cat) {
//                items.add(item);
//            }
//        }

        ((ListView) findViewById(R.id.items)).setAdapter(new ItemAddAdapter(items, this));
    }

    private List<Item> getAllItems() {
//        return getMainActivity().getAllItems();

        DbHelper helper = new DbHelper(this);
        return helper.loadItems(Category.valueOf(getIntent().getExtras().get("category").toString()));
    }

    private boolean hasMainActivity() {
        return getParent() != null;
    }

    private MainActivity getMainActivity() {
        return (MainActivity) getParent();
    }

    void onItemAdded(Item item) {
        Intent result = new Intent();
        result.putExtra("item", item.getTitle());

        setResult(0, result);
        finish();
    }

}
