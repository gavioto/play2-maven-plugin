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
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
//import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Compile Scala and Java test sources
 * 
 * @author <a href="mailto:gslowikowski@gmail.com">Grzegorz Slowikowski</a>
 * @since 1.0.0
 */
@Mojo( name = "testCompile"/*, defaultPhase = LifecyclePhase.TEST_COMPILE*/, requiresDependencyResolution = ResolutionScope.TEST )
public class Play2ScalaTestCompileMojo
    extends AbstractPlay2ScalaCompileMojo
{
    /**
     * Set this to 'true' to bypass compilation of test sources. Its use is NOT RECOMMENDED, but quite convenient on
     * occasion.
     * 
     */
    @Parameter ( property = "maven.test.skip" )
    private boolean testCompileSkip;

    /**
     * The source directories containing the test-source to be compiled.
     * 
     */
    @Parameter ( defaultValue = "${project.testCompileSourceRoots}", readonly = true, required = true )
    private List<String> compileSourceRoots;

    /**
     * Project test classpath.
     * 
     */
    @Parameter ( defaultValue = "${project.testClasspathElements}", required = true, readonly = true )
    private List<String> classpathElements;

    /**
     * The directory where compiled test classes go.
     * 
     */
    @Parameter ( defaultValue = "${project.build.testOutputDirectory}", required = true, readonly = true )
    private File outputDirectory;

    @Override
    protected void internalExecute()
        throws MojoExecutionException, MojoFailureException, IOException
    {
        if ( testCompileSkip )
        {
            getLog().info( "Not compiling test sources" );
        }
        else
        {
            super.internalExecute();
        }
    }

    @Override
    protected List<String> getCompileSourceRoots()
    {
        return compileSourceRoots;
    }

    @Override
    protected List<String> getClasspathElements()
    {
        return classpathElements;
    }

    @Override
    protected File getOutputDirectory()
    {
        return outputDirectory;
    }

    @Override
    protected File getAnalysisCacheFile()
    {
        return defaultTestAnalysisCacheFile( project );
    }

}
