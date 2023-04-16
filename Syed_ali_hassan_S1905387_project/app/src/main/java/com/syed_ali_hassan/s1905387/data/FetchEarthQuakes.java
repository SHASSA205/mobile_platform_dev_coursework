//Syed_Ali_Hassan_S1905387
package com.syed_ali_hassan.s1905387.data;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class FetchEarthQuakes implements Callable<ArrayList<EarthQuakeModel>> {
    private final String urlString;
    private ArrayList<EarthQuakeModel> arrayList = new ArrayList<>();
    private Boolean itemTagStart = false;

    public FetchEarthQuakes(String url) {
        this.urlString = url;
    }

    @Override
    public ArrayList<EarthQuakeModel> call() throws IOException, XmlPullParserException {
        URLConnection yc;
        BufferedReader in;
        String inputLine;
        URL url = new URL(urlString);
        yc = url.openConnection();
        in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        StringBuilder result = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        EarthQuakeModel earthQuakeModel = null;

        xpp.setInput(new StringReader(result.toString()));
        int eventType = xpp.getEventType();
        String tag = "", text = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {
            tag = xpp.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tag.equals("item")) {
                        itemTagStart = true;
                        earthQuakeModel = new EarthQuakeModel();
                    }
                    break;
                case XmlPullParser.TEXT:
                    text = xpp.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (itemTagStart) {
                        switch (tag) {
                            case "title":
                                earthQuakeModel.setTitle(text);
                                break;
                            case "description":
                                earthQuakeModel.setDescription(text);
                                break;
                            case "location":
                                earthQuakeModel.setLink(text);
                                break;
                            case "pubDate":
                                earthQuakeModel.setDate(text);
                                break;
                            case "lat":
                                earthQuakeModel.setLat(text);
                                break;
                            case "long":
                                earthQuakeModel.setLng(text);
                                break;
                            case "item":
                                EarthQuakeModel earthQuakeModel1 = new EarthQuakeModel();
                                earthQuakeModel1.setTitle(earthQuakeModel.getTitle());
                                earthQuakeModel1.setDescription(earthQuakeModel.getDescription());
                                earthQuakeModel1.setLink(earthQuakeModel.getLink());
                                earthQuakeModel1.setDate(earthQuakeModel.getDate());
                                earthQuakeModel1.setLng(earthQuakeModel.getLng());
                                earthQuakeModel1.setLat(earthQuakeModel.getLat());
                                arrayList.add(earthQuakeModel1);
                                itemTagStart = false;
                                break;
                        }
                    }
                    break;
            }
            eventType = xpp.next();
        }

        Log.v("size", String.valueOf(arrayList.size()));
        return arrayList;
    }
}