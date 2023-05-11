buildscript {
	dependencies {
		classpath(group = "com.liferay", name = "com.liferay.gradle.plugins", version = "@com.liferay.gradle.plugins.version@")
		classpath(group = "com.liferay", name = "com.liferay.gradle.plugins.workspace", version = "@com.liferay.gradle.plugins.workspace.version@")
		classpath(group = "net.saliman", name = "gradle-properties-plugin", version = "1.4.6")
	}

	repositories {
		maven {
			url = uri("https://repository-cdn.liferay.com/nexus/content/groups/public")
		}
	}
}

apply(plugin = "net.saliman.properties")

apply(plugin = "com.liferay.workspace")