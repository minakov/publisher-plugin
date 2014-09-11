package co.parish.stephen.publisher

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.AndroidPublisherScopes

class PublishServiceHelper {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private static HttpTransport HTTP_TRANSPORT

    static final String APK_MIME_TYPE = "application/vnd.android.package-archive";

    static AndroidPublisher get(File clientSecrets) {
        newTrustedTransport();
        Credential credential = authorizeWithServiceAccount(clientSecrets);
        return new AndroidPublisher.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Publisher Plugin")
                .build();
    }

    static void newTrustedTransport(){
        if (null == HTTP_TRANSPORT) {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        }
    }

    static Credential authorizeWithServiceAccount(File clientSecrets) {
        return GoogleCredential.fromStream(new FileInputStream(clientSecrets), HTTP_TRANSPORT, JSON_FACTORY)
                .createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));
    }
}
