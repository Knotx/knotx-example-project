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
package com.acme.hbs.helpers;

import com.github.jknack.handlebars.Options;
import io.knotx.te.handlebars.CustomHandlebarsHelper;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 * Checks if current value and given parameter are equal as Strings.<br> Usage:
 * <pre>
 *     {{#string_equals value "parameter"}}
 *         equal
 *     {{else}}
 *         not equal
 *     {{/string_equals}}
 * </pre>
 * If value is "parameter", the output will be "equal".
 */
public class StringEqualsHelper implements CustomHandlebarsHelper<Object> {

  @Override
  public String getName() {
    return "string_equals";
  }

  @Override
  public CharSequence apply(Object value, Options options) throws IOException {
    Options.Buffer buffer = options.buffer();
    if (StringUtils.equals(String.valueOf(value), options.param(0, StringUtils.EMPTY))) {
      buffer.append(options.fn());
    } else {
      buffer.append(options.inverse());
    }
    return buffer;
  }
}
