package com.aryl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        DataFeeder dataFeeder = new DataFeeder();
        Document doc = null;
        Elements ele = null;

        int rootID = 0;
        String rootURL = "https://www.w3schools.com";
        String title = "W3School";

        dataFeeder.push(rootID, rootURL, title, -1);
        dataFeeder.start++;
//        System.out.println("URL: " + rootURL);

        int tempID = rootID+1;

        int counter = 0;
        while(!dataFeeder.isEmpty() && counter<357) {

            try {
                System.out.println(dataFeeder.start);
                System.out.println(dataFeeder.end);
                String urltemp = dataFeeder.pop();
                System.out.println(urltemp);
                System.out.println(dataFeeder.start);
                System.out.println(dataFeeder.end);
                System.out.println(tempID);
                doc = Jsoup.connect(urltemp).get();
                title = doc.title();
                dataFeeder.update(title, 1);

                ele = doc.getElementsByTag("a");

                for (Element element : ele) {
                    dataFeeder.push(tempID, element.absUrl("href"), element.text(), -1);
                    tempID++;
                }
                counter++;
                System.out.println(dataFeeder.start);
                System.out.println(dataFeeder.end);
                System.out.println(tempID);
                System.out.println(counter);

            } catch (Exception e) {
                if (!dataFeeder.isEmpty()) {
                    dataFeeder.start++;
                } else {
                    System.out.println("listempty");
                }
                System.out.println("Handling");
            }


        }


    }
}
