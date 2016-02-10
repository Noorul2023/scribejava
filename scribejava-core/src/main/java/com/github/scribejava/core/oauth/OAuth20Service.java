package com.github.scribejava.core.oauth;

import com.ning.http.client.ProxyServer;
import java.io.IOException;
import java.util.concurrent.Future;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.AccessToken;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthAsyncRequestCallback;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.OAuthRequestAsync;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;

public class OAuth20Service extends OAuthService {

    private static final String VERSION = "2.0";
    private final DefaultApi20 api;

    /**
     * Default constructor
     *
     * @param api OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public OAuth20Service(DefaultApi20 api, OAuthConfig config) {
        super(config);
        this.api = api;
    }

    public AccessToken getAccessToken(final Verifier verifier) {
        final Response response = createAccessTokenRequest(verifier,
                new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this)).send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

     /**
     * Start the request to retrieve the access token. The optionally provided callback will be called with the Token
     * when it is available.
     *
     * @param verifier verifier code
     * @param callback optional callback
     * @return Future
     */
    public Future<AccessToken> getAccessTokenAsync(Verifier verifier, OAuthAsyncRequestCallback<AccessToken> callback, ProxyServer proxyServer) {
        final OAuthRequestAsync request = createAccessTokenRequest(verifier,
                new OAuthRequestAsync(api.getAccessTokenVerb(), api.getAccessTokenEndpoint(), this));
        return request.sendAsync(callback, new OAuthRequestAsync.ResponseConverter<AccessToken>() {
            @Override
            public AccessToken convert(final com.ning.http.client.Response response) throws IOException {
                return getApi().getAccessTokenExtractor().extract(OAuthRequestAsync.RESPONSE_CONVERTER.convert(response).getBody());
            }
        }, proxyServer);
    }

    protected <T extends AbstractRequest> T createAccessTokenRequest(Verifier verifier, T request) {
        final OAuthConfig config = getConfig();
        request.addParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addParameter(OAuthConstants.CODE, verifier.getValue());
        request.addParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) {
            request.addParameter(OAuthConstants.SCOPE, config.getScope());
        }
        if (config.hasGrantType()) {
            request.addParameter(OAuthConstants.GRANT_TYPE, config.getGrantType());
        }
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void signRequest(final Token token, final AbstractRequest request) {
        if (token instanceof OAuth2AccessToken) {
            signRequest((AccessToken) token, request);
        } else {
            throw new IllegalArgumentException("The access token must be an OAuth2AccessToken.");
        }

    }
    
    @Override
    public void signRequest(final AccessToken accessToken, final AbstractRequest request) {
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
    }

    /**
     * Returns the URL where you should redirect your users to authenticate your application.
     *
     * @return the URL where you should redirect your users
     */
    public String getAuthorizationUrl() {
        return api.getAuthorizationUrl(getConfig());
    }

    public DefaultApi20 getApi() {
        return api;
    }
}
