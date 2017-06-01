import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Created by estaine on 31.05.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String apiURL = "http://www.nbrb.by/API/RefinancingRate";
        String websiteURL = "http://www.nbrb.by/statistics/MonetaryPolicyInstruments/InterestRates";

        List<BankRate> bankRates = getBankRatesFromAPI(apiURL);
        printBankRates(bankRates);

        String overnightRate = getOvernightRateFromHTML(websiteURL);
        System.out.println("Overnight rate: " + overnightRate);
    }

    private static List<BankRate> getBankRatesFromAPI(String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

// add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result);

        Gson gson = new Gson();
        return gson.fromJson(result.toString(), new TypeToken<List<BankRate>>(){}.getType());
    }

    private static String getOvernightRateFromHTML(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Element element = doc.select("#BodyHolder_tblMain > tbody > tr").get(2).select("td").get(2);

        return element.html();
    }

    private static void printBankRates(List<BankRate> bankRates) {
        for(BankRate bankRate : bankRates) {
            System.out.println(bankRate.getDate() + ": " + bankRate.getValue());
        }
    }
}
