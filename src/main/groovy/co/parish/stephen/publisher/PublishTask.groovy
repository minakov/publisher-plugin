package co.parish.stephen.publisher

import com.google.api.client.http.AbstractInputStreamContent
import com.google.api.client.http.FileContent
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.model.Apk
import com.google.api.services.androidpublisher.model.AppEdit
import com.google.api.services.androidpublisher.model.Track
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class PublishTask extends DefaultTask {
    File applicationFile;
    String packageName;
    String track = "alpha";

    @TaskAction void publish() {
        // get service
        AndroidPublisher service = PublishServiceHelper.get(project.publisher.clientSecretsFile);
        final AndroidPublisher.Edits edits = service.edits();

        // create edit
        AndroidPublisher.Edits.Insert editRequest = edits
                .insert(packageName, null);
        AppEdit edit = editRequest.execute();
        final String editId = edit.getId();
        println "Created edit with id: ${editId}"

        // upload
        final AbstractInputStreamContent apkFile = new FileContent(PublishServiceHelper.APK_MIME_TYPE, applicationFile);
        AndroidPublisher.Edits.Apks.Upload uploadRequest = edits
                .apks()
                .upload(packageName, editId, apkFile);
        Apk apk = uploadRequest.execute();
        println "Version code ${apk.getVersionCode()} has been uploaded"

        // assign to chosen track
        List<java.lang.Integer> apkVersionCodes = new ArrayList<java.lang.Integer>();
        apkVersionCodes.add(apk.getVersionCode());
        Track updatedTrack = edits.tracks()
                .update(packageName, editId, track, new Track().setVersionCodes(apkVersionCodes))
                .execute();
        println "Track ${updatedTrack.getTrack()} has been updated"

        // commit changes
        AppEdit appEdit = edits.commit(packageName, editId)
                .execute();
        println "App edit with id ${appEdit.getId()} has been comitted"
    }
}
