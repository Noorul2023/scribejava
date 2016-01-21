package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

public class LinkedInApi extends DefaultApi10a {

    private static final String AUTHORIZE_URL = "https://api.linkedin.com/uas/oauth/authenticate?oauth_token=%s";
    private static final String REQUEST_TOKEN_URL = "https://api.linkedin.com/uas/oauth/requestToken";

    private static class InstanceHolder {
        private static final LinkedInApi INSTANCE = new LinkedInApi();
    }

    public static LinkedInApi instance() {
        return InstanceHolder.INSTANCE;
    }

    private final String scopesAsString;

    public LinkedInApi() {
        scopesAsString = null;
    }

    public LinkedInApi(final String... scopes) {
        if (scopes == null || scopes.length == 0) {
            scopesAsString = null;
        } else {
            final StringBuilder builder = new StringBuilder();
            for (final String scope : scopes) {
                builder.append('+').append(scope);
            }
            scopesAsString = "?scope=" + builder.substring(1);
        }
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.linkedin.com/uas/oauth/accessToken";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return scopesAsString == null ? REQUEST_TOKEN_URL : REQUEST_TOKEN_URL + scopesAsString;
    }

    @Override
    public String getAuthorizationUrl(final Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}
