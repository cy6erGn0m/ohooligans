package cg.ohooligans;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Mashkov aka cy6erGn0m
 * @since 14.05.12
 */
public class ItemsActivity extends Activity implements UpdatableActivity {

    private final Category category;
    private List<Item> items;

    public ItemsActivity(Category category) {
        this.category = category;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
    }

    @Override
    public void update() {
        items = getMainActivity().getAllItems();
        setItems();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        update();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        update();
    }

    private void setItems() {
        ArrayList<Item> filtered = new ArrayList<Item>(items.size());
        for (Item item : items) {
            if (item.getCategory() == category) {
                filtered.add(item);
            }
        }
        updateItems(filtered);
    }

    private void updateItems(List<Item> items) {
        ((ListView) findViewById(R.id.items)).setAdapter(new ItemAdapter(items, getMainActivity().getMgr()));
    }

    private MainActivity getMainActivity() {
        return (MainActivity) getParent();
    }
}
