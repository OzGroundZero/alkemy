/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.groundzero.alkemy.cloudsight;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class CSPostResult extends GenericJson {
  @Key
  private String token;

  @Key
  private String url;

  public String getToken() {
    return token;
  }

  public String getUrl() {
    return url;
  }
}

