package cg.ohooligans;

import android.os.AsyncTask;
import android.util.Xml;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sergey Mashkov aka cy6erGn0m
 * @since 14.05.12
 */
public class ItemDownloadTask extends AsyncTask<String, Void, HooligansMenu> {

    @Override
    protected HooligansMenu doInBackground(String... urls) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(urls[0]);
        InputStream is = null;

        try {
            HttpResponse response = client.execute(get);
            is = response.getEntity().getContent();

            ItemsHandler handler = new ItemsHandler();
            Xml.parse(is, Xml.Encoding.UTF_8, handler);
            List<Item> items = handler.getItems();
            Set<Item> defaultFavorites = handler.getFavs();

            return new HooligansMenu(items, defaultFavorites);
        } catch (IOException e) {
            Logger.getLogger(ItemDownloadTask.class.getName()).log(Level.SEVERE, "Failed to load items list", e);
            return HooligansMenu.empty();
        } catch (SAXException e) {
            Logger.getLogger(ItemDownloadTask.class.getName()).log(Level.SEVERE, "Failed to load items list", e);
            return HooligansMenu.empty();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
