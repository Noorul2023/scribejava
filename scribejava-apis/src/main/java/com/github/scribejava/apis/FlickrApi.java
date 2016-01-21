package com.github.scribejava.apis;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.Token;

/**
 * OAuth API for Flickr.
 *
 * @author Darren Greaves
 * @see <a href="http://www.flickr.com/services/api/">Flickr API</a>
 */
public class FlickrApi extends DefaultApi10a {

    private FlickrApi() {
    }

    private static class InstanceHolder {
        private static final FlickrApi INSTANCE = new FlickrApi();
    }

    public static FlickrApi instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAccessTokenEndpoint() {
        return "https://www.flickr.com/services/oauth/access_token";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthorizationUrl(final Token requestToken) {
        return "https://www.flickr.com/services/oauth/authorize?oauth_token=" + requestToken.getToken();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestTokenEndpoint() {
        return "https://www.flickr.com/services/oauth/request_token";
    }
}
