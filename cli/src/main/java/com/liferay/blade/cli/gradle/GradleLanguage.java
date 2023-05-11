/**
 * Copyright (c) 2000-2023 Liferay, Inc. All rights reserved.
 * <p>
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */
package com.liferay.blade.cli.gradle;

import java.io.File;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * The scripting language used in a Gradle project forming the Liferay Workspace.
 *
 * Gradle supports Groovy and since Gradle 5.0, Kotlin as well.
 * @author Josef Sustacek
 */
public enum GradleLanguage {
	GROOVY(
		"build.gradle", "settings.gradle",
		".*apply\\s*plugin\\s*:\\s*[\'\"]com\\.liferay\\.workspace[\'\"]\\s*$",
		".*name:\\s*\"com\\.liferay\\.gradle\\.plugins\\.workspace\",\\s*version:" +
				"\\s*\"([latest\\.release|latest\\.integration]+)\".*",
		".*name:\\s*\"com\\.liferay\\.gradle\\.plugins\\.workspace\",\\s*version:\\s*\"([0-9\\.]+)\".*"),
	KOTLIN(
		"build.gradle.kts", "settings.gradle.kts",
		".*apply\\s*\\(\\s*plugin\\s*=\\s*\"com\\.liferay\\.workspace\"\\s*\\)\\s*$",
		".*name\\s*=\\s*\"com\\.liferay\\.gradle\\.plugins\\.workspace\",\\s*version\\s*=" +
				"\\s*\"([latest\\.release|latest\\.integration]+)\".*",
		".*name\\s*=\\s*\"com\\.liferay\\.gradle\\.plugins\\.workspace\",\\s*version\\s*=\\s*\"([0-9\\.]+)\".*");

	GradleLanguage(String buildScriptFileName, String settingsScriptFileName, String patternWorkspacePlugin, String patternWorkspacePluginLatestRelease, String patternWorkspacePluginVersion) {
		this.buildScriptFileName = buildScriptFileName;
		this.settingsScriptFileName = settingsScriptFileName;
		this.patternWorkspacePlugin =
			Pattern.compile(patternWorkspacePlugin, Pattern.MULTILINE | Pattern.DOTALL);
		this.patternWorkspacePluginLatestRelease =
			Pattern.compile(patternWorkspacePluginLatestRelease, Pattern.MULTILINE | Pattern.DOTALL);
		this.patternWorkspacePluginVersion =
			Pattern.compile(patternWorkspacePluginVersion, Pattern.MULTILINE | Pattern.DOTALL);
	}

	private final String buildScriptFileName;

	private final String settingsScriptFileName;

	private final Pattern patternWorkspacePlugin;

	private final Pattern patternWorkspacePluginLatestRelease;

	private final Pattern patternWorkspacePluginVersion;

	/**
	 * Returns the Gradle's language as detected in given rootProject directory or
	 * throws an exception if none could be recognized.
	 * 
	 * @param gradleRootProjectDir the rootProject directory of Gradle where the language should be detected.
	 * @return the detected Gradle language
	 * @throws RuntimeException in case no language out of the known ones could be detected
	 */
	public static GradleLanguage detect(File gradleRootProjectDir) {
		if(Objects.isNull(gradleRootProjectDir) || !gradleRootProjectDir.isDirectory()) {
			throw new RuntimeException(
				"Cannot detect Gradle language in " + gradleRootProjectDir + ", it is either null or not a directory.");
		}

		// Groovy takes precedence in Gradle, i.e. if both setting.gradle (Groovy) and settings.gradle.kts (Kotlin)
		// are present in rootProject of Gradle, then the Groovy one will be evaluated and used;
		// the same is true for any 'build.gradle' vs. 'build.gradle.kts' within the same directory
		for (GradleLanguage candidate: new GradleLanguage[] { GROOVY, KOTLIN } ) {
			File settingsScript = new File(gradleRootProjectDir, candidate.settingsScriptFileName);

			if(settingsScript.isFile()) {
				return candidate;
			}
		}

		throw new RuntimeException(
			"Cannot detect Gradle language for root project directory " + gradleRootProjectDir +
				", neither Groovy nor Kotlin scripts found in this directory.");
	}

	public String getBuildScriptFileName() {
		return buildScriptFileName;
	}

	public String getSettingsScriptFileName() {
		return settingsScriptFileName;
	}

	public Pattern getPatternWorkspacePlugin() {
		return patternWorkspacePlugin;
	}

	public Pattern getPatternWorkspacePluginLatestRelease() {
		return patternWorkspacePluginLatestRelease;
	}

	public Pattern getPatternWorkspacePluginVersion() {
		return patternWorkspacePluginVersion;
	}
}
