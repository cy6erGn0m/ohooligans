package cg.ohooligans;

import android.content.res.Resources;
import android.util.Xml;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
* @author Sergey Mashkov
*/
class ItemsHandler extends DefaultHandler {

    private int price;
    private StringBuilder sb;
    private final ArrayList<Item> items;
    private final Category category;

    public ItemsHandler(ArrayList<Item> items, Category category) {
        this.items = items;
        this.category = category;
    }

    public void handle(Resources res, int id) {
        InputStream is = null;
        try {
            is = res.openRawResource(id);
            Xml.parse(is, Xml.Encoding.UTF_8, this);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (SAXException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ignore) {
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("item")) {
            sb = new StringBuilder(64);
            price = Integer.parseInt(attributes.getValue("price"));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (sb != null) {
            sb.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("item") && sb != null) {
            items.add(new Item(sb.toString(), price, category));
            sb = null;
        }
    }
}
