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

import com.google.common.hash.Hashing;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.oneandone.maven.plugins.billofmaterials.CreateBillOfMaterialsMojo.ToBomString;
import net.oneandone.maven.plugins.billofmaterials.CreateBillOfMaterialsMojo.ToFile;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.slf4j.impl.StaticLoggerBinder;

/**
 *
 * @author Mirko Friedenhagen
 */
public class CreateBillOfMaterialsMojoTest {

    private static final String EMPTY_FILE_FOR_SHA1 = CreateBillOfMaterialsMojoTest.class.getResource("/sha1-test-dummy.txt").getFile();

    @Before
    public void setLogging() {
        StaticLoggerBinder.getSingleton().setMavenLog(mock(Log.class));
    }

    /**
     * Test of execute method, of class CreateBillOfMaterialsMojo.
     */
    @Test    
    public void testExecute() throws Exception {
        final MavenProject projectMock = createMinimalProject();
        final CreateBillOfMaterialsMojo instance = new CreateBillOfMaterialsMojo(
                "CreateBillOfMaterialsMojoTest/tickets/bill-of-materials.txt", projectMock, new File("target"));
        when(projectMock.getPackaging()).thenReturn("pom");
        instance.execute();
        assertTrue("Expected existing file",
                new File("target/CreateBillOfMaterialsMojoTest/tickets/bill-of-materials.txt").exists());
    }

    /**
     * Test of getListOfArtifacts method, of class CreateBillOfMaterialsMojo.
     */
    @Test
    public void testGetListOfArtifacts() {
        final MavenProject projectMock = createMinimalProject();        
        final CreateBillOfMaterialsMojo instance = new CreateBillOfMaterialsMojo(null, projectMock, null);
        when(projectMock.getPackaging()).thenReturn("pom");
        final List pomResult = instance.getListOfArtifacts();
        // no attached artifacts => POM projects have no artifact => empty list.
        assertEquals(0, pomResult.size());
        when(projectMock.getPackaging()).thenReturn("jar");
        when(projectMock.getArtifact()).thenReturn(mock(Artifact.class));
        final List jarResult = instance.getListOfArtifacts();
        // no attached artifacts => JAR project's artifact is the JAR => list with one element!
        assertEquals(1, jarResult.size());
    }

    /**
     * Test of addHashEntryForPom method, of class CreateBillOfMaterialsMojo.
     */
    @Test
    public void testAddHashEntryForPom() throws Exception {
        final List<String> hashBaseNames = new ArrayList<String>();
        final MavenProject projectMock = createMinimalProject();
        CreateBillOfMaterialsMojo sut = new CreateBillOfMaterialsMojo(null, projectMock, null);
        sut.addHashEntryForPom(hashBaseNames);
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709  a-v.pom", hashBaseNames.get(0));
    }

    /**
     * Test of writeResults method, of class CreateBillOfMaterialsMojo.
     */
    @Test
    public void testWriteResults() throws Exception {
        System.setProperty("user.name", "wwhite");
        final MavenProject projectMock = createMinimalProject();
        final List<String> hashBaseNames = Arrays.asList("line1", "line2");
        final StringBuilder result = new StringBuilder();
        CreateBillOfMaterialsMojo sut = new CreateBillOfMaterialsMojo(null, projectMock, null) {
            void write(final String content) {
                result.append(content);
            }
        };
        sut.writeResults(hashBaseNames);
        assertEquals("# g:a:v user=wwhite\nline1\nline2\n", result.toString());
    }

    @Test
    public void testProjectCommentToString() {
        final MavenProject projectMock = createMinimalProject();
        final CreateBillOfMaterialsMojo sut = new CreateBillOfMaterialsMojo("DOES NOT MATTEER", projectMock, null);
        assertEquals("# g:a:v user=wwhite\n", sut.projectCommentToString("wwhite"));

    }

    @Test
    public void testToFile() {
        final ToFile sut = new ToFile();
        final File expected = new File("");
        final Artifact mock = mock(Artifact.class);
        when(mock.getFile()).thenReturn(expected);
        assertEquals(expected, sut.apply(mock));
    }

    @Test
    public void testToBomString() {
        final ToBomString sut = new ToBomString(Hashing.sha1());
        final File fileForWhichWeWantToCalculateSha1 = new File(EMPTY_FILE_FOR_SHA1);
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709  sha1-test-dummy.txt", sut.apply(fileForWhichWeWantToCalculateSha1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBomStringFail() {
        final ToBomString sut = new ToBomString(Hashing.sha1());
        final File nonExistingFileForWhichWeWantToCalculateSha1 = new File("I DO NOT EXIST");
        sut.apply(nonExistingFileForWhichWeWantToCalculateSha1);
    }

    private MavenProject createMinimalProject() {
        final MavenProject projectMock = mock(MavenProject.class);
        when(projectMock.getGroupId()).thenReturn("g");
        when(projectMock.getArtifactId()).thenReturn("a");
        when(projectMock.getVersion()).thenReturn("v");
        when(projectMock.getFile()).thenReturn(new File(EMPTY_FILE_FOR_SHA1));
        when(projectMock.getAttachedArtifacts()).thenReturn(new ArrayList<Artifact>());
        return projectMock;
    }
}
