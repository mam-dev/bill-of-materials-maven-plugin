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
public class GetLatestReleaseIncludingStagesMojoTest extends AbstractGetReleaseMojoAbstractTest {

    @Test
    public void testExecute() throws Exception {
        new GetLatestReleaseIncludingStagesMojo();
        final GetLatestReleaseIncludingStagesMojo mojo = new MyGetLatestReleaseIncludingStagesMojo("1.2");
        mojo.execute();
        assertEquals("1.3", project.getProperties().getProperty("releaseVersion"));
    }

    @Test(expected = MojoExecutionException.class)
    public void testExecuteParseError() throws Exception {
        final GetLatestReleaseIncludingStagesMojo mojo = new MyGetLatestReleaseIncludingStagesMojo("abc");
        mojo.execute();
    }

    private class MyGetLatestReleaseIncludingStagesMojo extends GetLatestReleaseIncludingStagesMojo {
        private String s;

        public MyGetLatestReleaseIncludingStagesMojo(String s) {
            super(GetLatestReleaseIncludingStagesMojoTest.this.project);
            this.s = s;
        }

        @Override
        InputStream getInputStream(URL url) throws IOException {
            return new ByteArrayInputStream(s.getBytes());
        }
    }
}