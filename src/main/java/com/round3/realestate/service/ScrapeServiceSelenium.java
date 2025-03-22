package com.round3.realestate.service;

import com.round3.realestate.exception.CustomInternalServerException;
import com.round3.realestate.payload.RealStateData;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.util.List;

@Service
@Slf4j
public class ScrapeServiceSelenium implements ScrapeService {

    @Override
    public RealStateData scrape(String url) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        URL seleniuRemoteUrl = null;
        try {
            seleniuRemoteUrl = new URI(System.getenv("SELENIUM_REMOTE_URL")).toURL();
        }catch (Exception exception) {
            throw new CustomInternalServerException("Unable to construct the remote selenium url");
        }

        WebDriver driver = new RemoteWebDriver(seleniuRemoteUrl, options);

        driver.get(url);

        RealStateData data = new RealStateData();

        String fullTitle = getFullTitle(driver);

        data.setUrl(url);
        data.setFullTitle(fullTitle);
        data.setType(fullTitle.isEmpty() ? "" : fullTitle.split(" ")[0]);
        data.setLocation(getLocation(driver));
        data.setPrice(getPrice(driver));
        data.setSize(getSize(driver));
        data.setRooms(getNumberOfRooms(driver));

        driver.quit();

        return data;
    }

    private String getFullTitle(WebDriver driver) {
        try {
            String title = driver.findElement(By.cssSelector(".main-info__title-main")).getText().trim();
            log.info("Scrapped title: '{}'", title);
            return title;
        } catch (Exception e) {
            log.warn("Title element not found.");
            return "";
        }
    }

    private String getLocation(WebDriver driver) {
        try {
            String location = driver.findElement(By.cssSelector(".main-info__title-minor")).getText().trim();
            log.info("Scrapped location: '{}'", location);
            return location;
        } catch (Exception e) {
            log.warn("Location element not found.");
            return "";
        }
    }

    private String getPrice(WebDriver driver) {
        try {
            String price = driver.findElement(By.cssSelector(".info-data-price")).getText().trim();
            price = price.replaceAll("[^0-9.]", "");
            log.info("Scrapped price: '{}'", price);
            return price;
        } catch (Exception e) {
            log.warn("Price element not found.");
            return "";
        }
    }

    private String getSize(WebDriver driver) {
        try {
            String size = driver.findElement(By.cssSelector(".info-features span")).getText().trim();
            log.info("Scrapped size: '{}'", size);
            return size;
        } catch (Exception e) {
            log.warn("Size element not found.");
            return "";
        }
    }

    private String getNumberOfRooms(WebDriver driver) {
        try {
            List<WebElement> spans = driver.findElements(By.cssSelector(".info-features span"));

            for (WebElement span : spans) {
                String text = span.getText().trim();
                if (text.contains("hab.")) {
                    log.info("scrapped number of rooms: '{}'", text);
                    return text;
                }
            }
        } catch (Exception e) {
            log.warn("Number of rooms element not found...");
        }
        log.info("Number of rooms not found!");
        return "";
    }

}
