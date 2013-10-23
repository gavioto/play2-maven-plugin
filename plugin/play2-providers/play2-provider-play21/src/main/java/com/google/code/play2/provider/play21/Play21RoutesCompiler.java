/*
 * Copyright 2013 Grzegorz Slowikowski (gslowikowski at gmail dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.google.code.play2.provider.play21;

import java.io.File;
//?import java.io.IOException;
import java.util.List;

import scala.collection.JavaConversions;

import play.router.RoutesCompiler.RoutesCompilationError;
import play.router.RoutesCompiler$;

import com.google.code.play2.provider.Play2RoutesCompiler;
import com.google.code.play2.provider.RoutesCompilationException;

public class Play21RoutesCompiler
    implements Play2RoutesCompiler
{
    private File outputDirectory;

    private List<String> additionalImports;

    public void setOutputDirectory( File outputDirectory )
    {
        this.outputDirectory = outputDirectory;
    }

    public void setAdditionalImports( List<String> additionalImports )
    {
        this.additionalImports = additionalImports;
    }

    public void compile( File routesFile )
        throws RoutesCompilationException
    {
        try
        {
            RoutesCompiler$.MODULE$.compile( routesFile, outputDirectory,
                                             JavaConversions.asScalaBuffer( additionalImports ) );
        }
        catch ( RoutesCompilationError e )
        {
            throw new RoutesCompilationException( e.source(), e.message(), (Integer) e.line().get(),
                                                  (Integer) e.column().get() );
        }
    }

}
