/*
 * Copyright (C) 2018 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.adapter.action.http.impl;


import io.knotx.forms.http.common.http.HttpClientFacade;
import io.knotx.dataobjects.ClientResponse;
import io.knotx.forms.api.FormsAdapterRequest;
import io.knotx.forms.api.FormsAdapterResponse;
import io.knotx.forms.api.reactivex.AbstractFormsAdapterProxy;
import io.knotx.forms.http.common.configuration.HttpFormsAdapterOptions;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.Single;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;

public class HttpActionAdapterProxyImpl extends AbstractFormsAdapterProxy {

  private HttpClientFacade httpClientFacade;

  public HttpActionAdapterProxyImpl(Vertx vertx, HttpFormsAdapterOptions configuration) {
    this.httpClientFacade = new HttpClientFacade(
        WebClient.create(vertx, configuration.getClientOptions()),
        configuration);
  }

  @Override
  protected Single<FormsAdapterResponse> processRequest(FormsAdapterRequest request) {
    return httpClientFacade.process(request, HttpMethod.POST).map(this::prepareResponse);
  }

  private FormsAdapterResponse prepareResponse(ClientResponse response) {
    FormsAdapterResponse result = new FormsAdapterResponse();

    if (response.getStatusCode() == HttpResponseStatus.OK.code()) {
      if (isJsonBody(response.getBody()) && response.getBody().toJsonObject()
          .containsKey("validationErrors")) {
        result.setSignal("error");
      } else {
        result.setSignal("success");
      }
    }
    result.setResponse(response);

    return result;
  }

  private boolean isJsonBody(Buffer bodyBuffer) {
    String body = bodyBuffer.toString().trim();
    return body.charAt(0) == '{' && body.charAt(body.length() - 1) == '}';
  }
}
