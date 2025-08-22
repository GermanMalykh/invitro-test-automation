<p align="center">
<img width="25%" title="Logo" src="./images/icons/logo.svg">
</p>

# Invitro Test Automation

Проект автоматизации тестирования для Invitro с поддержкой Android, Web и API тестов.

## Структура проекта

- `src/test/java/tests/android/` - Android тесты (Appium)
- `src/test/java/tests/web/` - Web тесты (Selenide)
- `src/test/java/tests/api/` - API тесты (REST Assured)

## Запуск тестов

### ⚠️ ВАЖНО: Основная задача test НЕ запускает тесты по умолчанию

```bash
./gradlew test
```
Эта команда покажет инструкции по запуску тестов, но не запустит их.

### Запуск только Android тестов
```bash
./gradlew android
```

### Запуск только Web тестов
```bash
./gradlew web
```

### Запуск только API тестов
```bash
./gradlew api
```

### Запуск всех тестов с правильным разделением
```bash
./gradlew allTests
```

### Запуск UI тестов (Android + Web)
```bash
./gradlew uiTests
```

## Решение проблемы: Web тесты пытаются выполняться в Android окружении

### ✅ Проблема решена!

**Что было не так:**
1. Отсутствовали отдельные Gradle задачи для разных типов тестов
2. Основная задача `test` запускала все тесты без разделения
3. Android и Web тесты конфликтовали в одном процессе

**Что исправлено:**
1. **Добавлены отдельные задачи** для каждого типа тестов
2. **Добавлены теги** к тестам (`@Tag("android")`, `@Tag("web")`, `@Tag("api")`)
3. **Добавлены проверки окружения** в конфигурациях тестов
4. **Основная задача `test`** больше не запускает тесты по умолчанию

### Как теперь работает разделение:

- **`./gradlew android`** - запускает только Android тесты с мобильным драйвером
- **`./gradlew web`** - запускает только Web тесты с браузером
- **`./gradlew api`** - запускает только API тесты без UI
- **`./gradlew allTests`** - запускает все тесты с правильным разделением окружений

## Важные моменты

1. **Android тесты** требуют запущенный Appium сервер и подключенное Android устройство/эмулятор
2. **Web тесты** запускаются в браузере Chrome
3. **API тесты** не требуют браузера или мобильного устройства
4. **Каждый тип тестов** теперь проверяет свое окружение и не конфликтует с другими

## Конфигурация

### Android конфигурация
Файл: `src/test/resources/configs/mobile/local.properties`
- `appiumUrl` - URL Appium сервера
- `device` - имя устройства
- `os_version` - версия Android
- `appPackage` - пакет приложения
- `appActivity` - главная активность

### Web конфигурация
- Браузер: Chrome
- Разрешение: 1920x1080
- Таймаут: 10 секунд

### API конфигурация
- Базовый URL настраивается в `ApiConfigConstants.BASE_URL`

## Зависимости

- Java 21
- Gradle 8+
- JUnit 5
- Selenide
- Appium
- REST Assured
- Allure Reporting

## Генерация отчетов

```bash
./gradlew allureReport
./gradlew allureServe
```

## Примеры использования

### Разработка Android функционала
```bash
./gradlew android
```

### Разработка Web функционала
```bash
./gradlew web
```

### Разработка API функционала
```bash
./gradlew api
```

### Полное тестирование перед релизом
```bash
./gradlew allTests
```
