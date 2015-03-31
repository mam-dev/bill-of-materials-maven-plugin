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
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by mirko on 31.03.15.
 */
public class GetLatestReleaseMojoTest extends AbstractGetReleaseMojoAbstractTest {

    @Test
    public void testExecute() throws Exception {
        new GetLatestReleaseMojo();
        final GetLatestReleaseMojo mojo = new MyGetLatestReleaseMojo("1.2");
        mojo.execute();
        assertEquals("1.2", project.getProperties().getProperty("previousVersion"));
    }

    @Test(expected = MojoExecutionException.class)
    public void testExecuteParseError() throws Exception {
        final GetLatestReleaseMojo mojo = new MyGetLatestReleaseMojo("abc");
        mojo.execute();
    }

    private class MyGetLatestReleaseMojo extends GetLatestReleaseMojo {
        private String s;

        public MyGetLatestReleaseMojo(String s) {
            super(GetLatestReleaseMojoTest.this.project);
            this.s = s;
        }

        @Override
        InputStream getInputStream(URL url) throws IOException {
            return new ByteArrayInputStream(s.getBytes());
        }
    }
}