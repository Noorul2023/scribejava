package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.Preconditions;
import org.scribe.utils.URLUtils;

/**
 * @author Boris G. Tsirkin <mail@dotbg.name>
 * @since 20.4.2011
 */

public class VkontakteApi extends DefaultApi20 {
  private static final String AUTHORIZE_URL =
    "https://api.vkontakte.ru/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code";

  @Override
  public String getAccessTokenEndpoint() {
    return "https://api.vkontakte.ru/oauth/access_token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config) {
    Preconditions.checkValidUrl(
      config.getCallback(),
      "Valid url is required for a callback. Vkontakte does not support OOB"
    );
    return String.format(AUTHORIZE_URL, config.getApiKey(), URLUtils.formURLEncode(config.getCallback()));
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor() {
    return new JsonTokenExtractor();
  }
}
