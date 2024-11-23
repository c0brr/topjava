[//]: # (MealRestController.getAll)
curl -X GET http://localhost:8080/topjava/rest/meals

[//]: # (MealRestController.get)
curl -X GET http://localhost:8080/topjava/rest/meals/100005

[//]: # (MealRestController.getBetween)
curl -X GET http://localhost:8080/topjava/rest/meals/filter?startTime=13:00

[//]: # (MealRestController.createWithLocation)
curl -X POST -H "Content-Type:application/json" --data "{ \"id\":\"null\",\"dateTime\":\"2024-11-24T21:55:01\",\"description\":\"Ужин\",\"calories\":\"440\"}" http://localhost:8080/topjava/rest/meals/

[//]: # (MealRestController.delete)
curl -X DELETE http://localhost:8080/topjava/rest/meals/100007

[//]: # (MealRestController.update)
curl -X PUT -H "Content-Type:application/json" --data "{ \"id\":\"100005\",\"dateTime\":\"1999-12-12T22:22:22\",\"description\":\"УЖИН\",\"calories\":\"555\"}" http://localhost:8080/topjava/rest/meals/100005