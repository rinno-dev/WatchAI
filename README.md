# WatchAI

Wear OS chat application with a Gemini-powered assistant.

WatchAI is an independent project that uses the Gemini API provided by Google. It is not affiliated with, endorsed by, or sponsored by Google. Google and Gemini are referenced only to identify the third-party service.

## Download, configure, and build

### Requirements

- Android Studio (the project uses Android SDK 34)
- JDK 17 or newer. Android Studio includes a compatible JDK; select it under **Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK** if needed.
- A Gemini API key to use the assistant

### 1. Download the source code

Choose one method:

- On GitHub, click **Code → Download ZIP**, then extract the archive.
- Or clone the repository with Git:

```sh
git clone https://github.com/rinno-dev/WatchAI.git
```

### 2. Open the project

In Android Studio, choose **Open** and select the `WatchAI` folder—the one containing `settings.gradle.kts`. Wait for Gradle Sync to finish. If Android Studio asks to install Android SDK 34 or other required components, accept and let it finish.

### 3. Add your Gemini API key

Create a key in [Google AI Studio](https://aistudio.google.com/apikey); see Google's [API key guide](https://ai.google.dev/gemini-api/docs/api-key).

In the project root, open `local.properties`. If the file does not exist, copy `local.properties.example` and name the copy `local.properties`. Add this line, replacing the example value with your own key. If `local.properties` already contains an `sdk.dir` line, keep it:

```properties
GEMINI_API_KEY=your_api_key_here
```

The `local.properties` file is excluded from Git. Each developer must use their own Gemini API key; this repository does not include a shared key.

### 4. Build the app

In Android Studio, choose **Build → Build Bundle(s) / APK(s) → Build APK(s)**. Or use the Gradle wrapper from the project root:

```powershell
.\gradlew.bat :app:assembleDebug
```

On macOS or Linux:

```sh
./gradlew :app:assembleDebug
```

The debug APK is created at `app/build/outputs/apk/debug/app-debug.apk`. You can also run the app directly from Android Studio by selecting a Wear OS emulator or connected watch as the target and clicking **Run**.

### 5. Install on a Wear OS watch

Connect a watch using Android Studio or ADB (USB debugging where supported, or wireless debugging over Wi-Fi). With the watch connected and authorized, run this from the project root:

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Alternatively, copy the APK to the watch and open it there if your transfer method supports APK installation. The watch may require enabling **Install unknown apps** for the app used to open the APK.

The key is embedded in the app during the build and can be extracted from an APK. Do not publish or share an APK containing a personal or production key. A backend proxy is needed to keep a production key private.

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

---

## WatchAI

Приложение для Wear OS с ИИ-ассистентом на базе Gemini.

WatchAI — независимый проект, использующий Gemini API от Google. Проект не связан с Google, не одобрен и не спонсируется компанией. Названия Google и Gemini упоминаются только для обозначения стороннего сервиса.

### Требования

- Android Studio (проект использует Android SDK 34)
- JDK 17 или новее. В Android Studio есть совместимый JDK; при необходимости выберите его в **Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK**.
- API-ключ Gemini для работы ассистента

### 1. Скачайте исходный код

Выберите один из способов:

- На GitHub нажмите **Code → Download ZIP**, затем распакуйте архив.
- Или клонируйте репозиторий через Git:

```sh
git clone https://github.com/rinno-dev/WatchAI.git
```

### 2. Откройте проект

В Android Studio выберите **Open** и укажите папку `WatchAI` — ту, где находится `settings.gradle.kts`. Дождитесь завершения Gradle Sync. Если Android Studio предложит установить Android SDK 34 или другие необходимые компоненты, согласитесь и дождитесь установки.

### 3. Добавьте API-ключ Gemini

Создать ключ можно в [Google AI Studio](https://aistudio.google.com/apikey); инструкция есть в [руководстве Google по API-ключам](https://ai.google.dev/gemini-api/docs/api-key).

В корне проекта откройте `local.properties`. Если файла нет, скопируйте `local.properties.example` и назовите копию `local.properties`. Добавьте строку ниже, заменив пример своим ключом. Если в `local.properties` уже есть строка `sdk.dir`, сохраните её:

```properties
GEMINI_API_KEY=your_api_key_here
```

Файл `local.properties` исключён из Git. Каждому разработчику нужен собственный API-ключ Gemini; общего ключа в репозитории нет.

### 4. Соберите приложение

В Android Studio выберите **Build → Build Bundle(s) / APK(s) → Build APK(s)**. Либо запустите Gradle Wrapper из корня проекта:

```powershell
.\gradlew.bat :app:assembleDebug
```

В macOS или Linux:

```sh
./gradlew :app:assembleDebug
```

Готовый debug APK появится по пути `app/build/outputs/apk/debug/app-debug.apk`. Также можно запустить приложение прямо из Android Studio: выберите эмулятор Wear OS или подключённые часы и нажмите **Run**.

### 5. Установите приложение на часы Wear OS

Подключите часы через Android Studio или ADB (USB-отладка, если поддерживается, либо беспроводная отладка по Wi-Fi). Когда часы подключены и вы подтвердили отладку, выполните команду из корня проекта:

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Другой вариант — скопировать APK на часы и открыть его там, если выбранный способ передачи поддерживает установку APK. Возможно, для приложения, которым вы открываете APK, потребуется разрешить **Установку неизвестных приложений**.

При сборке ключ встраивается в приложение, и его можно извлечь из APK. Не публикуйте и не передавайте APK с личным или рабочим ключом. Чтобы сохранить рабочий ключ в секрете, нужен сервер-посредник.

### Gemini API и конфиденциальность

Приложение отправляет запросы и контекст диалога в Google через Gemini API. История чатов также хранится локально на часах. Обработка данных Google зависит от используемого API-сервиса и актуальных условий. При бесплатном использовании Google может использовать запросы и ответы для улучшения своих продуктов, а проверяющие могут обрабатывать их вручную. Не отправляйте чувствительную, конфиденциальную или персональную информацию.

Согласно актуальным условиям Gemini API, API предназначен для профессионального или делового использования, а не для потребительских приложений. Также запрещены API-клиенты, рассчитанные на аудиторию младше 18 лет или с высокой вероятностью доступные ей. Перед распространением приложения проверьте актуальные условия: [Дополнительные условия Gemini API](https://ai.google.dev/gemini-api/terms). В правилах Google по бренду также сказано, что нельзя создавать впечатление связи с компанией, её одобрения или спонсорства, если их нет: [Google Brand Resource Center](https://about.google/brand-resource-center/guidance/).

### Лицензия

Проект распространяется по лицензии MIT. См. файл `LICENSE`.
