package cg.ohooligans;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Mashkov
 */
public class OrderManager {
    public static final int businessLunchDiscount = 30;
    
    private final Map<Item, Integer> counters = new HashMap<Item, Integer>();
    private boolean businessLunch = true;
    private int discountCard = 10;
    
    public int getPositionCount(Item item) {
        Integer c = counters.get(item);
        return c == null ? 0 : c;
    }
    
    public boolean increase(Item item) {
        Integer c = counters.get(item);
        c = c == null ? 1 : (c + 1);
        counters.put(item, c);
        
        return c == 1;
    }
    
    public boolean decrease(Item item) {
        Integer c = counters.get(item);
        if (c == null || c == 0) {
            throw new IllegalStateException("Can't decrease");
        }

        c--;
        if (c == 0) {
            counters.remove(item);
        } else {
            counters.put(item, c);
        }
        
        return c == 0;
    }

    public boolean isBusinessLunch() {
        return businessLunch;
    }

    public void setBusinessLunch(boolean businessLunch) {
        this.businessLunch = businessLunch;
    }

    public int getDiscount() {
        return discountCard;
    }

    public void setDiscount(int discount) {
        this.discountCard = discount;
    }

    public int computeSum() {
        int sum = 0;
        
        for (Map.Entry<Item, Integer> e : counters.entrySet()) {
            if (e.getValue() != null) {
                int discount = discountCard;
                if (e.getKey().getCategory() == Category.FOOD && isBusinessLunch()) {
                    discount = businessLunchDiscount;
                }

                sum += discount(e.getKey().getPrice() * e.getValue(), discount);
            }
        }
        
        return sum;
    }
    
    private static int discount(int price, int discount) {
        return (int)Math.ceil((float)price * (100 - discount) / 100);
    }
}
