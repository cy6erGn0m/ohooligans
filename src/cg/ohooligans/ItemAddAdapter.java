package cg.ohooligans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * @author Sergey Mashkov aka cy6erGn0m
 * @since 24.05.12
 */
public class ItemAddAdapter extends BaseAdapter {

    private List<Item> items;
    private final ItemAddActivity activity;

    public ItemAddAdapter(List<Item> items, ItemAddActivity activity) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_view, null);
        }
        TextView title = (TextView)view.findViewById(R.id.caption);
        final TextView count = (TextView)view.findViewById(R.id.count);

        final Button minusButton = (Button)view.findViewById(R.id.minusButton);
        Button plusButton = (Button)view.findViewById(R.id.plusButton);

        final Item item = (Item) getItem(i);

        title.setText(item.getTitle() + " (" + item.getPrice() + ")");
        count.setText("");
        minusButton.setEnabled(false);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onItemAdded(item);
            }
        });

        return view;
    }

}
