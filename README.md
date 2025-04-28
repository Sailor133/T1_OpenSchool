# T1_OpenSchool
Этот репозиторий был создан для выполнения работ из открытой школы T1

Приложение работает на 8081 порте. Все изменения по конфигурации и подулючению можно менять в application.properties

Тестовые даные по всем эндпоинтам:
1) Получить все таски - GET http://localhost:8081/tasks
2) Получить таск по id - GET http://localhost:8081/tasks/1
3) Создать таск POST http://localhost:8081/tasks
Тело:
{
  "title": "Задача 2",
  "description": "Описание задачи 2",
  "userId": 1
}
4) Обновить таск - PUT http://localhost:8081/tasks/1
Тело:
{
  "title": "Задача 5",
  "description": "Описание задачи 5",
  "userId": 1
}
5) Удалить таск - DELETE http://localhost:8081/tasks/1

