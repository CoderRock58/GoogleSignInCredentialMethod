# Sign-In with Credential Manager
- Google Sign-In for Android is outdated and no longer supported. To ensure the continued security and usability of your app, migrate to Credential Manager today. Credential Manager supports passkey, password, and federated identity authentication (such as Sign-in with Google), stronger security, and a more consistent user experience.
# Free to use
- This project is free to use and open source. Any one can use this code in his/her project.
# Set up project at Google Concole Platform
> Open your project in the API Console, or create a project if you don't already have one.
> On the OAuth consent screen page, make sure all of the information is complete and accurate.
- Make sure your app has a correct App Name, App Logo, and App Homepage assigned. These values will be presented to users on the Sign in with Google consent screen on sign up and the Third-party apps & services screen.
- Make sure you have specified the URLs of your app's privacy policy and terms of service.
> In the Credentials page, create an Android client ID for your app if you don't already have one. You will need to specify your app's package name and SHA-1 signature.
- Go to the Credentials page.
- Click Create credentials > OAuth client ID.
- Select the Android application type.
> In the Credentials page, create a new "Web application" client ID if you haven't already. You can ignore the "Authorized JavaScript Origins" and "Authorized redirect URIs" fields for now. This client ID will be used to identify your backend server when it communicates with Google's authentication services.
- Go to the Credentials page.
- Click Create credentials > OAuth client ID.
- Select the Web application type.
# Dependencise
- implementation "androidx.credentials:credentials:<latest_version>"
- implementation "androidx.credentials:credentials-play-services-auth:<latest_version>"
- implementation "com.google.android.libraries.identity.googleid:googleid:<latest_version>"
# Add console key Gradle
- Place your project key to buildConfigField value inside of debug in gradle file before use this code.
