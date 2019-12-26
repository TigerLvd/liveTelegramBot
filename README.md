# liveTelegramBot
Бот сбора статистики и бот отображения информации в формате меню.

Для работы необходимо:
1. в src/main/resources/config.properties указать:
    * bot_name=<имя бота 1>
    * bot_token=<токен бота 1> (если его нет то можно создать нового бота с помощью бота @BotFather)
    * bot_name=<имя бота 2>
    * bot_token=<токен бота 2>
    * proxy_host=<ip-адрес>
    * proxy_port=<порт прокси> (если прокси используется)
2. в src/main/WEB-INF/spring.properties указать настройки для подключения к БД:
    * jdbc.driverClassName=com.mysql.jdbc.Driver
    * jdbc.url=jdbc:mysql://<путь к базе>
    * jdbc.username=<имя пользователя>
    * jdbc.password=<пароль>
    * jdbc.schema=live_ntc