package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;

public class GoogleApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=%s";

    private GoogleApi() {
    }

    private static class InstanceHolder {
        private static final GoogleApi INSTANCE = new GoogleApi();
    }

    public static GoogleApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.google.com/accounts/OAuthGetAccessToken";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.google.com/accounts/OAuthGetRequestToken";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }

    @Override
    public String getAuthorizationUrl(final Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }
}
