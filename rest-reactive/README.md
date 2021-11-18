Rest-Reactive
---

# Description

This project implements a microservice for the following problem space: 

* users booking appointments with stores
* Persistent datastore for the state  
* REST API with endpoints for CRUD operations 
* Error handling for error scenarios
* Test Suite for all the valid operations

The business area concerns booking appointments with stores and users. 

* Stores are defined, with a street address and optional country
* Each store defines the appointment slots it supports.
* Users have some basic details
* Appointments are made by matching slots, stores and users.

# Domain Model

| Data entity | Description |
|-------------|---------------|
| Appointment | The booked appointment matching store, store slot and user(s). |
| Store       | The business entity that is providing slots that can be booked. |
| StoreSlot   | The time slot that a store provides. |
| User        | The user entity that can book appointments with stores. |

# REST API Endpoints

The following endpoints are provided


| Http Method | Details |
|-------------|---------------|
| Appointment | The booked appointment matching store, store slot and user(s). |
| GET         | /appointments/{startTime}/{limit} |
| POST        | /appointments/{startTime}/{limit} |
| PUT         | /appointment |
| DELETE      | /appointment/{appointmentCode} |
|-------------|---------------|
| Store       | The business entity that is providing slots that can be booked. |
| GET         | /stores/{limit} |
| GET         | /store/{storeCode} |
| PUT         | /store |
| DELETE      | /store/{storeCode} |
|-------------|---------------|
| StoreSlot   | The time slot that a store provides. |
| GET         | /stores/slots/{startTime}/{limit} |
| POST        | /stores/slots/{startTime}/{limit} |
| PUT         | /store/{storeCode}/slot |
| DELETE      | /store/{storeCode}/slot/{slotCode} |
|-------------|---------------|
| User        | The user entity that can book appointments with stores. |
| GET         | /users/{limit} |
| GET         | /user/{username} |
| PUT         | /user |
| DELETE      | /user/{username} |
|-------------|---------------|

