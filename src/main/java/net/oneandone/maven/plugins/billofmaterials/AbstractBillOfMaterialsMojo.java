/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
     * Path to the Output-file relative to the basedir of the execution-root.
     */
    @Parameter(defaultValue = "target/tickets/bill-of-materials.txt", required = true, property = "bill-of-materials.path")
    private String billOfMaterialsPath;

    /**
     * The Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * The exectionRootDirectory of the current Maven session.
     */
    @Parameter(defaultValue = "${session.executionRootDirectory}", readonly = true)
    private File sessionExectionRootDirectory;

    /**
     * Returns the {@link File} pointing to the bill of materials.
     *
     * @return {@link File} pointing to the bill of materials.
     */
    File calculateBillOfMaterialsFile() {
        final File bomFile = new File(sessionExectionRootDirectory, billOfMaterialsPath);
        LOG.debug("bill-of-materials file={}", bomFile);
        return bomFile;
    }

    /**
     * @return the project
     */
    MavenProject getProject() {
        return project;
    }

}
