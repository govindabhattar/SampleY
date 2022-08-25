# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/cl-mac-mini-3/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn android.support.**

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}


# OkHttp
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**


# Okio
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**


## Square Picasso specific rules ##
## https://square.github.io/picasso/ ##

-dontwarn com.squareup.okhttp.**


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
**[] $VALUES;
public *;
}


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

-keepclassmembers class * {
private <fields>;
}

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.stripe.** { *; }

-dontwarn sun.misc.Unsafe

-keep class com.skeleton.models.** { *; }

-dontwarn rx.**

-keep public class de.hdodenhof.*
-dontwarn org.xmlpull.v1.**
-dontwarn com.github.siyamed.**
-keep class com.github.siyamed.shapeimageview.**{ *; }


-keeppackagenames org.jsoup.nodes

-keep public class org.jsoup.** {
	public *;

}

# proguard
-keep class your.app.data.model.** { *; }
-keep class * implements java.io.Serializable { *; }

# daimajia androidanimations
-dontwarn com.daimajia.**

-keep class com.daimajia.androidanimations.** { *; }
-keep interface com.daimajia.androidanimations.** { *; }

# blurkit
-keep class com.wonderkiln.blurkit.** { *; }

-dontwarn android.support.v8.renderscript.*
-keepclassmembers class android.support.v8.renderscript.RenderScript {
native *** rsn*(...);
native *** n*(...);
}

# loading bar
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

# calligraphy
-keep class uk.co.chrisjenx.calligraphy.* { *; }
-keep class uk.co.chrisjenx.calligraphy.*$* { *; }

# facebook
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepnames class com.facebook.FacebookActivity
-keepnames class com.facebook.CustomTabActivity

-keep class com.facebook.core.Core

-keep public class com.android.vending.billing.IInAppBillingService {
    public static com.android.vending.billing.IInAppBillingService asInterface(android.os.IBinder);
    public android.os.Bundle getSkuDetails(int, java.lang.String, java.lang.String, android.os.Bundle);
}