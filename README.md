# Chat-api

 Это репозиторий RESTfull API на языке Java (Spring Framework), с использованием базы данных PostgreSQL и контейнера Docker.

### Spring Framework

Сервер реализует запросы, указанные в ТЗ, и дополняет их, а также реализует другие запросы, такие как: регистрация пользователя, выход из аккаунта, создание новых диалогов и другие. Созданный сервер полнотью самостоятелен и не зависит от общего API, данного в ТЗ.
API хранит файлы, отправляемые в сообщении и предоставляет ссылку на них для загрузки + может создавать несколько диалогов для каждого пользователя (мессенджер) + производит аунтеикацию. Также, сервер полностью синхронизирован и обрабатывает большинство ошибок.

#### Техническое Задание - https://documenter.getpostman.com/view/7834747/2s8YepsXyp

### PostgreSQL

База данных содержит всего 3 таблицы: Пользователь(User), Сообщение(Message) и Диалог(Dialog). Первая хранит все характеристики пользователя, нужные для поиска нужного диалога и создания токена. Message хранит информацию о сообщении, а также ссылку на файл. Dialog хранит все чаты приложения, а также даёт возможность найти чаты определённого пользователя и сообщения чата с каким-либо пользователем

## API Download

Можно установить изображение серверной части с Docker по ссылке: https://hub.docker.com/repository/docker/wtkeqrf0/chat-api
