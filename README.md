### Deployment

1) Generate backend .jar files using the command `./docker/build_jar_files.bat`;
2) Ensure that you have a docker environment running properly and execute the command `./docker/clean_and_run.bat`. It will clear the docker resources and run the command `docker compose up`; 
3) The web app will be available at http://localhost:3001/. There is only one initial created user with username="user_1" and password="pass_1".