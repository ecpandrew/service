# SemanticWebService

## Data Structure

## REST

### **GET**   `/person/byroom/{roomID}`
#### Retorna as pessoas que estão no espaço físico XYZ agora e a duração de tempo que estão neste local

Example:

`http://localhost:8080/semantic/person/byroom/1/`

```json
[
    {
        "personID":	1,
        "name":		"Daniel",
        "email":	"daniel@lsdi.ufma.br",
        "mhubID":	"399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "duration":	3600
    },
    {
        "personID":	2,
        "name":		"Alysson",
        "email":	"alysson@lsdi.ufma.br",
        "mhubID":	"d4d70f9c-b5fa-4a47-8441-e90c8942f6ff",
        "duration":	7200
    }
]
```


### **GET**   `/person/byroomandperson/{roomID}/{personID}/...`
#### Retorna se a(s) pessoa(s) ABC está(ão) agora no espaço físico XYZ e por quanto tempo

Example:

`http://localhost:8080/semantic/person/byroomandperson/1/1/`

```json
[
    {
        "personID": 1,
        "name":     "Daniel",
        "email":    "daniel@lsdi.ufma.br",
        "mhubID":   "399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "duration": 3600
    }
]
```


### **GET**   `/person/byroomandtime/{roomID}/{Q}/{W}/`
#### Retorna as pessoas que estiveram no espaço físico XYZ entre o timestamp Q e o timestamp W o tempo dos encontros

Example:

`http://localhost:8080/semantic/person/byroomandtime/1/1538956800/1539043200/`

```json
[
    {
        "personID": 1,
        "name":     "Daniel",
        "email":    "daniel@lsdi.ufma.br",
        "mhubID":   "399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "start":    1539007200,
        "end":      1539018000
    }
]
```


### **GET**   `/person/byroomandtimeandperson/{roomID}/{Q}/{W}/{personID}/...`
#### Retorna se a(s) pessoa(s) ABC esteve no espaço XYZ entre o timestamp Q e o timestamp W e o tempo dos encontros

Example:

`http://localhost:8080/semantic/person/byroomandtimeandperson/1/1538956800/1539043200/1/`

```json
[
    {
        "personID": 1,
        "name":     "Daniel",
        "email":    "daniel@lsdi.ufma.br",
        "mhubID":   "399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "start":    1539007200,
        "end":      1539010800
    },
    {
        "personID": 1,
        "name":     "Daniel",
        "email":    "daniel@lsdi.ufma.br",
        "mhubID":   "399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "start":    1539014400,
        "end":      1539018000
    }
]
```


### **GET**   `/room/byperson/{personID}/...`
#### Retorna o espaço físico que a(s) pessoa(s) ABC está agora e por quanto tempo

Example:

`http://localhost:8080/semantic/room/byperson/1/2/`

```json
[
    {
        "roomID":	1,
        "roomName":	"Sala das ETs",
        "name":     "Daniel",
        "email":	"daniel@lsdi.ufma.br",
        "mhubID":	"399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "duration":	3600
    },
    {
        "roomID":	2,
        "roomName":	"Sala das ETs",
        "name":		"Alysson",
        "email":	"alysson@lsdi.ufma.br",
        "mhubID":	"d4d70f9c-b5fa-4a47-8441-e90c8942f6ff",
        "duration":	7200
    }
]
```


### **GET**   `/room/bytimeandperson/{Q}/{W}/{personID}/...`
#### Retorna o espaço físico que a(s) pessoa(s) ABC estiveram entre o timestamp Q e o timestamp W e o tempo dos encontros

Example:

`http://localhost:8080/semantic/room/byperson/1/2/`

```json
[
    {
        "roomID":   1,
        "roomName": "Sala das ETs",
        "name":     "Daniel",
        "email":    "daniel@lsdi.ufma.br",
        "mhubID":   "399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "start":    1539007200,
        "end":      1539018000
    }
]
```
