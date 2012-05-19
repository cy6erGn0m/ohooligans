package cg.ohooligans;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Boolean.parseBoolean;

/**
* @author Sergey Mashkov
*/
class ItemsHandler extends DefaultHandler {

    private int price;
    private Category category;
    private StringBuilder sb;
    private boolean favorite;
    private final List<Item> items = new ArrayList<Item>(384);
    private final Set<Item> favs = new HashSet<Item>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("item")) {
            sb = new StringBuilder(64);
            price = Integer.parseInt(attributes.getValue("price"));
            category = Category.valueOf(attributes.getValue("cat"));

            String fav = attributes.getValue("fav");
            favorite = fav != null && parseBoolean(fav);
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
            Item item = new Item(sb.toString(), price, category);
            items.add(item);
            if (favorite) {
                favs.add(item);
            }

            sb = null;
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public Set<Item> getFavs() {
        return favs;
    }
}
