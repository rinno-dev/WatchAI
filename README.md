# WatchAI

Wear OS chat application with a Gemini-powered assistant.

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
