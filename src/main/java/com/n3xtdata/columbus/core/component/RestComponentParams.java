/*
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.n3xtdata.columbus.core.component;


import com.github.wnameless.json.flattener.JsonFlattener;
import com.n3xtdata.columbus.utils.Params;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestComponentParams implements ComponentParams {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String url;
  private Method method;

  RestComponentParams(Params params) {
    this.url = (String) params.get("url");
    this.method = Method.fromString((String) params.get("method"));
  }

  @Override
  public List<Map<String, Object>> execute() throws Exception {
    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
    RestTemplate restTemplate = restTemplateBuilder.build();

    ResponseEntity<String> response;
    switch (method) {
      case GET:
        response = restTemplate.getForEntity(this.url, String.class);
        break;
      case POST:
        response = restTemplate.postForEntity(this.url, "", String.class);
        break;
      default:
        throw new Exception("No valid Rest Method! Please use one of the following: " + Arrays
            .toString(Method.values()));
    }

    Map<String, Object> flatJson = JsonFlattener.flattenAsMap(response.getBody());
    logger.debug("REST RESULT: " + flatJson.toString());

    List<Map<String, Object>> list = new ArrayList<>();
    list.add(flatJson);

    return list;
  }

  @Override
  public String toString() {
    return "RestComponentParams{" +
        "url='" + url + '\'' +
        ", method=" + method +
        '}';
  }

  enum Method {
    GET, POST;

    static Method fromString(String str) {
      for (Method method : Method.values()) {
        if (method.name().equalsIgnoreCase(str)) {
          return method;
        }
      }
      return null;
    }
  }
}