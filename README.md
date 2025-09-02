# Демо-проект для автоматизации сервиса

<p align="left">
  <img src="images/icons/logo.svg" width="150">
</p>

> **Invitro** — крупнейшая в России частная медицинская компания, предоставляющая услуги лабораторной диагностики, анализов и других медицинских исследований.  
> Цель проекта — автоматизировать тестирование ключевых сервисов: **Android-приложение**, **WEB-интерфейс**, **API**.

---

# <a name="Описание">Описание</a>

Автоматизированный тестовый проект покрывает три направления: **Android**, **Web** и **API**.

Основные особенности проекта:

- [x] Поддержка паттерна **Page Object**
- [x] Разделение тестов по окружениям (**Android / Web / API**)
- [x] Использование **тегов JUnit 5** (`@Tag("android")`, `@Tag("web")`, `@Tag("api")`)
- [x] Кастомный **Allure listener** для визуализации API-запросов/ответов
- [ ] Интеграция с **Allure Report** и **Allure TestOps**
- [ ] Интеграция с **Jenkins** и возможность запуска тестов по категориям

---

# <a name="Технологии и инструменты">Технологии и инструменты</a>

## 🛠 Стек технологий

**Язык и сборка**
- Java
- Gradle

**Фреймворки для тестов**
- JUnit 5
- Selenide (Web UI)
- Appium (Android)
- REST Assured (API)

**Инфраструктура и CI/CD**
- Jenkins
- GitHub
- Android Studio

**Отчётность и аналитика**
- Allure Report
- Allure TestOps

**Коммуникации**
- Telegram (бот для уведомлений)

---

# <a name="Запуск">Как запустить тесты</a>

## Gradle-команды

### Запуск отдельных типов тестов

```bash
./gradlew api                # API тесты
./gradlew androidLocalTests  # Android тесты локально
./gradlew androidRemoteTests # Android тесты удалённо
./gradlew webLocalTests      # Web тесты локально
./gradlew webRemoteTests     # Web тесты удалённо
```