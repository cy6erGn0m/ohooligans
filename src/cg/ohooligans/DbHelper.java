package cg.ohooligans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Sergey Mashkov aka cy6erGn0m
 * @since 14.05.12
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "OHDatabase", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE ITEMS (NAME TEXT, PRICE INTEGER, CATEGORY TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE FAVORITES (NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.beginTransaction();

            for (; i < i1; ++i) {
                switch (i) {
                    case 1:
                    case 2:
                        db.delete("ITEMS", null, null);
                        break;
                    case 3:
                        db.execSQL("CREATE TABLE FAVORITES (NAME TEXT)");
                        break;
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Item> loadItems(@Nullable Category category) {
        String sql = "SELECT NAME, PRICE, CATEGORY FROM ITEMS";
        if (category != null) {
            sql += " WHERE CATEGORY = '" + category.name() + "'";
        }

        SQLiteDatabase db = getReadableDatabase();
        try {
            ArrayList<Item> items = new ArrayList<Item>(64);
            Cursor cursor = db.rawQuery(sql, new String[0]);

            while (cursor.moveToNext()) {
                items.add(new Item(cursor.getString(0), cursor.getInt(1), Category.valueOf(cursor.getString(2))));
            }

            cursor.close();
            return items;
        } finally {
            db.close();
        }
    }

    public List<String> loadFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor c = db.query("FAVORITES", new String[] {"NAME"}, null, new String[0], null, null, null);
            try {
                ArrayList<String> items = new ArrayList<String>();
                while (c.moveToNext()) {
                    items.add(c.getString(0));
                }

                return items;
            } finally {
                c.close();
            }
        } finally {
            db.close();
        }
    }

    public void addFavorite(String name) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues(1);
            values.put("NAME", name);

            db.insert("FAVORITES", null, values);

            db.setTransactionSuccessful();
        } finally {
            try {
                db.endTransaction();
            } finally {
                db.close();
            }
        }
    }

    public void replaceItems(List<Item> items) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();

            db.delete("ITEMS", null, null);

            ContentValues values = new ContentValues(4);
            for (Item item : items) {
                values.put("NAME", item.getTitle());
                values.put("PRICE", item.getPrice());
                values.put("CATEGORY", item.getCategory().name());

                db.insert("ITEMS", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void replaceFavorites(Collection<Item> favorites) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();

            db.delete("FAVORITES", null, null);
            ContentValues values = new ContentValues(1);
            for (Item item : favorites) {
                values.put("NAME", item.getTitle());

                db.insert("FAVORITES", null, values);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}
