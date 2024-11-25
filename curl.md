Get all meals: 
curl -X GET http://localhost:8080/topjava/rest/meals

Get a meal by its ID: 
curl -X GET http://localhost:8080/topjava/rest/meals/100005

Get meals filtered by date and time. Both dates and start time are included, end time is excluded: 
curl -X GET http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=13:00&endDate=2020-01-31&endTime=21:00

Create a new meal: 
curl -X POST -H "Content-Type:application/json" --data "{ \"id\":\"null\",\"dateTime\":\"2024-11-24T21:55:01\",\"description\":\"Ужин\",\"calories\":\"440\"}" http://localhost:8080/topjava/rest/meals/

Delete a meal by its ID: 
curl -X DELETE http://localhost:8080/topjava/rest/meals/100007

Update a meal by its ID:
curl -X PUT -H "Content-Type:application/json" --data "{ \"id\":\"100005\",\"dateTime\":\"1999-12-12T22:22:22\",\"description\":\"УЖИН\",\"calories\":\"555\"}" http://localhost:8080/topjava/rest/meals/100005