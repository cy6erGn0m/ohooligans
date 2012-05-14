package cg.ohooligans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Mashkov aka cy6erGn0m
 * @since 14.05.12
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "OHDatabase", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE ITEMS (NAME TEXT, PRICE INTEGER, CATEGORY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.beginTransaction();

            for (; i < i1; ++i) {
                switch (i) {
                    case 1:
                        db.delete("ITEMS", null, null);
                        break;
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Item> loadItems(Category category) {
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

    public void replaceItems(Category category, List<Item> items) {
        String deleteSql = "DELETE FROM ITEMS WHERE CATEGORY = '" + category.name() + "'";
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();

            db.rawQuery(deleteSql, null).close();

            ContentValues values = new ContentValues();
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
}
