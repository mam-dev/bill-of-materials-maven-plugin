/*
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
import java.io.IOException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mifr
 */
public class AbstractBillOfMaterialsMojoTest {
    
    @Before
    public void setLogging() {
    }
    /**
     * Test of calculateBillOfMaterialsFile method, of class AbstractBillOfMaterialsMojo.
     */
    @Test
    public void testCalculateBillOfMaterialsFile() throws IOException {
        AbstractBillOfMaterialsMojo instance = new AbstractBillOfMaterialsMojoImpl(new File("bom.txt"), null);
        File expResult = new File("bom.txt");
        File result = instance.calculateBillOfMaterialsFile();
        assertEquals(expResult.getCanonicalFile(), result.getCanonicalFile());
    }

    /**
     * Test of getProject method, of class AbstractBillOfMaterialsMojo.
     */
    @Test
    public void testGetProject() {
        final MavenProject mavenProject = new MavenProject();
        AbstractBillOfMaterialsMojo instance = new AbstractBillOfMaterialsMojoImpl(new File("DOES_NOT_MATTER"), mavenProject);
        MavenProject result = instance.getProject();
        assertEquals(mavenProject, result);
    }

    private class AbstractBillOfMaterialsMojoImpl extends AbstractBillOfMaterialsMojo {

        AbstractBillOfMaterialsMojoImpl(File billOfMaterialsPath, MavenProject project) {
            super(billOfMaterialsPath, project);
        }
        
        @Override
        public void execute() throws MojoExecutionException, MojoFailureException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
