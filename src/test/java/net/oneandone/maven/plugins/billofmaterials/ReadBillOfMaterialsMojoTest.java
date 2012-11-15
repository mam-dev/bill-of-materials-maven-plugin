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

import java.io.File;
import java.util.Properties;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Mirko Friedenhagen
 */
public class ReadBillOfMaterialsMojoTest {
    
    final Log LOG = mock(Log.class);
    final MavenProject mockedProject = mock(MavenProject.class);
    final Properties properties = new Properties();

    /**
     * Test of execute method, of class ReadBillOfMaterialsMojo.
     */
    @Test
    public void testExecuteBOMNotFound() throws MojoExecutionException, MojoFailureException {
        ReadBillOfMaterialsMojo instance = createReadBillOfMaterialsMojo(
                "target/test-classes/NON_EXISTING/bill-of-materials.txt");
        instance.setLog(LOG);
        instance.execute();
        verify(LOG).warn(
            "Could not read content 'java.io.FileNotFoundException: target/test-classes/NON_EXISTING/bill-of-materials.txt (No such file or directory)', did you run bill-of-materials:create?");
    }
    
    /**
     * Test of execute method, of class ReadBillOfMaterialsMojo.
     */
    @Test
    public void testExecuteBOMFound() throws MojoExecutionException, MojoFailureException {
        ReadBillOfMaterialsMojo instance = createReadBillOfMaterialsMojo(
                "target/test-classes/ReadBillOfMaterialsMojoTest/bill-of-materials.txt");
        instance.execute();
        assertTrue(properties.getProperty("qaBillOfMaterials").startsWith("# g:a:v "));
    }
    
    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testDefaultConstructor() {
        new ReadBillOfMaterialsMojo();
    }

    ReadBillOfMaterialsMojo createReadBillOfMaterialsMojo(
                String billOfMaterialsPath) {
        when(mockedProject.getProperties()).thenReturn(properties);
        ReadBillOfMaterialsMojo instance = new ReadBillOfMaterialsMojo(
                new File(billOfMaterialsPath), mockedProject);
        instance.setLog(LOG);
        return instance;
    }
    
}
