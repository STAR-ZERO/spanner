/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.ilios.spanner.config;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import dk.ilios.spanner.options.SpannerOptions;
import dk.ilios.spanner.util.Util;

/**
 * Loads Gauge configuration files and, if necessary, creates new versions from the defaults.
 */
public final class SpannerConfigLoader {
    private final SpannerOptions options;

    public SpannerConfigLoader(SpannerOptions options) {
        this.options = options;
    }

    public SpannerConfiguration loadOrCreate() throws InvalidConfigurationException {
        File configFile = options.spannerConfigFile();
        ImmutableMap<String, String> defaults;
        try {
            defaults = Util.loadProperties(Util.resourceSupplier(SpannerConfiguration.class, "/global-config.properties"));
        } catch (IOException impossible) {
            throw new AssertionError(impossible);
        }

        if (configFile.exists()) {
            try {
                ImmutableMap<String, String> user =
                        Util.loadProperties(Files.asByteSource(configFile));
                return new SpannerConfiguration(mergeProperties(options.configProperties(), user, defaults));
            } catch (IOException keepGoing) {
            }
        }

        ByteSource supplier = Util.resourceSupplier(SpannerConfiguration.class, "/default-config.properties");
        tryCopyIfNeeded(supplier, configFile);

        ImmutableMap<String, String> user;
        try {
            user = Util.loadProperties(supplier);
        } catch (IOException e) {
            throw new AssertionError(e); // class path must be messed up
        }
        return new SpannerConfiguration(mergeProperties(options.configProperties(), user, defaults));
    }

    private static ImmutableMap<String, String> mergeProperties(Map<String, String> commandLine,
                                                                Map<String, String> user,
                                                                Map<String, String> defaults) {
        Map<String, String> map = Maps.newHashMap(defaults);
        map.putAll(user); // overwrite and augment
        map.putAll(commandLine); // overwrite and augment
        Iterables.removeIf(map.values(), Predicates.equalTo(""));
        return ImmutableMap.copyOf(map);
    }

    private static void tryCopyIfNeeded(ByteSource supplier, File rcFile) {
        if (!rcFile.exists()) {
            try {
                supplier.copyTo(Files.asByteSink(rcFile));
            } catch (IOException e) {
                rcFile.delete();
            }
        }
    }
}