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
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

/**
 * Reads a bill of materials for all installed artifacts and puts it's content in a property 
 * called <tt>qaBillOfMaterials</tt> of the project which may be reused by templating plugins.
 * 
 * Spits out a warning when the file does not exist. For usage see the integration tests.
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
 * @author Mirko Friedenhagen <mirko.friedenhagen@1und1.de>
 */
@Mojo(name = "read", defaultPhase = LifecyclePhase.INSTALL)
public class ReadBillOfMaterialsMojo extends AbstractBillOfMaterialsMojo {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ReadBillOfMaterialsMojo.class);
    
    /**
     * Default constructor for maven.
     */
    ReadBillOfMaterialsMojo() {
        super();
    }
    
    /**
     * Just for tests.
     * @param billOfMaterialsPath path to bom.
     * @param project current project
     */
    ReadBillOfMaterialsMojo(File billOfMaterialsPath, MavenProject project) {
        super(billOfMaterialsPath, project);
    }
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        StaticLoggerBinder.getSingleton().setMavenLog(this.getLog());
        final File bomFile = calculateBillOfMaterialsFile();
        LOG.info("Reading bill of materials from {}", bomFile);
        try {
            final String qaBillOfMaterials = Files.toString(bomFile, Charsets.UTF_8);
            LOG.debug("qaBillOfMaterials {}", qaBillOfMaterials);
            getProject().getProperties().put("qaBillOfMaterials", qaBillOfMaterials);
        } catch (IOException e) {
            LOG.warn(String.format(
                        Locale.ENGLISH, "Could not read content '%s', did you run bill-of-materials:create?", e));
        }
    }

}
