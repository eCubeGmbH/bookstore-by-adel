# Projekt: Bookstore Learning Project 

Name: Adel Kharbout, Luc Schulze



## Kurzbeschreibung
Das ist eine Spring-Boot Applikation die im Moment ausschließlich Autoren, in eine Liste aufnehmen kann.

## Run the aplication
> Die Applikation kann auf der Kommandozeile durch den Command "mvn spring-boot:run" gestartet werden.

## Calling the Health API
Die Health API kann von der Kommandozeile mit dem "cURL"-Command aufgerufen werden:
curl localhost:9988/actuator/health

## Create a new Author
**Post-Request**
Um einen neuen Autor der Liste hinzu zufügen:

curl --location --request POST 'localhost:9988/api/authors' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data-raw '{
    "name": "string",
    "country": "string",
    "birthDate": "string"
}'

in " --data-raw '{   }' " müssen die jeweiligen Parameter, als String, eingetragen werden.

**Response**
Das ist eine zu erwartende Antwort an dem Beispiel "Friedrich Nietzsche":

{
    "id": "7a5e21a5-7dae-47ff-99a7-79403ba96d67",
    "name": "Friedrich Nietzsche",
    "country": "De",
    "birthDate": "1844-10-15"
}

Die ID ist eine auto-generierte UUID.

## Get a specific Author
**Get-Request**
Um einen spezifischen Autor auszugeben:

curl --location --request GET 'localhost:9988/api/authors/{authorId}' \
--header 'Content-Type: application/json' \
--data-raw ''

in diesem Fall, muss in der URL " {authorId} " durch eine valide ID des gewünschten Autors ersetzt werden.

**Response**
Das ist eine zu erwartende Antwort an dem Beispiel "Friedrich Nietzsche":

{
    "id": "7a5e21a5-7dae-47ff-99a7-79403ba96d67",
    "name": "Friedrich Nietzsche",
    "country": "De",
    "birthDate": "1844-10-15"
}

Falls der Autor nicht gefunden wird:

{
    "timestamp": "2022-09-21T16:29:02.405+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "The author you requested doesn't exist. Please review your parameters!",
    "path": "/api/authors/{authorId}"
}

## Get all Authors
**Get-Request**
Um die gesamte Liste der Autoren auszugeben:

curl --location --request GET 'localhost:9988/api/authors/' \
--header 'Content-Type: application/json'

**Response**
Das ist eine zu erwartende Antwort an dem Beispiel einer Liste mit zwei Objekten:

[
    {
        "id": "3b2f6628-d023-47b1-9f31-5c7ae7117cc5",
        "name": "Franz Kafka",
        "country": "CZ",
        "birthDate": "1883-07-03"
    },
    {
        "id": "edb64885-a5d0-468d-ac3b-d9cc5522d460",
        "name": "Friedrich Nietzsche",
        "country": "De",
        "birthDate": "1844-10-15"
    }
]

Die Liste ist aktuell noch in keiner bestimmten Reihenfolge, und kann auch noch nicht sortiert werden.

Falls die Liste leer ist:

{
    "timestamp": "2022-09-21T16:29:02.405+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "The List you requested appears to be empty. Please add at least one Object before requesting it",
    "path": "/api/authors/{authorId}"
}

## Update an existing Author
**Put-Request**
Um einen existierenden Autor "up-zu-daten":

curl --location --request PUT 'localhost:9988/api/authors/2d038750-6d1e-4a92-b522-cd6b2398bd58' \
--header 'Content-Type: application/json' \
--header 'Accept: application/json' \
--data-raw '{
    "name": "string",
    "country": "string",
    "birthDate": "string"
}'

in " --data-raw '{   }' " müssen die jeweiligen Parameter, als String, eingetragen werden.

**Response**
Das ist eine zu erwartende Antwort an dem Beispiel Franz Kafka:
Vorher ("Franz Schafka"):

{
    "id": "3b2f6628-d023-47b1-9f31-5c7ae7117cc5",
    "name": "Franz Schafka",
    "country": "CZ",
    "birthDate": "1883-07-03"
}

Nachher ("Franz Kafka"):

{
    "id": "3b2f6628-d023-47b1-9f31-5c7ae7117cc5",
    "name": "Franz Kafka",
    "country": "CZ",
    "birthDate": "1883-07-03"
}

Falls der Autor nicht gefunden wird:

{
    "timestamp": "2022-09-21T16:29:02.405+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "The author you requested doesn't exist. Please review your parameters!",
    "path": "/api/authors/{authorId}"
}

## Delete a specific Author
**Delete-Request**
Um einen spezifischen Autor zu löschen:

curl --location --request DELETE 'localhost:9988/api/authors/f757da9c-3bfc-48f3-b51b-13191638653b' \
--header 'Content-Type: application/json' \
--data-raw ''

Falls der Autor nicht gefunden wird:

{
    "timestamp": "2022-09-21T16:29:02.405+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "The author you requested doesn't exist. Please review your parameters!",
    "path": "/api/authors/{authorId}"
}



**Hinweise**: Adel und Luc befinden sich noch in der Ausbildung.
