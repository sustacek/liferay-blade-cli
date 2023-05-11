/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// This script gets executed if the archetype si used from Maven => remove all Gradle-related files

Path projectPath = Paths.get(request.outputDirectory, request.artifactId)

Path buildGradlePath = projectPath.resolve("build.gradle")
Path buildGradleKotlinPath = projectPath.resolve("build.gradle.kts")

Path settingsGradlePath = projectPath.resolve("settings.gradle")
Path settingsGradleKotlinPath = projectPath.resolve("settings.gradle.kts")

Path propertiesPath = projectPath.resolve("gradle.properties")
Path propertiesLocalPath = projectPath.resolve("gradle-local.properties")

Files.deleteIfExists buildGradlePath
Files.deleteIfExists buildGradleKotlinPath
Files.deleteIfExists settingsGradlePath
Files.deleteIfExists settingsGradleKotlinPath
Files.deleteIfExists propertiesPath
Files.deleteIfExists propertiesLocalPath