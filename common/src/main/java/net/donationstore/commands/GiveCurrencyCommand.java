package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.donationstore.exception.InvalidCommandUseException;
import net.donationstore.exception.WebstoreAPIException;
import net.donationstore.util.FormUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GiveCurrencyCommand implements Command {

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("username")
    private String username;

    @JsonProperty("currency-code")
    private String currencyCode;

    @JsonIgnore
    private String secretKey;

    @JsonIgnore
    private String webstoreAPILocation;

    @JsonIgnore
    private HttpClient httpClient;

    @JsonIgnore
    private ArrayList<String> logs;

    public GiveCurrencyCommand() {
        logs = new ArrayList<>();
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    @Override
    public ArrayList<String> runCommand(String[] args) throws Exception {
        if (args.length != 5) {
            logs.add("Invalid usage of command. Help Info: ");
            logs.add(this.helpInfo());
            throw new InvalidCommandUseException(logs);
        } else {
            String webstoreAPILocation = args[0];
            String secretKey = args[1];
            String username = args[2];
            String currencyCode = args[3];
            String amount = args[4];
            Map<String, String> data = new HashMap<>();
            data.put("username", username);
            data.put("currency_code", currencyCode);
            data.put("amount", amount);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("%s/get-currency-balances", webstoreAPILocation)))
                    .header("secret-key", secretKey)
                    .POST(FormUtil.ofFormData(data))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject jsonResponse = new JsonParser().parse(response.body()).getAsJsonObject();

                    logs.add(jsonResponse.get("message").getAsString());

                } else {
                    logs.add("Invalid webstore API response:");
                    logs.add(response.body());
                    throw new WebstoreAPIException(logs);
                }
            } catch (IOException exception) {
                throw new IOException("IOException when contacting the webstore API when running the give currency command.");
            } catch (InterruptedException exception) {
                throw new InterruptedException("InterruptedException when contacting the webstore API when running the give currency command.");
            }
        }

        return logs;
    }

    @Override
    public String helpInfo() {
        return "This command is used to award a player with in-game virtual currency.";
    }

    @Override
    public CommandType commandType() {
        return CommandType.PLAYER;
    }

    public String getAmount() {
        return amount;
    }

    public GiveCurrencyCommand setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public GiveCurrencyCommand setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public GiveCurrencyCommand setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public GiveCurrencyCommand setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public String getWebstoreAPILocation() {
        return webstoreAPILocation;
    }

    public GiveCurrencyCommand setWebstoreAPILocation(String webstoreAPILocation) {
        this.webstoreAPILocation = webstoreAPILocation;
        return this;
    }
}