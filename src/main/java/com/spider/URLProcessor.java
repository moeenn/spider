package com.spider;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class URLProcessor {
    public List<URL> process(URL url) throws Exception {
        Document doc = getURLContents(url.toString());
        Elements links = doc.select("a[href]");

        List<URL> result = new ArrayList<>();
        for (Element link : links) {
            String href = link.attr("href");
            Optional<URL> fullURL = makeUniformURL(url, href);
            if (fullURL.isEmpty()) {
                System.out.printf("skipping: %s\n", href);
                continue;
            }

            System.out.println(fullURL.get());
            // TODO: complete imlementation.
        }

        return result;
    }

    private Document getURLContents(String url) throws Exception {
        System.out.printf("Download page: %s\n", url);
        Document doc = Jsoup.connect(url).get();
        return doc;
    }

    private Optional<URL> makeUniformURL(URL pageURL, String href) {
        if (href.isEmpty() || href.startsWith("#")) {
            return Optional.empty();
        }

        String protocol = pageURL.getProtocol();
        String host = pageURL.getHost();

        if (href.startsWith("/")) {
            String url = pageURL.getProtocol() + "://" + host + href;
            try {
                return Optional.of(new URI(url).toURL());
            } catch (Exception ex) {
                return Optional.empty();
            }
        }

        if (href.startsWith("http")) {
            try {
                URL url = new URI(href).toURL();
                if (url.getProtocol().equals(protocol) && url.getHost().equals(host)) {
                    return Optional.of(url);
                }
            } catch (Exception ex) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
