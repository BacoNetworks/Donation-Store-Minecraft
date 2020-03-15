package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.donationstore.models.request.GatewayRequest;
import net.donationstore.enums.CommandType;
import net.donationstore.enums.HttpMethod;
import net.donationstore.http.WebstoreHTTPClient;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApiCommand<T> extends AbstractCommand {

    @JsonIgnore
    private WebstoreHTTPClient webstoreHTTPClient;

    public AbstractApiCommand() {
        super();
        webstoreHTTPClient = new WebstoreHTTPClient();
    }

    public abstract String getSupportedCommand();

    public abstract Command validate(String[] args);

    public abstract CommandType commandType();

    public abstract ArrayList<String> runCommand() throws Exception;

    public abstract String helpInfo();

    public String getInvalidCommandMessage() {
        return "Invalid usage of command. Help Info: ";
    }

    public WebstoreHTTPClient getWebstoreHTTPClient() {
        return webstoreHTTPClient;
    }

    public void setWebstoreHTTPClient(WebstoreHTTPClient webstoreHTTPClient) {
        this.webstoreHTTPClient = webstoreHTTPClient;
    }
}
