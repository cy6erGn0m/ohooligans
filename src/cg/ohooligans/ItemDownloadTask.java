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
import java.util.List;

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
            throw new IllegalStateException(e);
        } catch (SAXException e) {
            throw new IllegalStateException(e);
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
