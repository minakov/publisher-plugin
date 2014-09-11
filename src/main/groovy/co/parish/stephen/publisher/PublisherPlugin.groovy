package co.parish.stephen.publisher

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

class PublisherPlugin implements Plugin<Project> {
    void apply(Project project) {
        if (!project.plugins.withType(AppPlugin)) {
            throw new IllegalStateException("Android plugin is not found")
        }
        applyExtensions(project)
        applyTasks(project)
    }

    void applyExtensions(final Project project) {
        project.extensions.add("publisher", PublisherExtension)
    }

    void applyTasks(final Project project) {
        AppExtension android = project.android
        android.applicationVariants.all { ApplicationVariant variant ->
            if (!variant.buildType.isDebuggable()) {
                String[] tracks = ["alpha", "beta", "production"]
                for (String track : tracks) {
                    PublishTask task = project.tasks.create("publish${variant.name.capitalize()}ToGooglePlay${track.capitalize()}", PublishTask)
                    task.group = "Publish"
                    task.description = "Publishes '${variant.name}' to Google Play Store ${track} track"
                    task.applicationFile = variant.outputFile;
                    task.packageName = variant.packageName;
                    task.track = track;
                    task.dependsOn variant.assemble
                }
            }
        }
    }
}
