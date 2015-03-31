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

import com.google.common.io.CharStreams;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by mirko on 26.03.15.
 */
public abstract class AbstractGetReleaseMojo extends AbstractMojo {
    /**
     * The Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    public AbstractGetReleaseMojo() {

    }

    /**
     * Just for tests.
     * @param project inject for tests.
     */
    AbstractGetReleaseMojo(MavenProject project) {
        this.project = project;
    }

    /**
     * The Artifactory search URL for the latest version.
     */
    @Parameter(defaultValue = "http://repo.jenkins-ci.org/api/search/latestVersion/", required = true,
            property = "bill-of-materials.searchBase")
    private URI searchBase = URI.create("http://repo.jenkins-ci.org/api/search/latestVersion/");

    String getLatestVersion(final String repositories) throws MojoExecutionException {
        final String repositoryString = getRepositoryString(repositories);
        final URI resolveURI = searchBase.resolve(
                "?g=" + project.getGroupId() + "&a=" + project.getArtifactId() + repositoryString);
        getLog().info("resolveURI=" + resolveURI);
        final String latestVersion;
        try {
            final URL url = resolveURI.toURL();
            InputStream in = getInputStream(url);
            try {
                latestVersion = CharStreams.toString(new InputStreamReader(in, Charset.forName("UTF-8")));
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Can not get " + resolveURI, e);
        }
        getLog().info("latestVersion=" + latestVersion + " from repositories='" + repositories + "'");
        return latestVersion;
    }

    String getRepositoryString(String repositories) {
        final String repositoryString;
        if (!repositories.isEmpty()) {
            repositoryString = "&repos=" + repositories;
        } else {
            repositoryString = "";
        }
        return repositoryString;
    }

    InputStream getInputStream(URL url) throws IOException {
        return url.openStream();
    }
}
