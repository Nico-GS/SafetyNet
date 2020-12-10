<h2>SafetyNet Alerts</h2>

SafetyNet Alerts is an application that sends essential information to emergency services

<h2>API</h2>

Java 1.8 -
Maven -
Spring Boot

URL : http://localhost:8080/

Properties : src/main/resources/application.properties

<h2>Data File JSON</h2>

Source : src/main/resources/data/data.json

<code>
"persons":[

{
"firstName": "John",
"lastName": "Boyd",
"address": "1509 Culver St",
"city": "Culver",
"zip": "97451",
"phone": "841-874-6512",
"email": "jaboyd@email.com"
},</code>

<h2>Endpoints</h2>
<h3>GET</h3>

http://localhost:8080/firestation?stationNumber={station_number}

Return list of people covered by FireStation

http://localhost:8080/childAlert?address={address}

Return list of child(s) living at the address

http://localhost:8080/phoneAlert?firestation={firestation_number}

Return list of telephone number of the habitants covered by FireStation

http://localhost:8080/personInfo?firstName={firstName}&lastName={lastName}

Return : Name - Age - Address - Email - MedicalRecord of a person

http://localhost:8080/communityEmail?city={city}

Return list of emails of all the habitants by city

http://localhost:8080/flood/stations?stations={list_of_station_numbers}

Return list of households covered by station

http://localhost:8080/fire?address={address}

Return list of habitants covered FireStation number : Name - Phone - Age - MedicalRecord


<h2>POST / PUT / DELETE</h2>


<h3>JSON Body for /person</h3>

<code>
{ "firstName":"Marcel", 
"lastName":"Denis", 
"address":"1509 Culver St", 
"city":"Culver", "zip":"97451", 
"phone":"0642876413", 
"email":"nicolas@email.com" }
</code>

<h3>JSON Body for /firestation</h3>

<code>
{ "address":"Rue de la Paix", "station":"2" }
</code>

<h3>JSON Body for /medicalRecord</h3>

<code>
{ "firstName":"John", "lastName":"Boyd", "birthdate":"03/03/1993", "medications":["aznol:350mg", "hydrapermazol:100mg"], "allergies":["nillacilan"] }</code>


