# league_alerts-ChamplainECP
Project for all 3rd Year Students. We have to get a external client and build them their very first web application. This consists of TypeScript and a Java Spring Boot backend connected to a MongoDB.

## Repo Instructions
This repo has been built with a docker compose with no front-end. To build the front-end run:
```
npm start
```
This is their is a hot reload when running when editing CSS styles for the front-end. 
The team and I also utilized Cloudinary to upload images for our articles. A cloudinary URI is needed and must be added in the following files:
- application.yml
- docker-compose-no-FE.yml

The team and I utilized mongoDB. A URI is also needed and must be provided in the following files:
- application.yml
- docker-compose-no-FE.yml