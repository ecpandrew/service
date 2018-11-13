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

`http://localhost:8080/semantic/person/byroomandtime/Sala%20das%20ETs/1538956800/1539043200/`

```json
[
    {
        "name":     "Daniel",
        "email":    "daniel@lsdi.ufma.br",
        "mhubID":   "399eba78-2346-4c20-81ba-0a8cad5fdd22",
        "start":    1539007200,
        "end":      1539018000
    }
]
```


### **GET**   `/person/rendezvous/{Q}/{W}/{personEmail}/...`
#### Retorna true se a(s) pessoa(s) ABC se encontraram entre o timestamp Q e o timestamp W, e false caso contrário

Example:

`http://localhost:8080/semantic/person/bytimeandperson/1538956800/1539043200/daniel@lsdi.ufma.br/alysson@lsdi.ufma.br/`

```json
[
    {
        "rendezvous":	"true"
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
        "roomID":	1,
        "roomName":	"Sala das ETs",
        "name":     	"Daniel",
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


### **GET**   `/room/bytimeandperson/{Q}/{W}/{personEmail}/...`
#### Retorna o espaço físico que a(s) pessoa(s) ABC estiveram entre o timestamp Q e o timestamp W

Example:

`http://localhost:8080/semantic/room/byperson/daniel@lsdi.ufma.br/alysson@lsdi.ufma.br/`

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
