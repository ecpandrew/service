# SemanticWebService

## Data Structure

## REST

### **GET**   `/person/byroom/{roomName}`
#### Retorna as pessoas que estão no espaço físico XYZ agora e a duração de tempo que estão neste local

Example:

`http://localhost:8080/service/webresources/get/person/byroom/Sala%20das%20ETs/`

```json
[
    {
        "name":		"Daniel",
        "email":	"daniel@lsdi.ufma.br",
        "mhubID":	"399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "duration":	3600
    },
    {
        "name":		"Alysson",
        "email":	"alysson@lsdi.ufma.br",
        "mhubID":	"d4d70f9c-b5fa-4a47-8441-e90c8942f6ff",
        "duration":	7200
    }
]
```


### **GET**   `/person/byroomandtime/{roomName}/{Q}/{W}/`
#### Retorna as pessoas que estiveram no espaço físico XYZ entre o timestamp Q e o timestamp W o tempo dos encontros

Example:

`http://localhost:8080/semantic/person/byroomandtime/Sala%20das%20ETs/1551113150/1551287880/`

```json
[
    {
        "name": "Daniel", 
        "email": "daniel@lsdi.ufma.br", 
        "mhubID": "a246df10-7902-3715-9abc-e4148bb97788", 
        "arrive": 1551287871, 
        "depart": 1551287876
    }, 
    {
        "name": "Daniel", 
        "email": "daniel@lsdi.ufma.br", 
        "mhubID": "a246df10-7902-3715-9abc-e4148bb97788", 
        "arrive": 1551113153, 
        "depart": 1551113220
    }
]
```


### **GET**   `/person/rendezvous/{Q}/{W}/{personEmail}/...`
#### Retorna true se a(s) pessoa(s) ABC se encontraram entre o timestamp Q e o timestamp W, e false caso contrário

Example:

`http://localhost:8080/semantic/person/rendezvous/1551113150/1551287880/daniel@lsdi.ufma.br/alysson@lsdi.ufma.br`

```json
[
    {
        "room": "Sala das ETs", 
        "person": [
            {
                "name": "Alyson"
            }, 
            {
                "name": "Daniel"
            }
        ], 
        "arrive": 1551113190, 
        "depart": 1551113208
    }
]
```


### **GET**   `/room/byperson/{personEmail}/...`
#### Retorna o espaço físico que a(s) pessoa(s) ABC está agora e por quanto tempo

Example:

`http://localhost:8080/semantic/room/byperson/daniel@lsdi.ufma.br/alysson@lsdi.ufma.br/`

```json
[
    {
        "name": "Daniel", 
        "room": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "duration":	3600
    },
    {
        "name": "Alyson", 
        "room": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "duration":	7200
    }
]
```


### **GET**   `/room/bypersonandtime/{Q}/{W}/{personEmail}/...`
#### Retorna o espaço físico que a(s) pessoa(s) ABC estiveram entre o timestamp Q e o timestamp W

Example:

`http://localhost:8080/semantic/room/bypersonandtime/1551113150/1551287880/daniel@lsdi.ufma.br/alysson@lsdi.ufma.br`

```json
[
    {
        "name": "Daniel", 
        "room": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "arrive": 1551287871, 
        "depart": 1551287876
    }, 
    {
        "name": "Daniel", 
        "room": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "arrive": 1551113153, 
        "depart": 1551113208
    }, 
    {
        "name": "Alyson", 
        "room": "Sala das ETs", 
        "thingID": "f80638aa-e7d7-3f8b-b538-aa8f78cce93b", 
        "arrive": 1551113190, 
        "depart": 1551113220
    }
]
```
