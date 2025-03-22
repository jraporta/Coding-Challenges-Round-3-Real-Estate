package com.round3.realestate.service;

import com.round3.realestate.exception.CustomInternalServerException;
import com.round3.realestate.payload.RealStateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class ScrapeServiceJSoup implements ScrapeService {

    @Override
    public RealStateData scrape(String url) {

        RealStateData data = new RealStateData();

        Document doc = Jsoup.parse(callExternalWebScrappingAPI(url));

        String fullTitle = getFullTitle(doc);

        data.setUrl(url);
        data.setFullTitle(fullTitle);
        data.setType(fullTitle.isEmpty() ? "" : fullTitle.split(" ")[0]);
        data.setLocation(getLocation(doc));
        data.setPrice(getPrice(doc));
        data.setSize(getSize(doc));
        data.setRooms(getNumberOfRooms(doc));

        return data;
    }

    private String getFullTitle(Document doc) {
        try {
            Element titleElement = doc.selectFirst(".main-info__title-main");
            if (titleElement != null) {
                String title = titleElement.text().trim();
                log.info("Scrapped title: '{}'", title);
                return title;
            }
        } catch (Exception e) {
            log.warn("Title element not found.");
        }
        return "";
    }

    private String getLocation(Document doc) {
        try {
            Element locationElement = doc.selectFirst(".main-info__title-minor");
            if (locationElement != null) {
                String location = locationElement.text().trim();
                log.info("Scrapped location: '{}'", location);
                return location;
            }
        } catch (Exception e) {
            log.warn("Location element not found.");
        }
        return "";
    }

    private String getPrice(Document doc) {
        try {
            Element locationElement = doc.selectFirst(".info-data-price");
            if (locationElement != null) {
                String price = locationElement.text().trim().replaceAll("[^0-9.]", "");
                log.info("Scrapped price: '{}'", price);
                return price;
            }
        } catch (Exception e) {
            log.warn("Price element not found.");
        }
        return "";
    }

    private String getSize(Document doc) {
        try {
            Element sizeElement = doc.selectFirst(".info-features");
            if (sizeElement != null) {
                String fullText = sizeElement.text().trim();
                String[] split = fullText.split(" ");
                String size = split[0] + " " + split[1];
                log.info("Scrapped size: '{}'", size);
                return size;
            }
        } catch (Exception e) {
            log.warn("Size element not found.");
        }
        return "";
    }

    private String getNumberOfRooms(Document doc) {
        try {
            Element roomsElement = doc.selectFirst(".info-features");
            if (roomsElement != null) {
                String fullText = roomsElement.text().trim();
                String[] split = fullText.split(" ");
                String rooms = split[2] + " " + split[3];
                log.info("Scrapped number of rooms: '{}'", rooms);
                return rooms;
            }
        } catch (Exception e) {
            log.warn("Room element not found.");
        }
        return "";
    }

    public String callExternalWebScrappingAPI(String url) {
        String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
        String apiUrl = "https://api.zenrows.com/v1/?apikey=8c8ea489b563ab3ad546022db2cfa827d1111e17&url="
                + encodedUrl
                + "&premium_proxy=true&proxy_country=es";
        log.info("sending request to: {}", apiUrl);
        try {
            return Request.get(apiUrl)
                    .execute().returnContent().asString();
        } catch (IOException e) {
            throw new CustomInternalServerException(e.getMessage());
        }
    }

}
