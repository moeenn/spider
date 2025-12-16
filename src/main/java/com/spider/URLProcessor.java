package com.spider;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.time.Instant;
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
            Duration elapsed,
            List<URL> urls,
            Set<SkippedURL> skipped) {
    }

    public Optional<ProcessResult> process(URL url) throws Exception {
        Optional<RawProcessResult> rawURLs = getPageRawLinks(url);
        if (!rawURLs.isPresent()) {
            return Optional.empty();
        }

        List<URL> result = new ArrayList<>();
        for (String rawURL : rawURLs.get().urls) {
            UniformURLResult fullURL = makeUniformURL(url, rawURL);
            if (fullURL.skipReason.isEmpty()) {
                result.add(fullURL.url);
            } else {
                var skipped = assessSkippedURL(rawURL, fullURL.skipReason);
                rawURLs.get().addSkipped(skipped);
            }
        }

        ProcessResult processResults = new ProcessResult(rawURLs.get().elapsed, result, rawURLs.get().skipped);
        return Optional.of(processResults);
    }

    private SkippedURL assessSkippedURL(String url, Optional<String> skipReason) {
        if (url.startsWith("mailto:")) {
            return new SkippedURL(url, "Email link");
        }

        if (url.startsWith("tel:")) {
            return new SkippedURL(url, "Phone number link");
        }

        if (skipReason.isPresent()) {
            return new SkippedURL(url, skipReason.get());
        }

        return new SkippedURL(url, "Unknown reason for skipping");
    }

    public static record SkippedURL(String url, String reason) {
    }

    public static record RawProcessResult(
            Duration elapsed,
            List<String> urls,
            Set<SkippedURL> skipped) {

        public void addSkipped(SkippedURL url) {
            skipped.add(url);
        }
    }

    private Optional<RawProcessResult> getPageRawLinks(URL url) throws Exception {
        GetDocumentResult doc = getDocument(url);
        if (doc == null) {
            return Optional.empty();
        }

        Elements links = doc.doc.select("a[href]");
        List<String> urls = new ArrayList<>();
        for (Element link : links) {
            String href = link.attr("href");
            if (href.isEmpty() || href.isBlank() || href.trim().equals("#") || href.trim().startsWith("#")) {
                continue;
            }
            urls.add(href.strip());
        }

        RawProcessResult rawResult = new RawProcessResult(doc.elapsed, urls, new HashSet<>());
        return Optional.of(rawResult);
    }

    private static record GetDocumentResult(
            Document doc,
            Duration elapsed) {
    }

    private GetDocumentResult getDocument(URL url) throws Exception {
        String contentType = getContentType(url);
        if (contentType == null) {
            return null;
        }

        if (!contentType.startsWith(HTML_CONTENT_TYPE_PREFIX)) {
            return null;
        }

        Log.printf("download page: %s", url);
        var start = Instant.now();
        Document doc = Jsoup.connect(url.toString()).get();
        var end = Instant.now();
        Duration elapsed = Duration.between(start, end);

        return new GetDocumentResult(doc, elapsed);
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

    private static record UniformURLResult(URL url, Optional<String> skipReason) {
    }

    private UniformURLResult makeUniformURL(URL pageURL, String href) {
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

                return new UniformURLResult(baseURI.toURL(), Optional.empty());
            } catch (Exception ex) {
                Log.println("error: " + ex.getMessage());
                return new UniformURLResult(pageURL, Optional.of(ex.getMessage()));
            }
        }

        if (href.startsWith("http")) {
            try {
                URL url = new URI(href).toURL();
                if (url.getProtocol().equals(protocol) && url.getHost().equals(host)) {
                    return new UniformURLResult(url, Optional.empty());
                }

                if (!url.getHost().equals(host)) {
                    return new UniformURLResult(url, Optional.of("External link"));
                }

                if (!url.getProtocol().equals(protocol)) {
                    return new UniformURLResult(url, Optional.of("Protocol mismatch"));
                }
            } catch (Exception ex) {
                Log.println("error: " + ex.getMessage());
                return new UniformURLResult(null, Optional.of(ex.getMessage()));
            }
        }

        return new UniformURLResult(null, Optional.of("Malfomed URL"));
    }
}
