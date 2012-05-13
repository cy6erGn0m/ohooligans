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
 * @author Sergey Mashkov
 */
public class ItemAdapter extends BaseAdapter {
    
    private List<Item> items;
    private final OrderManager mgr;

    public ItemAdapter(List<Item> items, OrderManager mgr) {
        this.items = items;
        this.mgr = mgr;
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
        final int quantity = mgr.getPositionCount(item);

        title.setText(item.getTitle() + " (" + item.getPrice() + ")");
        count.setText(quantity == 0 ? "" : Integer.toString(quantity));
        minusButton.setEnabled(quantity > 0);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mgr.decrease(item)) {
                    minusButton.setEnabled(false);
                }
                int quantity = mgr.getPositionCount(item);
                count.setText(quantity == 0 ? "" : Integer.toString(quantity));
                view.refreshDrawableState();
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mgr.increase(item)) {
                    minusButton.setEnabled(true);
                }
                int quantity = mgr.getPositionCount(item);
                count.setText(quantity == 0 ? "" : Integer.toString(quantity));
                view.refreshDrawableState();
            }
        });

        return view;
    }

}
