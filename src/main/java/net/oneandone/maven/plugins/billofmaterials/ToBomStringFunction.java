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

import com.google.common.base.Function;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;

/**
 * Creates a hashsum check for a single artifact.
 * @author Mirko Friedenhagen
 */
final class ToBomStringFunction implements Function<File, String> {
    /**
     * SHA1 algorithm.
     */
    private final HashFunction hashFunction;

    /**
     * @param hashFunction to use.
     */
    ToBomStringFunction(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
    }

    @Override
    public String apply(final File file) {
        final HashCode hash;
        try {
            hash = Files.asByteSource(file).hash(hashFunction);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not create hash for " + file, e);
        }
        return hash + "  " + file.getName();
    }

}
