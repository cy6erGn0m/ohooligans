package cg.ohooligans;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Sergey Mashkov aka cy6erGn0m
 * @since 19.05.12
 */
public class HooligansMenu {
    private final List<Item> items;
    private final Set<Item> favorites = new HashSet<Item>(256);

    public HooligansMenu(List<Item> items, Set<Item> favorites) {
        if (items == null) {
            throw new IllegalArgumentException("items should not be null");
        }
        if (favorites == null) {
            throw new IllegalArgumentException("favorites should not be null");
        }

        this.items = Collections.unmodifiableList(items);
        this.favorites.addAll(favorites);
    }

    @NotNull
    public List<Item> getItems() {
        return items;
    }

    @NotNull
    public Set<Item> getFavorites() {
        return favorites;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public static HooligansMenu empty() {
        return new HooligansMenu(Collections.<Item>emptyList(), Collections.<Item>emptySet());
    }

    public static HooligansMenu create(List<Item> items, Collection<String> favorites) {
        Map<String, Item> byName = new HashMap<String, Item>(items.size());
        for (Item item : items) {
            byName.put(item.getTitle(), item);
        }

        Set<Item> favsSet = new HashSet<Item>(favorites.size());
        for (String name : favorites) {
            Item item = byName.get(name);
            if (item != null) {
                favsSet.add(item);
            }
        }

        return new HooligansMenu(items, favsSet);
    }

    public HooligansMenu mergeWithNewer(HooligansMenu m) {
        HooligansMenu result = new HooligansMenu(m.items, m.favorites);
        result.favorites.addAll(this.favorites);

        return result;
    }

    public Item find(String title) {
        for (Item item : items) {
            if (title.equals(item.getTitle())) {
                return item;
            }
        }

        return null;
    }
}
