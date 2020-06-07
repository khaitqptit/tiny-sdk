# Sgame SDK Guide
## Description
This is guide and sample app for integrating Sgame SDK 
## I. Config
1. Environment
- Android Studio 3.0
- compileSdkVersion 26

2. Given `google-services.json` and `strings.xml`
- Add `google-services.json` to main module
- Copy values inside `string.xlm` (which contains `app config` and `facebook config`) to `strings.xml` in main module

//Please check image of root folder

3. Update `classpath` and `repositories` build.gradle (`project level`)
```
buildscript {
    dependencies {
        ...
        classpath 'com.google.gms:google-services:3.1.1'
    }
}
```
4. Update `android` and `dependencies` in build.gradle (`app level`)
```
android {
    compileSdkVersion 26
    ...
}
```

```
dependencies {
    ...
    // sgame sdk
    implementation 'vn.tinyhands:sdk:0.0.21'
    // support v13
    implementation 'com.android.support:support-v13:26.1.0'
    // logger library
    implementation 'com.jakewharton.timber:timber:4.6.0'
    // image loader
    implementation 'com.facebook.fresco:fresco:1.9.0'
    // facebook sdk
    implementation 'com.facebook.android:facebook-login:4.28.0'
    // in app purchase lib
    implementation 'com.anjlab.android.iab.v3:library:1.0.44'
    // avoid conflict 26.1.0
    implementation 'com.android.support:cardview-v7:26.1.0'
    implementation 'com.android.support:customtabs:26.1.0'
}

apply plugin: 'com.google.gms.google-services'
```

5. Update `AndroidManifest.xml` : add facebook content provider to support sharing feature (replace `{FACEBOOK_APP_ID}` by given facebook app id)
```
<provider android:authorities="com.facebook.app.FacebookContentProvider{FACEBOOK_APP_ID}"
            android:name="com.facebook.FacebookContentProvider"
            android:exported="true"/>
```

## II. Feature
1. **`SgameSDK.init(Context context)`** : init SDK (`required to call firstly`)
2. **`SgameSDK.login(Activity activity)`** : login

Note: if user login successfully, `REQUEST_CODE_LOGIN` and `access_token` will be returned in `onActivityResult()`
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == SgameSDK.REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
        String userInfoData = data.getStringExtra("user_info");
        UserInfo userInfo = new Gson().fromJson(userInfoData, UserInfo.class);
        Toast.makeText(this, "Login Successful:"
                + ", userId=" + userInfo.getUserId()
                + ", accessToken=" + userInfo.getAccessToken(), Toast.LENGTH_SHORT).show();
        ...
    }
}
```
3. **`SgameSDK.logout(LogoutListener logoutListener)`** : logout
4. **`SgameSDK.openPayment(Activity activity)`** : open payment screen

Note: if user purchase successfully, `REQUEST_CODE_PAYMENT` will be returned in `onActivityResult()`
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == SgameSDK.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK) {
        ...
    }
}
```
5. **`SgameSDK.setUserConfig(String serverId, String characterId, String characterName, String characterLevel)`** : set user configs
6. **`SgameSDK.notify(Context context, String title, String body)`** : create a notify with given `title` and `body`
7. **`SgameSDK.showFloatingButton(Activity activity)`** : create a floating button which help user to open dashboard from it
8. **`SgameSDK.hideFloatingButton()`** : hide the floating button
9. **`SgameSDK.shareLink(Activity activity, String url)`** : share link to Facebook
10. **`SgameSDK.sharePhoto(Activity activity, Bitmap photo)`** : share photo to Facebook
