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

package com.google.code.play2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import org.codehaus.plexus.util.DirectoryScanner;

import com.google.code.play2.provider.AssetCompilationException;
import com.google.code.play2.provider.CoffeescriptCompilationResult;
import com.google.code.play2.provider.Play2CoffeescriptCompiler;
import com.google.code.play2.provider.Play2JavascriptCompiler;

/**
 * Compile Coffee Script assets
 * 
 * @author <a href="mailto:gslowikowski@gmail.com">Grzegorz Slowikowski</a>
 * @since 1.0.0
 */
@Mojo( name = "coffee-compile", defaultPhase = LifecyclePhase.GENERATE_RESOURCES )
public class Play2CoffeeCompileMojo
    extends AbstractPlay2AssetsCompileMojo
{

    private static final String[] coffeeExcludes = new String[] {};

    private static final String[] coffeeIncludes = new String[] { "**/*.coffee" };

    protected boolean compileAssets( File assetsSourceDirectory, File outputDirectory )
        throws AssetCompilationException, IOException
    {
        boolean anythingCompiled = false;
        
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir( assetsSourceDirectory );
        scanner.setIncludes( coffeeIncludes );
        scanner.setExcludes( coffeeExcludes );
        scanner.addDefaultExcludes();
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        if ( files.length > 0 )
        {
            Play2CoffeescriptCompiler compiler = play2Provider.getCoffeescriptCompiler();
            compiler.setOptions( new ArrayList<String>() /* TEMP */ );

            for ( String fileName : files )
            {
                getLog().debug( String.format( "Processing file \"%s\"", fileName ) );
                File coffeeFile = new File( assetsSourceDirectory, fileName );

                String jsFileName = fileName.replace( ".coffee", ".js" );
                File jsFile = new File( outputDirectory, jsFileName );

                String minifiedJsFileName = fileName.replace( ".coffee", ".min.js" );
                File minifiedJsFile = new File( outputDirectory, minifiedJsFileName );

                boolean modified = true;
                if ( jsFile.isFile() )
                {
                    modified = ( jsFile.lastModified() < coffeeFile.lastModified() );
                }

                if ( modified )
                {
                    CoffeescriptCompilationResult result = compiler.compile( coffeeFile );
                    String jsContent = result.getJs();
                    createDirectory( jsFile.getParentFile(), false );
                    writeToFile( jsFile, jsContent );
                    try
                    {
                        Play2JavascriptCompiler jsCompiler = play2Provider.getJavascriptCompiler();
                        String minifiedJsContent = jsCompiler.minify( jsContent, coffeeFile.getName() );
                        // String minifiedJsContent = JavascriptCompiler.minify( jsContent, coffeeFile.getName() );
                        createDirectory( minifiedJsFile.getParentFile(), false );
                        writeToFile( minifiedJsFile, minifiedJsContent );
                    }
                    catch ( AssetCompilationException e )
                    {
                        // ignore
                        if ( minifiedJsFile.exists() )
                        { // TODO-check if isFile
                            minifiedJsFile.delete(); // TODO-check result
                        }
                    }
                }
            }
            anythingCompiled = true;
        }
        return anythingCompiled;
    }

}
