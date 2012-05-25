package cg.ohooligans;

import android.os.AsyncTask;
import android.util.Xml;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

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
            GZIPInputStream gzipInputStream = new GZIPInputStream(new BufferedInputStream(is));

            ItemsHandler handler = new ItemsHandler();
            Xml.parse(gzipInputStream, Xml.Encoding.UTF_8, handler);
            List<Item> items = handler.getItems();
            Set<String> defaultFavorites = handler.getFavs();

            return HooligansMenu.create(items, defaultFavorites);
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
