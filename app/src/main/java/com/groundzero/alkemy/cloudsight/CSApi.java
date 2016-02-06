/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.groundzero.alkemy.cloudsight;


import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.groundzero.alkemy.utils.AHandler;


import java.io.IOException;


public final class CSApi {
  public static final String BASE_URL = "https://api.cloudsightapi.com";
  private static final String IMAGE_REQUESTS_PATH = "/image_requests";
  private static final String IMAGE_RESPONSES_PATH = "/image_responses";
  public static final String CONTENT_TYPE = "multipart/form-data";
  private static final String AUTHORIZATION_FORMAT = "CloudSight %s";
  private static final String URL_CONCATENATION_FORMAT = "%s/%s";
    private static final String TAG = "CSAPI";

    private final String mAuthorizationKey;
  private final HttpRequestFactory mHttpRequestFactory;
  private final GenericUrl mImagePostUrl;
  private final GenericUrl mImageGetUrl;


  public CSApi(
    final HttpTransport transport,
    final JsonFactory jsonFactory,
    final String authorizationKey,
    final String baseUrl
  ) {
    mAuthorizationKey = authorizationKey;
    mHttpRequestFactory = transport.createRequestFactory(new HttpRequestInitializer() {
      @Override
      public void initialize(HttpRequest request) {
        request.setParser(new JsonObjectParser(jsonFactory));
      }
    });
    mImagePostUrl = new GenericUrl(baseUrl);
    mImagePostUrl.appendRawPath(IMAGE_REQUESTS_PATH);
    mImageGetUrl = new GenericUrl(baseUrl);
    mImageGetUrl.appendRawPath(IMAGE_RESPONSES_PATH);
  }

  public CSApi(
    final HttpTransport transport,
    final JsonFactory jsonFactory,
    final String authorizationKey
  ) {
    this(transport, jsonFactory, authorizationKey, BASE_URL);
  }

  public void postImage(final AHandler mUiHandler, final CSPostConfig imagePostConfig) {
      Log.v(TAG, "IN POSTIMAGE");
    new Thread(new Runnable() {
      @Override
      public void run() {
          //Log.v(TAG, "START RUNNING HTTP REQUEST");
        final HttpRequest request;
        try {
            //Log.v(TAG, "RUNNING HTTP REQUEST");
          request = mHttpRequestFactory.buildPostRequest(
                  mImagePostUrl,
                  imagePostConfig.getContent()
          );

          request.getHeaders()
                  .setContentType(CONTENT_TYPE)
                  .setAuthorization(String.format(AUTHORIZATION_FORMAT, mAuthorizationKey));
          CSPostResult result = request.execute().parseAs(CSPostResult.class);
            Log.v(TAG, "CSPostResult result = " + result);
            mUiHandler.obtainMessage(AHandler.Messages.Scanner.FINISH_SCANNING_OBJECT, result).sendToTarget();
        } catch (IOException e) {
          e.printStackTrace();
        }

      }
    }).start();
  }

  public CSGetResult getImage(final String token) throws IOException {
    final HttpRequest request = mHttpRequestFactory.buildGetRequest(
      new GenericUrl(String.format(URL_CONCATENATION_FORMAT, mImageGetUrl.toString(), token))
    );
    request.getHeaders()
      .setContentType(CONTENT_TYPE)
      .setAuthorization(String.format(AUTHORIZATION_FORMAT, mAuthorizationKey));
    return request.execute().parseAs(CSGetResult.class);
  }

  public void getImageInfo(final AHandler uiHandler, final CSPostResult csPostResult) throws IOException {
      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  Log.v(TAG, "GETTING IMAGE CSGetResult");
                  CSGetResult result = getImage(csPostResult.getToken());

                  while(result.getStatus().equals("not completed")) {
                      result = getImage(csPostResult.getToken());
                  }
                  uiHandler.obtainMessage(AHandler.Messages.Scanner.GOT_IMAGE, result).sendToTarget();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }).start();
  }
}
