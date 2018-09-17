SmattNews
==========

This is a simple API Service built with Spring Boot for creating and consuming of news / articles. 

It demonstrates how Spring Security can be used to authenticate API routes.
 
Response Format
===============

All API response has a `status` key, in addition to standard HTTP Status Codes, that'll report if the operation is successful or not
 
Running Locally
===============
To run this app locally, clone the repo, create a local MySQL database and 

configure the database connection in   `application.properties`

Then run from the root directory of the project:

`./mvnw clean && mvn compile && mvn package && java -jar "./target/smattnews-1.0-SNAPSHOT.jar"`

 API Doc
 =======
 
 BaseURL on Heroku: `https://smattnews.herokuapp.com/`
 
 BaseURL on localhost: `http://localhost:9000/`
 
 All other URLs mentioned below are relative to the baseURL stated above. To test on Heroku, use the Heroku BaseURL otherwise, use the one for localhost.
 
 Authentication
 ---------------
 The authentication flow requires a call to the `/login` endpoint with email and password. 
 
 If successful, the response will contain **token** which should be included in the header
  
 of subsequent request as:
  
 **Authentication: token** e.g. `Authentication: taA0o6ASv3vp8yO`
 
 **Any request to a secure endpoint that does not supply the Authentication header with the correct token will be deemed unauthenticated**
  
  All routes with asterisks (*) requires Authentication token.
 
 Content Negotiation and Response Format
 ----------------------------------------
 The API supports JSON and XML response format - with the default being JSON.
 
 To get an XML response, supply the header:
 
 `Accept: application/xml`
 
 To get a JSON response, supply the header:
 
 `Accept: application/json`
 
 **NOTE:** All `POST`, `PATCH` AND `DELETE` requests require the `Content-Type` to be `application/json`
 and not `formdata` or any other.
 
 Below is a list of available endpoints, their HTTP Method, Request Parameters and Sample success response.
 
 GET `/`
 ------
  The home url will give result like this:
  
  ```json
  {
      "message": "Welcome to SmattNews! An API service for reading the latest news by our amazing authors",
      "status": true
  }
  ```
  
  GET `/news`
  -----------
  List all the available news. Supports pagination via query params. e.g. `/news?limit=5&offset=1` will return five results and skip the first five results
  
 ```json
{
    "meta": {
        "total": 2,
        "offset": 0,
        "limit": 100
    },
    "posts": [
        {
            "post": "This is a sample post",
            "author": "Seun Matt",
            "created_at": "2018-09-15T21:38:45.000+0000",
            "id": "HFVVLJKAYS",
            "title": "SmattNews Launched",
            "url": "smattnews-launched",
            "tags": null
        },
        {
            "post": "This is a sample post updated",
            "author": "Seun Matt",
            "created_at": "2018-09-15T21:49:17.000+0000",
            "id": "9Z0IDJYHWX",
            "title": "SmattNews Launched 2 Updated",
            "url": "smattnews-launching",
            "tags": null
        }
    ],
    "status": true
}
```
  
  GET `/news/{id}` e.g. `/news/HFVVLJKAYS` || `/news/{url}` e.g. `/news/smattnews-sample-two`
  -------------------------------------------------------------------------------------------
  Return the details of a single news article. The article url or id can be supplied:
  
  ```json
{
    "post": {
        "post": "This is a sample post",
        "updated_at": "2018-09-15T21:38:45.000+0000",
        "author": "Seun Matt",
        "created_at": "2018-09-15T21:38:45.000+0000",
        "id": "HFVVLJKAYS",
        "title": "SmattNews Launched",
        "url": "smattnews-launched",
        "tags": null
    },
    "status": true
}
```

 POST `/login`
 --------------
 This is the route for log in. On success, it will return an authentication
 token that'll be supply as Authorization header in subsequent requests.
 
 Request Param:
 ```json
{
	"email": "test@test.com",
	"password" : "test123"
}
```

Response:

```json
{
    "message": "Login Successful",
    "status": true,
    "token": "YitOlZ85kLwhIQf"
}
```

POST `/register`
----------------
This will register a new user that can create/update/delete articles.

Request Param:

```json
{
	"email" : "test8@test.com",
	"password":"test123",
	"first_name":"Seun",
	"last_name" :"Matt",
	"password_confirmation" : "test123",
	"bio" : "This is a simple Bio for a user"
}
```

Response:

```json
{
    "message": "Registration Successful! Login with your credentials",
    "status": true
}
```

\* GET`/app/users`
-------------------
This will list all the registered users on the system. It supports limit and offset keys for pagination e.g. `/app/users?limit=5&offset=1` will return skip the first five results and return max of five results

Response:

```json
{
    "meta": {
        "total": 3,
        "offset": 0,
        "limit": 2
    },
    "users": [
        {
            "registered_on": "2018-09-15T21:30:23.000+0000",
            "last_name": "Matt",
            "bio": null,
            "id": "QCQWPLROGG",
            "first_name": "Seun",
            "email": "test@test.com"
        },
        {
            "registered_on": "2018-09-17T12:22:23.000+0000",
            "last_name": "Matt",
            "bio": "This is a simple Bio for a user",
            "id": "Q5NHBQCCFR",
            "first_name": "Seun",
            "email": "test8@test.com"
        }
    ],
    "status": true
}
```

\* POST `app/post`
-------------------
Create a new post.

Request Param:

```json
{
	"post" : "This is a sample post",
	"title" : "SmattNews Company",
	"url" : "smattnews-intro",
	"tags": "Startup, Awesomeness"
}
```

The `url` and `tags` entry are optional.

Response:

```json
{
    "message": "New Post Created Successfully",
    "status": true
}
```

\* PATCH `/app/post/{id}` e.g. `/app/post/9Z0IDJYHWX`
--------------------------------------------------
This will update a post whose id is specified in the URL.

The Request payload can be any of the above listed params for creating a post.

A post can only be updated by it's author.

Response:

```json
{
    "message": "Post Updated Successfully",
    "status": true
}
```
  
  
\* DELETE `/app/post/{id}` e.g. `/app/post/FA2HIEIOYO` 
----------------------------------------------------
 This will delete a post whose id is specified in the URL. 
 
 A post can only be deleted by the author.
 
 Response:
 
 ```json
{
    "message": "Post deleted successfully",
    "status": true
}
```
 
 AUTHOR
 =======
 Seun Matt (https://smattme.com)
  
 BUGS
 ====
 Found a bug? Kindly create an issue for it
 
 CONTRIBUTIONS
 =============
 Submit your contributions via GitHub Pull Requests.