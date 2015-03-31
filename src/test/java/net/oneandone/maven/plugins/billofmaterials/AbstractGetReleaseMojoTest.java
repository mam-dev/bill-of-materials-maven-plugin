/**
 * Copyright 1&1 Internet AG, https://github.com/1and1/
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
package net.oneandone.maven.plugins.billofmaterials;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by mirko on 31.03.15.
 */
public class AbstractGetReleaseMojoTest extends AbstractGetReleaseMojoAbstractTest {

    @Test
    public void testGetLatestVersion() throws Exception {
        AbstractGetReleaseMojo mojo = new MyAbstractGetReleaseMojo(project);
        final String latestVersion = mojo.getLatestVersion("");
        assertEquals("1.2", latestVersion);
    }

    @Test(expected = MojoExecutionException.class)
    public void testGetLatestVersionIOException() throws Exception {
        AbstractGetReleaseMojo mojo = new MyAbstractGetReleaseMojo(project) {
            @Override
            InputStream getInputStream(URL url) throws IOException {
                throw new IOException("Oops");
            }
        };
        mojo.getLatestVersion("");
    }

    @Test
    public void testGetLatestVersionURL() throws Exception {
        AbstractGetReleaseMojo mojo = new AbstractGetReleaseMojo(project) {
            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {

            }
        };
        final InputStream stream = mojo.getInputStream(new URL("file:pom.xml"));
        stream.close();
    }

    @Test
    public void testGetRepositoryString() throws Exception {
        AbstractGetReleaseMojo mojo = new MyAbstractGetReleaseMojo(project);
        final String repositoryString = mojo.getRepositoryString("");
        assertEquals("", repositoryString);
    }

    static class MyAbstractGetReleaseMojo extends AbstractGetReleaseMojo {
        private final ByteArrayInputStream inputStream;

        public MyAbstractGetReleaseMojo(MavenProject project) {
            super(project);
            inputStream = new ByteArrayInputStream("1.2".getBytes());
        }

        @Override
        public void execute() throws MojoExecutionException, MojoFailureException {
        }

        @Override
        InputStream getInputStream(URL url) throws IOException {
            return inputStream;
        }
    }
}