package com.spider;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class URLProcessor {
    private final String HTML_CONTENT_TYPE_PREFIX = "text/html";

    public static record ProcessResult(
            List<URL> urls,
            Set<String> skipped) {
    }

    public Optional<ProcessResult> process(URL url) throws Exception {
        Optional<RawProcessResult> rawURLs = getPageRawLinks(url);
        if (!rawURLs.isPresent()) {
            return Optional.empty();
        }

        List<URL> result = new ArrayList<>();
        for (String rawURL : rawURLs.get().urls) {
            Optional<URL> fullURL = makeUniformURL(url, rawURL);
            if (fullURL.isPresent()) {
                result.add(fullURL.get());
            } else {
                rawURLs.get().skipped.add(rawURL);
            }
        }

        ProcessResult processResults = new ProcessResult(result, rawURLs.get().skipped);
        return Optional.of(processResults);
    }

    public static record RawProcessResult(
            List<String> urls,
            Set<String> skipped) {
    }

    private Optional<RawProcessResult> getPageRawLinks(URL url) throws Exception {
        Document doc = getURLContents(url);
        if (doc == null) {
            return Optional.empty();
        }

        Elements links = doc.select("a[href]");
        List<String> urls = new ArrayList<>();
        for (Element link : links) {
            String href = link.attr("href");
            if (href.isEmpty() || href.isBlank() || href.trim().equals("#") || href.trim().startsWith("#")) {
                continue;
            }
            urls.add(href);
        }

        RawProcessResult rawResult = new RawProcessResult(urls, new HashSet<>());
        return Optional.of(rawResult);
    }

    private Document getURLContents(URL url) throws Exception {
        String contentType = getContentType(url);
        if (!contentType.startsWith(HTML_CONTENT_TYPE_PREFIX)) {
            return null;
        }

        System.out.printf("Download page: %s\n", url);
        Document doc = Jsoup.connect(url.toString()).get();
        return doc;
    }

    /**
     * we will only download and scrape web pages. This method will be used to
     * identify if a url returns a webpage or not.
     */
    private String getContentType(URL url) throws Exception {
        URLConnection conn = url.openConnection();
        String contentType = conn.getContentType();
        return contentType;
    }

    private Optional<URL> makeUniformURL(URL pageURL, String href) {
        String protocol = pageURL.getProtocol();
        String host = pageURL.getHost();

        if (href.startsWith("/")) {
            String url = String.format("%s://%s%s", protocol, host, href);
            try {
                URL currentURL = new URI(url).toURL();
                URI uri = currentURL.toURI();

                // construct URL without query and fragment.
                URI baseURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(),
                        null, null);
                return Optional.of(baseURI.toURL());
            } catch (Exception ex) {
                System.err.println("error: " + ex.getMessage());
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
                System.err.println("error: " + ex.getMessage());
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
