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

import com.google.common.base.Function;
import java.io.File;
import org.apache.maven.artifact.Artifact;

/**
 * Returns the file connected to the given {@link Artifact}.
 * @author Mirko Friedenhagen
 */
final class ToFileFunction implements Function<Artifact, File> {

    @Override
    public File apply(final Artifact artifact) {
        return artifact.getFile();
    }

}
