/*
 * Copyright 2011 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
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

package org.ocpsoft.rewrite.servlet.impl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.ocpsoft.rewrite.servlet.spi.RequestParameterProvider;

/**
 * @author Lincoln Baxter, III <lincoln@ocpsoft.com>
 */
public class HttpRewriteWrappedRequest extends HttpServletRequestWrapper implements RequestParameterProvider
{
   private final Map<String, String[]> modifiableParameters;
   private HttpRewriteWrappedRequest outer = null;

   /**
    * Create a new request wrapper that will merge additional parameters into the request object without prematurely
    * reading parameters from the original request.
    */
   public HttpRewriteWrappedRequest(final HttpServletRequest request, final Map<String, String[]> additionalParams)
   {
      super(request);
      modifiableParameters = new TreeMap<String, String[]>();
      modifiableParameters.putAll(additionalParams);

      /*
       * The previous wrapped request needs to be updated when this object's values are modified.
       */
      HttpRewriteWrappedRequest inner = getFromRequest(request);
      if (inner != null)
         inner.setOuter(this);

      setInRequest(this, request);
   }

   private void setOuter(HttpRewriteWrappedRequest outer)
   {
      this.outer = outer;
   }

   @Override
   public String getParameter(final String name)
   {
      String[] strings = getParameterMap().get(name);
      if (strings != null)
      {
         return strings[0];
      }
      return null;
   }

   @Override
   public Map<String, String[]> getParameterMap()
   {
      Map<String, String[]> allParameters = new TreeMap<String, String[]>();
      allParameters.putAll(super.getParameterMap());

      allParameters.putAll(modifiableParameters);

      if (outer != null)
         allParameters.putAll(outer.getModifiableParameters());

      return Collections.unmodifiableMap(allParameters);
   }

   @Override
   public Enumeration<String> getParameterNames()
   {
      return Collections.enumeration(getParameterMap().keySet());
   }

   @Override
   public String[] getParameterValues(final String name)
   {
      return getParameterMap().get(name);
   }

   public Map<String, String[]> getModifiableParameters()
   {
      return modifiableParameters;
   }

   @Override
   public Map<String, String[]> getParameters(ServletRequest request, ServletResponse response)
   {
      return modifiableParameters;
   }

   @Override
   public String toString()
   {
      return super.getRequestURL().toString();
   }

   public static HttpRewriteWrappedRequest getFromRequest(ServletRequest request)
   {
      HttpRewriteWrappedRequest wrapper = (HttpRewriteWrappedRequest) request
               .getAttribute(HttpRewriteWrappedRequest.class.getName());
      return wrapper;
   }

   private static void setInRequest(final HttpRewriteWrappedRequest wrapped, final ServletRequest request)
   {
      request.setAttribute(HttpRewriteWrappedRequest.class.getName(), wrapped);
   }
}
