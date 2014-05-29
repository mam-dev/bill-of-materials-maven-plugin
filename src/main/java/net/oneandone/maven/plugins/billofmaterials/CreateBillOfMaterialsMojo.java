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

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

/**
 * Creates a bill of materials for all installed artifacts.
 *
 * <p>This in the standard format for the <tt>sha1sum</tt> command including meta information:</p>
 * <pre>
 * # company:company-parent-pom:1.0-SNAPSHOT user=mirko
 * 2dcb20b977ff170dd802c30b804229264c97ebf6  company-parent-pom-1.0-SNAPSHOT.pom
 * # company:child1:1.0-SNAPSHOT user=mirko
 * ed5b932c3157b347d0f7a4ec773ae5d5890c1ada  child1-1.0-SNAPSHOT-sources.jar
 * 8294565e2a5d99b548b111fe6262719331436143  child1-1.0-SNAPSHOT.jar
 * 082fa2206c4a00e3f428e9100199a0337ad42fdb  child1-1.0-SNAPSHOT.pom
 * # company:child2:1.0-SNAPSHOT user=mirko
 * 05d419cf53e175c6e84ddc1cf2fccdc9dd109c6b  child2-1.0-SNAPSHOT-sources.jar
 * df633b963220ba124ffa80eb6ceab676934bb387  child2-1.0-SNAPSHOT.jar
 * 5661e9270a02c5359be47615bb6ed9911105d878  child2-1.0-SNAPSHOT.pom
 * </pre>
 *
 * @author Mirko Friedenhagen &lt;mirko.friedenhagen@1und1.de&gt;
 */
@Mojo(name = "create", aggregator = false, defaultPhase = LifecyclePhase.INSTALL)
public class CreateBillOfMaterialsMojo extends AbstractBillOfMaterialsMojo {

    /**
     * SHA1 hash function.
     */
    private final HashFunction sha1 = Hashing.sha1();

    /**
     * Function to calculate the hash of a file.
     */
    private final Function<File, String> toBomStringFunction;

    /**
     * Function to get the file from the artifact.
     */
    private final Function<Artifact, File> toFileFunction;

    /**
     * Default constructor for maven.
     */
    CreateBillOfMaterialsMojo() {
        super();
        toFileFunction = new ToFileFunction();
        toBomStringFunction = new ToBomStringFunction(sha1);
    }

    /**
     * Just for tests.
     * @param billOfMaterialsPath path to bom.
     * @param project current project
     */
    CreateBillOfMaterialsMojo(File billOfMaterialsPath, MavenProject project) {
        super(billOfMaterialsPath, project);
        toFileFunction = new ToFileFunction();
        toBomStringFunction = new ToBomStringFunction(sha1);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            final List<Artifact> artifacts = getListOfArtifacts();
            final ArrayList<File> files = new ArrayList<File>(Collections2.transform(artifacts, toFileFunction));
            final ArrayList<String> hashBaseNames = new ArrayList<String>(Collections2.transform(files, toBomStringFunction));
            addHashEntryForPom(hashBaseNames);
            writeResults(hashBaseNames);
        } catch (IOException ex) {
            throw new MojoExecutionException(ex.toString(), ex);
        }
    }

    /**
     * Creates a list of all artifacts for the build.
     * @return a list of all artifacts for the build including the attached ones.
     */
    List<Artifact> getListOfArtifacts() {
        final MavenProject project = getProject();
        final List<Artifact> artifacts = new ArrayList<Artifact>(project.getAttachedArtifacts());
        final String packaging = project.getPackaging();
        // POMs return null as their artifact, which will crash the transformation lateron.
        if (!"pom".equals(packaging)) {
            artifacts.add(project.getArtifact());
        }
        return artifacts;
    }

    /**
     * Adds the hash entry for the POM.
     * @param hashBaseNames to add the entry to.
     * @throws IOException when the POM could not be read.
     */
    void addHashEntryForPom(final List<String> hashBaseNames) throws IOException {
        final MavenProject project = getProject();
        //Files.copy(project.getFile(), new File(getTargetDirectory(), "pom.xml"));
        final HashCode sha1OfPom = Files.hash(project.getFile(), sha1);
        final String pomLine = String.format(Locale.ENGLISH, "%s  %s-%s.pom",
                    sha1OfPom, project.getArtifactId(), project.getVersion());
        hashBaseNames.add(pomLine);
    }

    /**
     * Writes the resulting hash file to {@link CreateBillOfMaterialsMojo#billOfMaterialsPath}.
     *
     * @param hashBaseNames to write
     * @throws IOException when the parent directory could not be created or something went wrong while writing the result.
     */
    void writeResults(final List<String> hashBaseNames) throws IOException {
        final String hashBaseNamesAsString = Joiner.on("\n").join(hashBaseNames) + "\n";
        final String userName = System.getProperty("user.name");
        write(projectCommentToString(userName));
        write(hashBaseNamesAsString);
    }

    /**
     * Writes content to the bomFile creating intermediate directories.
     *
     * @param content to write
     * @throws IOException when the target directory could not be created or the content could not be written.
     */
    void write(final String content) throws IOException {
        final File bomFile = calculateBillOfMaterialsFile();
        final File parentDirectory = bomFile.getParentFile();
        if (!createParentDirectory(parentDirectory)) {
            throw new IOException("Could not create parent directory for " + bomFile);
        }
        Files.append(content, bomFile, Charsets.UTF_8);
    }

    /**
     * Returns a string representation for the comment.
     *
     * @param userName current user
     * @return string representation for the comment.
     */
    String projectCommentToString(final String userName) {
        final MavenProject project = getProject();
        return String.format(
                Locale.ENGLISH,
                "# %s:%s:%s user=%s\n",
                project.getGroupId(), project.getArtifactId(), project.getVersion(), userName);
    }

    /**
     * Creates directory for storage.
     *
     * @param parentDirectory
     * @return true when parentDirectory could not be created.
     */
    boolean createParentDirectory(final File parentDirectory) {
        return parentDirectory.exists() || parentDirectory.mkdirs();
    }
}
