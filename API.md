# SemanticWebService

## Data Structure

## REST

### **GET**   `physical_spaces/{id}/persons/`
#### Retorna as pessoas que estão no espaço físico XYZ agora e a duração de tempo que estão neste local

 * Full URL: `http://localhost:8080/service/` **`physical_spaces/{id}/persons/`**
 * HTTP Method: **GET**

##### Input Parameters:

| Name     | Payload            |
|----------|--------------------|
| body     | Room JSON          |
|{rooName} | The id of the room |


##### Output Parameters:
| Name      | Value              |
|-----------|--------------------|
| HTTP Code | 200 (OK)           |
| Payload   | Person retrieved   |

Example:

```json
[
    {
        "shortName": "Daniel",
        "email": "daniel@lsdi.ufma.br",
        "mhubID": "399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "duration": 3600
    },
    {
        "shortName": "Alysson",
        "email": "alysson@lsdi.ufma.br",
        "mhubID": "d4d70f9c-b5fa-4a47-8441-e90c8942f6ff",
        "duration": 7200
    }
]
```


### **GET**   `physical_spaces/{id}/persons/{Q}/{W}/`
#### Retorna as pessoas que estiveram no espaço físico XYZ entre o timestamp Q e o timestamp W o tempo dos encontros

 * Full URL: `http://localhost:8080/service/` **`physical_spaces/{id}/persons/{Q}/{W}/`**
 * HTTP Method: **GET**

Example:

`http://localhost:8080/service/physical_spaces/1/persons/1551113150/1551287880/`

```json
[
    {
        "shortName": "Daniel", 
        "email": "daniel@lsdi.ufma.br", 
        "mhubID": "a246df10-7902-3715-9abc-e4148bb97788", 
        "arrive": 1551287871, 
        "depart": 1551287876
    }, 
    {
        "shortName": "Daniel", 
        "email": "daniel@lsdi.ufma.br", 
        "mhubID": "a246df10-7902-3715-9abc-e4148bb97788", 
        "arrive": 1551113153, 
        "depart": 1551113220
    }
]
```


### **GET**   `persons/{id/...}/rendezvous/{Q}/{W}/`
#### Retorna true se a(s) pessoa(s) ABC se encontraram entre o timestamp Q e o timestamp W, e false caso contrário

 * Full URL: `http://localhost:8080/service/` **`persons/{id/...}/rendezvous/{Q}/{W}/`**
 * HTTP Method: **GET**

Example:

`http://localhost:8080/service/persons/1/2/rendezvous/1551113150/1551287880/`

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


### **GET**   `persons/{id/...}/physical_spaces/`
#### Retorna o espaço físico que a(s) pessoa(s) ABC está agora e por quanto tempo

 * Full URL: `http://localhost:8080/service/` **`persons/{id/...}/physical_spaces/`**
 * HTTP Method: **GET**

Example:

`http://localhost:8080/service/persons/1/2/physical_spaces/`

```json
[
    {
        "shortName": "Daniel", 
        "physical_space": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "duration":	3600
    },
    {
        "shortName": "Alyson", 
        "physical_space": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "duration":	7200
    }
]
```


### **GET**   `persons/{id/...}/physical_spaces/{Q}/{W}/`
#### Retorna o espaço físico que a(s) pessoa(s) ABC estiveram entre o timestamp Q e o timestamp W

 * Full URL: `http://localhost:8080/service/` **`persons/{id/...}/physical_spaces/{Q}/{W}/`**
 * HTTP Method: **GET**

Example:

`http://localhost:8080/service/persons/1/2/physical_spaces/1551113150/1551287880/`

```json
[
    {
        "shortName": "Daniel", 
        "physical_space": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "arrive": 1551287871, 
        "depart": 1551287876
    }, 
    {
        "shortName": "Daniel", 
        "physical_space": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "arrive": 1551113153, 
        "depart": 1551113208
    }, 
    {
        "name": "Alyson", 
        "physical_space": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "arrive": 1551113190, 
        "depart": 1551113220
    }
]
```
