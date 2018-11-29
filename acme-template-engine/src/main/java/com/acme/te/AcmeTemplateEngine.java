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
package com.acme.te;

import io.knotx.dataobjects.Fragment;
import io.knotx.te.api.TemplateEngine;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;

class AcmeTemplateEngine implements TemplateEngine {

  private static final Logger LOGGER = LoggerFactory.getLogger(AcmeTemplateEngine.class);

  public AcmeTemplateEngine(Vertx vertx, JsonObject config) {
    LOGGER.info("<{}> instance created", this.getClass().getSimpleName());
  }

  @Override
  public String process(Fragment fragment) {
    return "<p>Hello from Acme Template Engine</p>";
  }
}
