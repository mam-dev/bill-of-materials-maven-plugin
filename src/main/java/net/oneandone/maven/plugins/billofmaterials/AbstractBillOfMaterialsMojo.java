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
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class implementing the calculation of the bill of materials file.
 *
 * @author Mirko Friedenhagen <mirko.friedenhagen@1und1.de>
 */
public abstract class AbstractBillOfMaterialsMojo extends AbstractMojo {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBillOfMaterialsMojo.class);

    /**
     * Absolute path to the output-file.
     */
    @Parameter(
        defaultValue = "${session.executionRootDirectory}/target/tickets/bill-of-materials.txt",
        required = true, property = "bill-of-materials.bomPath")
    private File bomPath;

    /**
     * The Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Default constructor for maven.
     */
    AbstractBillOfMaterialsMojo() {
        super();
    }
    
    /**
     * Just for tests.
     * @param billOfMaterialsPath relative path to bom.
     * @param project current project
     */
    AbstractBillOfMaterialsMojo(File billOfMaterialsPath, MavenProject project) {
        this.bomPath = billOfMaterialsPath;
        this.project = project;
    }

    
    /**
     * Returns the {@link File} pointing to the bill of materials.
     *
     * @return {@link File} pointing to the bill of materials.
     */
    File calculateBillOfMaterialsFile() {
        LOG.debug("bill-of-materials file={}", bomPath);
        return bomPath;
    }

    /**
     * @return the project
     */
    MavenProject getProject() {
        return project;
    }
}
