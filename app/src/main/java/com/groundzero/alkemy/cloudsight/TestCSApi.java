/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.groundzero.alkemy.cloudsight;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class TestCSApi {

  private static final String API_KEY = "RSqxYRJHYj5Ik2T37il2hg";

  static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  static final JsonFactory JSON_FACTORY = new JacksonFactory();



  public static void main(String[] args) throws Exception {
    CSApi api = new CSApi(
      HTTP_TRANSPORT,
      JSON_FACTORY,
      API_KEY
    );
    
    CSPostConfig imageToPost = CSPostConfig.newBuilder()
      .withRemoteImageUrl("http://files.parsetfss.com/0b0c321c-76ab-429b-8224-139e45918ddb/tfss-788a92f4-41ca-4b04-b603-d2921b845cec-LdanielOzSpringSite.jpg")
      .build();

    //CSPostResult portResult = api.postImage(null,imageToPost);

    //System.out.println("Post result: " + portResult);

    Thread.sleep(5000);
    
    //CSGetResult scoredResult = api.getImage(portResult);

    //System.out.println(scoredResult);
  }
}
