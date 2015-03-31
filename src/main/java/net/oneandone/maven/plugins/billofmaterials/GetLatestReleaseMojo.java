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
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.release.versions.DefaultVersionInfo;
import org.apache.maven.shared.release.versions.VersionParseException;

/**
 * Creates the latest version by retrieving all releases from the repositories called
 * <tt>bill-of-materials.releasesRepositories</tt>.
 */
@Mojo(name = "get-latest-release", aggregator = true, requiresProject = true)
public class GetLatestReleaseMojo extends AbstractGetReleaseMojo {

    /**
     * Comma seperated list of repositories to search.
     */
    @Parameter(defaultValue = "repo1", required = true, property = "bill-of-materials.releasesRepositories")
    private String releases = "repo1";

    public GetLatestReleaseMojo() {

    }

    /**
     * For tests.
     * @param project for tests.
     */
    GetLatestReleaseMojo(MavenProject project) {
        super(project);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final String latestVersion = getLatestVersion(releases);
        final DefaultVersionInfo versionInfo;
        try {
            versionInfo = new DefaultVersionInfo(latestVersion);
        } catch (VersionParseException e) {
            throw new MojoExecutionException("Could not parse latestVersion" + latestVersion, e);
        }
        project.getProperties().setProperty("previousVersion", versionInfo.getReleaseVersionString());
    }

}
