package cg.ohooligans;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
* @author Sergey Mashkov
*/
class ItemsHandler extends DefaultHandler {

    private int price;
    private Category category;
    private StringBuilder sb;
    private final ArrayList<Item> items;

    ItemsHandler(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("item")) {
            sb = new StringBuilder(64);
            price = Integer.parseInt(attributes.getValue("price"));
            category = Category.valueOf(attributes.getValue("cat"));
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
