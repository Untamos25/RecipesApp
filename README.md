# Приложение "Рецепты" / Recipes App

### 1. Описание

Это Android-приложение для просмотра кулинарных рецептов.

**Ключевые возможности:**

*   **Просмотр категорий:** Главный экран отображает список категорий блюд, загружаемых с удаленного сервера.
*   **Каталог рецептов:** Для каждой категории доступен список рецептов с изображениями и названиями.
*   **Детальный экран рецепта:** Полная информация о рецепте, включая список ингредиентов, пошаговый способ приготовления и изображение.
*   **Калькулятор порций:** Интерактивный `SeekBar` для пересчета количества ингредиентов в зависимости от выбранного числа порций.
*   **Избранное:** Возможность добавлять рецепты в избранное и просматривать их на отдельном экране.
*   **Offline-first:** Данные, полученные с сервера, кэшируются в локальной базе данных **Room**, что позволяет пользоваться приложением даже без доступа к сети.

Приложение спроектировано с использованием **Single-Activity** подхода и **Jetpack Navigation Component** для навигации между экранами.

### 2. Используемые технологии

*   **Архитектура и структура:**
    *   MVVM
    *   Repository Pattern
    *   Single-Activity Architecture
    *   Hilt

*   **Пользовательский интерфейс (UI):**
    *   XML Layouts и ViewBinding
    *   Material Design
    *   RecyclerView
    *   Jetpack Navigation Component

*   **Работа с данными и асинхронность:**
    *   Kotlin Coroutines
    *   Retrofit и OkHttp
    *   Room
    *   Kotlinx.Serialization
    *   Glide

### 3. Пример использования

1.  **Главный экран:** Открыв приложение, вы увидите список категорий блюд.
2.  **Список рецептов:** Нажмите на любую категорию, чтобы перейти к списку рецептов, относящихся к ней.
3.  **Просмотр рецепта:** Выберите рецепт из списка, чтобы открыть детальный экран. Здесь вы найдете:
    *   Изображение и название блюда.
    *   Кнопку для добавления в избранное.
    *   Калькулятор порций: двигайте ползунок, чтобы изменить количество порций, и список ингредиентов автоматически пересчитается.
    *   Пошаговый метод приготовления.
4.  **Избранное:** Перейдите во вкладку "Избранное", чтобы увидеть все рецепты, которые вы ранее сохранили.

### 4. Контакты

*   **Имя:** Павлушин Станислав
*   **Email:** [pavlushinsa18@gmail.com](mailto:pavlushinsa18@gmail.com)
*   **Telegram:** [@Untamo](https://t.me/Untamo)

---

### 1. Description

This is an Android application for browsing culinary recipes.

**Key Features:**

*   **Category Browsing:** The main screen displays a list of dish categories loaded from a remote server.
*   **Recipe Catalog:** Each category contains a list of recipes with their corresponding images and titles.
*   **Detailed Recipe View:** A comprehensive screen with full recipe details, including a list of ingredients, step-by-step cooking instructions, and an image.
*   **Servings Calculator:** An interactive `SeekBar` allows users to recalculate ingredient quantities based on the selected number of servings.
*   **Favorites:** Users can add recipes to a favorites list and view them on a separate screen.
*   **Offline-First:** Data fetched from the server is cached in a local **Room** database, allowing the application to be used even without a network connection.

The application is designed using a **Single-Activity** approach with the **Jetpack Navigation Component** for handling navigation between screens.

### 2. Technologies

*   **Architecture & Structure:**
    *   MVVM
    *   Repository Pattern
    *   Single-Activity Architecture
    *   Hilt

*   **User Interface (UI):**
    *   XML Layouts & ViewBinding
    *   Material Design
    *   RecyclerView
    *   Jetpack Navigation Component

*   **Data & Concurrency:**
    *   Kotlin Coroutines
    *   Retrofit & OkHttp
    *   Room for local database
    *   Kotlinx.Serialization
    *   Glide

### 3. Usage Example

1.  **Main Screen:** Upon launching the app, you will see a list of recipe categories.
2.  **Recipe List:** Tap on any category to navigate to a list of recipes within it.
3.  **Recipe View:** Select a recipe from the list to open its detail screen. Here you will find:
    *   The dish's image and title.
    *   A button to add the recipe to your favorites.
    *   A servings calculator: move the slider to adjust the number of servings, and the ingredient quantities will update automatically.
    *   Step-by-step cooking instructions.
4.  **Favorites:** Navigate to the "Favorites" tab to view all the recipes you have saved.

### 4. Contacts

*   **Name:** Pavlushin Stanislav
*   **Email:** [pavlushinsa18@gmail.com](mailto:pavlushinsa18@gmail.com)
*   **Telegram:** [@Untamo](https://t.me/Untamo)
