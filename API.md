# Attendance Service

The Attendance Service is a web service thats takes information from other two web services, Horys and Attendance Service, and provide information about presences and rendezvous of persons in physical spaces.

## Researches

There are three main researches that Attendance Service provides, they are:
* [Presence by Physical Space](#Physical-Spaces)
* [Search for Rendezvous](#Rendezvous)
* [Presence by Persons](#Persons)

## REST API

The Attendance Service uses the base `/service` URL for every REST operation.
Thus, assuming that the server has been deployed at `http://localhost:8080`, all HTTP calls **must** contain `http://localhost:8080/service`. 

## Physical-Spaces

### **GET**   `/physical_spaces/{id}/persons`
#### Retrieve all the persons present now in an existing physical space
This operation retrieves all the persons present now in the physical spaces with id equals `{id}`.
If everything goes well, Attendance Service returns a response with HTTP status code 200 (OK), otherwise it returns a response with HTTP status code 400 (BAD REQUEST).

 * Full URL: `http://localhost:8080/service` **`/physical_spaces/{id}/persons`**
 * HTTP Method: **GET**

##### Input Parameters:

| Name     | Value                        |
|----------|------------------------------|
| id       | The id of the physical space |

Example: `http://localhost:8080/service/physical_spaces/3/persons/`

##### Output Parameters:
| Name      | Value              |
|-----------|--------------------|
| HTTP Code | 200 (OK)           |
| Payload   | Array of Persons   |

Example:

```json
[
    {
        "shortName": "Daniel Carvalho",
        "email": "daniel.carvalho@lsdi.ufma.br",
        "physical_space": "LSDi",
        "duration": 3600
    },
    {
        "shortName": "André Luiz",
        "email": "andreluizalmeidacardoso@gmail.com",
        "physical_space": "Sala das ETs",
        "duration": 7200
    }
]
```


### **GET**   `/physical_spaces/{id}/persons/{Q}/{W}`
#### Retrieve all the persons present in an existing physical space in a given time
This operation retrieves all the persons present in the physical spaces with id equals `{id}` in the time interval `{Q}` to `{W}`.
If everything goes well, Attendance Service returns a response with HTTP status code 200 (OK), otherwise it returns a response with HTTP status code 400 (BAD REQUEST).

 * Full URL: `http://localhost:8080/service` **`/physical_spaces/{id}/persons/{Q}/{W}`**
 * HTTP Method: **GET**

##### Input Parameters:

| Name     | Value                        |
|----------|------------------------------|
| id       | The id of the physical space |
| Q        | Initial timestamp            |
| W        | Final timestamp              |

Example: `http://localhost:8080/service/physical_spaces/3/persons/1558764156/1559764156/`

##### Output Parameters:
| Name      | Value              |
|-----------|--------------------|
| HTTP Code | 200 (OK)           |
| Payload   | Array of Persons   |

Example:

```json
[
    {
        "shortName": "Daniel Carvalho",
        "email": "daniel.carvalho@lsdi.ufma.br",
        "physical_space": "LSDi",
        "arrive": 1559007804,
        "depart": 1559007837
    },
    {
        "shortName": "André Luiz",
        "email": "andreluizalmeidacardoso@gmail.com",
        "physical_space": "Sala das ETs",
        "arrive": 1559332894,
        "depart": 1559332904
    }
]
```

## Rendezvous

### **GET**   `/persons/{id/...}/rendezvous/{Q}/{W}`
#### Retrieve a group of person who have met in a physical space in a given time
This operation retrieves a group of persons who were present in a physical space in the same time lapse with id equals `{id}` in the time interval `{Q}` to `{W}`.
If everything goes well, Attendance Service returns a response with HTTP status code 200 (OK), otherwise it returns a response with HTTP status code 400 (BAD REQUEST).

 * Full URL: `http://localhost:8080/service` **`/persons/{id/...}/rendezvous/{Q}/{W}`**
 * HTTP Method: **GET**

##### Input Parameters:

| Name     | Value                      |
|----------|----------------------------|
| id/...   | The id array of Persons    |
| Q        | Initial timestamp          |
| W        | Final timestamp            |

Example: `http://localhost:8080/service/persons/1/2/rendezvous/1551113150/1551287880/`

##### Output Parameters:
| Name      | Value               |
|-----------|---------------------|
| HTTP Code | 200 (OK)            |
| Payload   | Array of Rendezvous |

Example:

```json
[
    {
        "physical_space": "Sala das ETs", 
        "persons": [
            {
                "shortName": "Alyson"
            }, 
            {
                "shortName": "Daniel"
            }
        ], 
        "arrive": 1551113190, 
        "depart": 1551113208
    }
]
```

## Persons

### **GET**   `/persons/{id/...}/physical_spaces`
#### Retrieve the physical space where the defined persons are present now
This operation retrieves all the physical spaces where the existing persons with id equals the array `{id/...}` are present.
If everything goes well, Attendance Service returns a response with HTTP status code 200 (OK), otherwise it returns a response with HTTP status code 400 (BAD REQUEST).

 * Full URL: `http://localhost:8080/service` **`/persons/{id/...}/physical_spaces`**
 * HTTP Method: **GET**

##### Input Parameters:

| Name     | Value                      |
|----------|----------------------------|
| id/...   | The id array of Persons    |

Example: `http://localhost:8080/service/persons/1/2/physical_spaces/`

##### Output Parameters:
| Name      | Value                    |
|-----------|--------------------------|
| HTTP Code | 200 (OK)                 |
| Payload   | Array of Physical Spaces |

Example:

```json
[
    {
        "shortName": "Daniel", 
        "physical_space": "Sala das ETs", 
        "description": "Sala das Estações de Trabalho do LSDi", 
        "duration":	3600
    },
    {
        "shortName": "Alyson", 
        "physical_space": "Sala das ETs", 
        "description": "Sala das Estações de Trabalho do LSDi", 
        "duration":	7200
    }
]
```


### **GET**   `persons/{id/...}/physical_spaces/{Q}/{W}/`
#### Retrieve the physical space where the defined persons were present in a given time
This operation retrieves all the physical spaces where the existing persons with id equals the array `{id/...}` were present in the time interval `{Q}` to `{W}`.
If everything goes well, Attendance Service returns a response with HTTP status code 200 (OK), otherwise it returns a response with HTTP status code 400 (BAD REQUEST).

 * Full URL: `http://localhost:8080/service/` **`persons/{id/...}/physical_spaces/{Q}/{W}/`**
 * HTTP Method: **GET**

##### Input Parameters:

| Name     | Value                      |
|----------|----------------------------|
| id/...   | The id array of Persons    |
| Q        | Initial timestamp          |
| W        | Final timestamp            |

Example: `http://localhost:8080/service/persons/1/2/physical_spaces/1551113150/1551287880/`

##### Output Parameters:
| Name      | Value                    |
|-----------|--------------------------|
| HTTP Code | 200 (OK)                 |
| Payload   | Array of Physical Spaces |

Example:

```json
[
    {
        "shortName": "Daniel", 
        "physical_space": "Sala das ETs", 
        "description": "Sala das Estações de Trabalho do LSDi", 
        "arrive": 1551287871, 
        "depart": 1551287876
    }, 
    {
        "shortName": "Daniel", 
        "physical_space": "Sala das ETs", 
        "description": "Sala das Estações de Trabalho do LSDi", 
        "arrive": 1551113153, 
        "depart": 1551113208
    }, 
    {
        "name": "Alyson", 
        "physical_space": "Sala de Reuniões", 
        "description": "Sala de Reuniões do LSDi",
        "arrive": 1551113190, 
        "depart": 1551113220
    }
]
```
