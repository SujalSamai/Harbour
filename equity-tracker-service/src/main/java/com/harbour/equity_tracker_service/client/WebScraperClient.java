package com.harbour.equity_tracker_service.client;

import com.harbour.equity_tracker_service.dto.EquityDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WebScraperClient
{
   private final WebClient webClient;

   public EquityDetailsResponse getStockData(String stockSymbol)
   {
      String url = "https://www.screener.in/company/" + stockSymbol +"/consolidated/";
      return scrapDataFromScreener(stockSymbol, url);
   }

   private EquityDetailsResponse scrapDataFromScreener(String symbol, String url)
   {
      EquityDetailsResponse indianStockResponse = new EquityDetailsResponse();
      try {
         // Fetch the HTML document
         Document doc = Jsoup.connect(url)
                  .userAgent("Mozilla/5.0")
                  .timeout(10_000)
                  .get();

         indianStockResponse.setSymbol(symbol);

         // Example: Get company name
         String companyName = Objects.requireNonNull(doc.selectFirst("h1")).text();
         indianStockResponse.setCompanyName(companyName);

         Element priceContainer = doc.selectFirst(".font-size-18.strong.line-height-14 .flex.flex-align-center");

         if (priceContainer != null) {
            // Current price
            String price = Objects.requireNonNull(priceContainer.select("span").first())
                     .text().replace("₹", "").trim();

            // Change percent (may have arrow icon inside)
            Element changeElem = priceContainer.select("span.font-size-12").first();
            String changePercent = "";
            if (changeElem != null) {
               changePercent = changeElem.text().replaceAll("[^\\d\\-.]", "").trim(); // removes everything except numbers, dot, and minus
            }
            indianStockResponse.setPrice(price);
            indianStockResponse.setChangeInPercentage(changePercent);
         }


         Element aboutElem = doc.selectFirst(".flex.flex-column .sub.show-more-box.about");
         String aboutText = aboutElem != null ? aboutElem.text().trim() : "N/A";

         // Extract "Business Overview" text from Key Points
         Element keyPointsElem = doc.selectFirst(".flex.flex-column .sub.commentary.always-show-more-box");
         String businessOverview = "N/A";
         if (keyPointsElem != null) {
            Element strongTag = keyPointsElem.selectFirst("strong");
            if (strongTag != null) {
               // Get the text after <strong> tag
               businessOverview = keyPointsElem.text().replace(strongTag.text(), "").trim();
            }
         }

         indianStockResponse.setDescription(aboutText);
         indianStockResponse.setBusinessOverview(businessOverview);

         Map<String, String> topRatios = new HashMap<>();

         // Select all <li> elements inside #top-ratios
         Elements ratioElements = doc.select("#top-ratios li");

         for (Element li : ratioElements) {
            // Get the name of the ratio
            String name = Objects.requireNonNull(li.selectFirst("span.name")).text().trim();

            // Get all numbers inside the value
            Elements numbers = li.select("span.value span.number");

            String value;
            if (numbers.size() == 1) {
               value = numbers.getFirst().text().trim();
            } else if (numbers.size() > 1) {
               // For High / Low, combine both numbers with /
               value = numbers.get(0).text().trim() + " / " + numbers.get(1).text().trim();
            } else {
               value = Objects.requireNonNull(li.selectFirst("span.value")).text().trim();
            }

            topRatios.put(name, value);
         }

         // Print the extracted ratios
         topRatios.forEach((k, v) -> {
            if (k.startsWith("Stock")) indianStockResponse.setStockPERatio(v);
            if (k.startsWith("Dividend")) indianStockResponse.setDividendYield(v);
            if (k.startsWith("Book")) indianStockResponse.setBookValue(v);
            if (k.startsWith("ROCE")) indianStockResponse.setRoce(v);
            if (k.startsWith("ROE")) indianStockResponse.setRoe(v);
            if (k.startsWith("Market")) indianStockResponse.setMarketCap(v);
            if (k.startsWith("Face")) indianStockResponse.setFaceValue(v);
         });

         Element highLowLi = doc.select("li.flex.flex-space-between").stream()
                  .filter(li -> li.selectFirst(".name") != null &&
                           Objects.requireNonNull(li.selectFirst(".name")).text().trim().equals("High / Low"))
                  .findFirst()
                  .orElse(null);

         if (highLowLi != null) {
            Elements numbers = highLowLi.select(".value .number");
            String high = !numbers.isEmpty() ? numbers.get(0).text().replaceAll(",", "").trim() : "N/A";
            String low = numbers.size() > 1 ? numbers.get(1).text().replaceAll(",", "").trim() : "N/A";

            indianStockResponse.setHighValue(high);
            indianStockResponse.setLowValue(low);
         }

      } catch (IOException e) {
         System.err.println("Error fetching or parsing the page: " + e.getMessage());
      }

      return indianStockResponse;
   }
}
