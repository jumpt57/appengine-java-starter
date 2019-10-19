package com.github.jumpt57.configuration.clientIds;

import com.github.jumpt57.configuration.environment.ConfigurationFactory;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.server.spi.Client;
import com.google.api.server.spi.Strings;
import com.google.api.server.spi.auth.EndpointsAuthenticator;
import com.google.api.server.spi.auth.GoogleAuth;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.response.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ClientIdsAuthenticator extends EndpointsAuthenticator {

    private static final String TOKEN_INFO_ENDPOINT = "https://www.googleapis.com/oauth2/v2/tokeninfo?access_token=";

    @Override
    public User authenticate(HttpServletRequest request) throws ServiceUnavailableException {
        User user = super.authenticate(request);

        if (Objects.nonNull(user)) {
            String authToken = GoogleAuth.getAuthToken(request);
            GoogleAuth.TokenInfo tokenInfo = getTokenInfoRemote(authToken);

            if (Objects.nonNull(tokenInfo) && ConfigurationFactory.get()
                    .getClientIds().stream()
                    .anyMatch(s -> s.equals(tokenInfo.clientId))) {
                return user;
            }
        }

        return null;
    }

    private GoogleAuth.TokenInfo getTokenInfoRemote(String token) {
        try {
            HttpRequest request = Client.getInstance().getJsonHttpRequestFactory()
                    .buildGetRequest(new GenericUrl(TOKEN_INFO_ENDPOINT + token));
            return parseTokenInfo(request);
        } catch (IOException e) {
            log.warn("Failed to retrieve tokeninfo", e);
            return null;
        }
    }

    private GoogleAuth.TokenInfo parseTokenInfo(HttpRequest request) throws IOException {
        GoogleAuth.TokenInfo info = request.execute().parseAs(GoogleAuth.TokenInfo.class);
        if (info == null || Strings.isEmptyOrWhitespace(info.email)) {
            log.warn("Access token does not contain email scope");
            return null;
        }
        return info;
    }

}
