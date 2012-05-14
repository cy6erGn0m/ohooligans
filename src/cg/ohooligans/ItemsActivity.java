package cg.ohooligans;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

/**
 * @author Sergey Mashkov aka cy6erGn0m
 * @since 14.05.12
 */
public class ItemsActivity extends Activity  {

    private final Category category;
    private final String url;

    public ItemsActivity(String url, Category category) {
        this.url = url;
        this.category = category;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        List<Item> items = new DbHelper(this).loadItems(category);
        if (items.isEmpty()) {
            final ProgressDialog p = new ProgressDialog(this);
            new ItemDownloadTask(category) {
                @Override
                protected void onPostExecute(List<Item> items) {
                    updateItems(items);
                    new DbHelper(ItemsActivity.this).replaceItems(category, items);
                    p.hide();
                }
            }.execute(url);
            p.setIndeterminate(true);
            p.setTitle("Загрузка списка...");
            p.show();
        } else {
            updateItems(items);
        }
    }

    private void updateItems(List<Item> items) {
        ((ListView) findViewById(R.id.items)).setAdapter(new ItemAdapter(items, MainActivity.getMgr()));
    }
}
