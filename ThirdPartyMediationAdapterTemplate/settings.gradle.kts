pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://ads.maven.yandex.net/")
            content {
                includeGroup("com.yandex.android")
                includeGroupAndSubgroups("com.yandex.ads")
            }
        }
    }
}

rootProject.name = "ThirdPartyMediationAdapterTemplate"
include(":app")
