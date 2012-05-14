package cg.ohooligans;

import android.os.AsyncTask;
import android.util.Xml;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sergey Mashkov aka cy6erGn0m
 * @since 14.05.12
 */
public class ItemDownloadTask extends AsyncTask<String, Void, List<Item>> {

    @Override
    protected List<Item> doInBackground(String... urls) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(urls[0]);
        InputStream is = null;

        try {
            HttpResponse response = client.execute(get);
            is = response.getEntity().getContent();

            ArrayList<Item> items = new ArrayList<Item>(64);
            Xml.parse(is, Xml.Encoding.UTF_8, new ItemsHandler(items));

            return items;
        } catch (IOException e) {
            Logger.getLogger(ItemDownloadTask.class.getName()).log(Level.SEVERE, "Failed to load items list", e);
            return Collections.emptyList();
        } catch (SAXException e) {
            Logger.getLogger(ItemDownloadTask.class.getName()).log(Level.SEVERE, "Failed to load items list", e);
            return Collections.emptyList();
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
