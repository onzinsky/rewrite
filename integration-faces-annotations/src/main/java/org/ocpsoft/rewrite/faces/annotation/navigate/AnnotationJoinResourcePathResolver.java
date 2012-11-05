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
package org.ocpsoft.rewrite.faces.annotation.navigate;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.faces.spi.JoinResourcePathResolver;

/**
 * Implementation of {@link JoinResourcePathResolver} that looks for {@link Join} annotations on the target class.
 * 
 * @author Christian Kaltepoth
 */
public class AnnotationJoinResourcePathResolver implements JoinResourcePathResolver
{

   @Override
   public String byClass(Class<?> clazz)
   {
      Join annotation = clazz.getAnnotation(Join.class);
      if (annotation != null) {
         return annotation.to();
      }
      return null;
   }

}
