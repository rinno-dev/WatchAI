# WatchAI

Wear OS chat application with a Gemini-powered assistant.

WatchAI is an independent project that uses the Gemini API provided by Google. It is not affiliated with, endorsed by, or sponsored by Google. Google and Gemini are referenced only to identify the third-party service.

## Requirements

- Android Studio with Android SDK 34
- JDK 17 or newer
- A Gemini API key

## Configure the API key

Add the key to the root `local.properties` file. That file is excluded from Git:

```properties
GEMINI_API_KEY=your_api_key_here
```

Each developer must use their own Gemini API key. This repository does not include a shared key.

The key is embedded in the app during the build and can be extracted from an APK. Do not publish an APK containing a personal or production key. A backend proxy is needed to keep a production key private.

## Gemini API and privacy

The app sends prompts and conversation context to Google through the Gemini API. Chat history is also stored locally on the watch. Google's handling of submitted content depends on the API service and current terms. For unpaid services, Google may use prompts and responses to improve its products and human reviewers may process them; do not send sensitive, confidential, or personal information.

Google's current Gemini API terms limit API use to professional or business purposes, not consumer use, and prohibit API clients directed to or likely to be accessed by people under 18. Check the current terms before distributing or offering this app to users: [Gemini API Additional Terms](https://ai.google.dev/gemini-api/terms). Google's branding guidance also says not to imply affiliation, endorsement, or sponsorship where none exists: [Google Brand Resource Center](https://about.google/brand-resource-center/guidance/).

## Build

On Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

On macOS or Linux:

```sh
./gradlew :app:assembleDebug
```

The debug APK is created at `app/build/outputs/apk/debug/app-debug.apk`.

## License

This project is licensed under the MIT License. See `LICENSE`.
