# 🚀 Быстрый старт с Allure 3

## ✅ Что уже настроено

- ✅ Allure 3 CLI установлен
- ✅ Gradle задачи созданы
- ✅ npm скрипты настроены
- ✅ Демо тесты созданы
- ✅ Конфигурация готова

## 🎯 Запуск тестов и генерация отчетов

### 1. Запуск всех тестов
```bash
./gradlew test
```

### 2. Генерация отчетов Allure 3

#### Через npm (рекомендуется):
```bash
# Все отчеты сразу
npm run allure:all

# Или по отдельности:
npm run allure:classic      # Классический отчет
npm run allure:awesome      # Awesome отчет
npm run allure:log          # Консольный лог

# Генерация + открытие в браузере:
npm run allure:classic:open # Classic отчет + открытие
npm run allure:awesome:open # Awesome отчет + открытие

# Генерация + локальный сервер:
npm run allure:serve:classic # Classic отчет + сервер
npm run allure:serve:awesome # Awesome отчет + сервер
```

#### Через Gradle:
```bash
./gradlew allure3Classic        # Классический отчет
./gradlew allure3Awesome        # Awesome отчет
./gradlew allure3Log            # Консольный лог

# Генерация + открытие в браузере:
./gradlew allure3ClassicOpen    # Classic отчет + открытие
./gradlew allure3AwesomeOpen    # Awesome отчет + открытие

# Локальный сервер:
./gradlew allure3ServeClassic   # Запуск сервера
```

### 3. Запуск только демо тестов
```bash
./gradlew test --tests "tests.allure.Allure3DemoTest"
```

## 📊 Результаты

После выполнения отчеты будут доступны в:
- `build/allure-classic-report/` - Классический отчет
- `build/allure-awesome-report/` - Awesome отчет
- `build/allure-log/console.log` - Консольный лог

## 🌐 Локальный сервер

Для просмотра результатов в браузере:
```bash
npm run allure:serve
```

## 🔧 Устранение неполадок

### Если тесты не запускаются:
1. Проверьте версию Java: `java -version` (должна быть 21)
2. Проверьте версию Gradle: `./gradlew --version` (должна быть 8.11+)
3. Очистите кэш: `./gradlew clean`

### Если отчеты не генерируются:
1. Проверьте, что тесты прошли: `./gradlew test`
2. Проверьте папку `build/allure-results/`
3. Переустановите зависимости: `npm install`

## 📝 Примеры использования

### Запуск тестов + генерация отчета
```bash
./gradlew test && npm run allure:classic
```

### Генерация всех типов отчетов
```bash
npm run allure:all
```

### Генерация + открытие в браузере
```bash
# Classic отчет
npm run allure:classic:open

# Awesome отчет  
npm run allure:awesome:open

# Через Gradle
./gradlew allure3ClassicOpen
```

### Просмотр логов "на лету"
```bash
# Показать актуальный лог сразу
npm run log:now

# Или короткая команда
npm run log:live

# Генерация + просмотр лога
npm run allure:log:live
```

### Локальный сервер с отчетом
```bash
# Classic отчет + сервер
npm run allure:serve:classic

# Awesome отчет + сервер
npm run allure:serve:awesome

# Через Gradle
./gradlew allure3ServeClassic
```

## 🎉 Готово!

Теперь у вас есть полноценная интеграция с Allure 3! 🚀
