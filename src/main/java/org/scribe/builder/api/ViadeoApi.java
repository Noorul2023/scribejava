package org.scribe.builder.api;

import org.scribe.builder.AuthUrlBuilder;
import org.scribe.builder.authUrl.DefaultAuthUrlBuilder;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;

import org.scribe.processors.extractors.JsonTokenExtractor;
import org.scribe.processors.extractors.TokenExtractor;

public class ViadeoApi extends DefaultApi20 {
  private static final String AUTHORIZE_URL = "https://secure.viadeo.com/oauth-provider/authorize2";

  @Override
  public TokenExtractor getAccessTokenExtractor() {
    return new JsonTokenExtractor();
  }
  
  @Override
  public String getAccessTokenEndpoint() {
    return "https://secure.viadeo.com/oauth-provider/access_token2?grant_type=authorization_code";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config, String state) {
      AuthUrlBuilder builder = new DefaultAuthUrlBuilder();

      builder.setEndpoint(AUTHORIZE_URL)
              .setClientId(config.getApiKey())
              .setRedirectUrl(config.getCallback())
              .setScope(config.getScope())
              .setState(state)
              .setResponseType(OAuthConstants.CODE);
      return builder.build();
  }
}
