package com.github.scribejava.apis.examples;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.RenrenApi;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;

public abstract class RenrenExample {

    private static final String NETWORK_NAME = "Renren";
    private static final String PROTECTED_RESOURCE_URL = "http://api.renren.com/restserver.do";
    private static final Token EMPTY_TOKEN = null;

    public static void main(final String... args) {
        // Replace these with your own api key and secret
        final String apiKey = "your api key";
        final String apiSecret = "your api secret";
        final OAuthService service = new ServiceBuilder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope("status_update publish_feed")
                .callback("http://your.doman.com/oauth/renren")
                .build(RenrenApi.instance());
        final Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL, service);
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("method", "users.getInfo");
        parameters.put("format", "json");
        parameters.put("v", "1.0");

        final List<String> sigString = new ArrayList<>(parameters.size() + 1);
        for (final Map.Entry<String, String> entry : parameters.entrySet()) {
            request.addQuerystringParameter(entry.getKey(), entry.getValue());
            sigString.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        sigString.add(String.format("%s=%s", OAuthConstants.ACCESS_TOKEN, accessToken.getToken()));
        Collections.sort(sigString);
        final StringBuilder b = new StringBuilder();
        for (final String param : sigString) {
            b.append(param);
        }
        b.append(apiSecret);
        System.out.println("Sig string: " + b.toString());
        request.addQuerystringParameter("sig", md5(b.toString()));
        service.signRequest(accessToken, request);
        final Response response = request.send();
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");

    }

    public static String md5(final String orgString) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] array = md.digest(orgString.getBytes(Charset.forName("UTF-8")));
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
