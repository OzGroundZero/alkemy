/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.groundzero.alkemy.cloudsight;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class CSGetResult extends GenericJson {
  @Key
  private String status;

  @Key
  private String name;

  public String getStatus() {
    return status;
  }

  public String getName() {
    return name;
  }
}
