# WatchAI

Wear OS chat application with a Gemini-powered assistant.

WatchAI is an independent project that uses the Gemini API provided by Google. It is not affiliated with, endorsed by, or sponsored by Google. Google and Gemini are referenced only to identify the third-party service.

## Requirements

- Android Studio with Android SDK 34
- JDK 17 or newer
- A Gemini API key

## Configure the API key

Add the key to the root `local.properties` file. That file is excluded from Git:

Create a key in [Google AI Studio](https://aistudio.google.com/apikey); see Google's [API key guide](https://ai.google.dev/gemini-api/docs/api-key).

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

---

## WatchAI

Приложение для Wear OS с ИИ-ассистентом на базе Gemini.

WatchAI — независимый проект, использующий Gemini API от Google. Проект не связан с Google, не одобрен и не спонсируется компанией. Названия Google и Gemini упоминаются только для обозначения стороннего сервиса.

### Требования

- Android Studio с Android SDK 34
- JDK 17 или новее
- API-ключ Gemini

### Настройка API-ключа

Добавьте ключ в корневой файл `local.properties`. Этот файл исключён из Git:

Создать ключ можно в [Google AI Studio](https://aistudio.google.com/apikey); инструкция есть в [руководстве Google по API-ключам](https://ai.google.dev/gemini-api/docs/api-key).

```properties
GEMINI_API_KEY=your_api_key_here
```

Каждому разработчику нужен собственный API-ключ Gemini. В репозитории общего ключа нет.

При сборке ключ встраивается в приложение, и его можно извлечь из APK. Не публикуйте APK с личным или рабочим ключом. Чтобы сохранить рабочий ключ в секрете, нужен сервер-посредник.

### Gemini API и конфиденциальность

Приложение отправляет запросы и контекст диалога в Google через Gemini API. История чатов также хранится локально на часах. Обработка данных Google зависит от используемого API-сервиса и актуальных условий. При бесплатном использовании Google может использовать запросы и ответы для улучшения своих продуктов, а проверяющие могут обрабатывать их вручную. Не отправляйте чувствительную, конфиденциальную или персональную информацию.

Согласно актуальным условиям Gemini API, API предназначен для профессионального или делового использования, а не для потребительских приложений. Также запрещены API-клиенты, рассчитанные на аудиторию младше 18 лет или с высокой вероятностью доступные ей. Перед распространением приложения проверьте актуальные условия: [Дополнительные условия Gemini API](https://ai.google.dev/gemini-api/terms). В правилах Google по бренду также сказано, что нельзя создавать впечатление связи с компанией, её одобрения или спонсорства, если их нет: [Google Brand Resource Center](https://about.google/brand-resource-center/guidance/).

### Сборка

В Windows:

```powershell
.\gradlew.bat :app:assembleDebug
```

В macOS или Linux:

```sh
./gradlew :app:assembleDebug
```

Готовый debug APK будет в `app/build/outputs/apk/debug/app-debug.apk`.

### Лицензия

Проект распространяется по лицензии MIT. См. файл `LICENSE`.
