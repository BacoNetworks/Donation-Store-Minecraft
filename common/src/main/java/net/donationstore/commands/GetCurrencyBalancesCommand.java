package net.donationstore.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.donationstore.models.request.CurrencyBalanceRequest;
import net.donationstore.models.response.CurrencyBalanceResponse;
import net.donationstore.models.response.CurrencyCodeResponse;
import net.donationstore.models.response.GatewayResponse;
import net.donationstore.enums.CommandType;
import net.donationstore.enums.HttpMethod;
import net.donationstore.exception.InvalidCommandUseException;

import java.util.ArrayList;


public class GetCurrencyBalancesCommand extends AbstractApiCommand {

    private CurrencyBalanceRequest currencyBalanceRequest;

    @Override
    public String getSupportedCommand() {
        return "balance";
    }

    @Override
    public Command validate(String[] args) {
        if (args.length != 3) {
            getLogs().add(getInvalidCommandMessage());
            getLogs().add(helpInfo());
            throw new InvalidCommandUseException(getLogs());
        }

        getWebstoreHTTPClient().setSecretKey(args[0])
                .setWebstoreAPILocation(args[1]);

        currencyBalanceRequest = new CurrencyBalanceRequest();
        currencyBalanceRequest.setUuid(args[2]);
        return this;
    }

    @Override
    public ArrayList<String> runCommand() throws Exception {

        GatewayResponse gatewayResponse = getWebstoreHTTPClient().sendRequest(
                buildDefaultRequest("currency/balances", HttpMethod.POST, currencyBalanceRequest),
                CurrencyBalanceResponse.class);

        CurrencyBalanceResponse currencyBalanceResponse = (CurrencyBalanceResponse) gatewayResponse.getBody();

        for(String key: currencyBalanceResponse.getBalances().keySet()) {
            addLog(String.format("%s: %s", key, currencyBalanceResponse.getBalances().get(key)));
        }

        return getLogs();
    }

    @Override
    public String helpInfo() {
        return "This command is used to view your Virtual Currency balances.";
    }

    @Override
    public CommandType commandType() {
        return CommandType.PLAYER;
    }

}
