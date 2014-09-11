Android Publisher Plugin
========================

A simple gradle plugin for uploading to the Google Play store with the [Google Play Developer API][play-api]

Supported functionality:

- Uploading an APK to alpha, beta and production tracks.

Usage
-----

### Recommendations

Use at your own risk. I highly recommend testing with uploads to Alpha track first. My personal process:

1. **Developer** 
    - Commit a release candidate branch
2. **CI Server**
    - Build and run acceptence test
    - If passing upload to **alpha**
3. **Developer**
    - Log in to play
    - Verify proper build details
    - Promote to beta or prod

It is not recommended to upload builds to **any track** more than once a day. The production track should be updated even less frequently, and with extreme care.

See [Google Play Developer API Usage Instructions][play-api-usage] for more tips.


### What You Need

- Service account for uploading 
- Private key for service account

[Create a service account here](https://developers.google.com/android-publisher/getting_started#setting_up_api_access_clients)

When done, you should be able to download `client_secrets.json` for the service account. **DO NOT** commit this file to source control. Should look something like this:

```
{
  "private_key_id": "{your_key_id}",
  "private_key": "{your_key}",
  "client_email": "{your_email}@developer.gserviceaccount.com",
  "client_id": "{your_id}.apps.googleusercontent.com",
  "type": "service_account"
}
```


### Setup

Apply the plugin in your `build.gradle`

```
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'co.parish.stephen.publisher:gradle-plugin:1.0'
    }
}

apply plugin: 'android'
apply plugin: 'android-publisher'

android {
    // ...
}

publisher {
    clientSecretsFile = file("client_secrets.json")
}
```


### Sample Tasks and Output

When running `./gradlew tasks` you should see something like (not including your flavors):

*Note: debuggable build types are skipped by default.*

Tasks:

```
Publish tasks
-------------
publishReleaseToGooglePlayAlpha - Publishes 'release' to Google Play Store alpha track
publishReleaseToGooglePlayBeta - Publishes 'release' to Google Play Store beta track
publishReleaseToGooglePlayProduction - Publishes 'release' to Google Play Store production track
```

Output:

```
./gradlew publishReleaseToGooglePlayAlpha
...
Created edit with id: [[UUID]]
Version code 10000 has been uploaded
Track alpha has been updated
App edit with id [[UUID]] has been comitted
```

[play-api]: https://developers.google.com/android-publisher/
[play-api-usage]: https://developers.google.com/android-publisher/api_usage
[fork]: https://help.github.com/articles/fork-a-repo
[branch]: http://learn.github.com/p/branching.html
[pr]: https://help.github.com/articles/using-pull-requests