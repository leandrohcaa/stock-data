### Deployment

1) Generate backend .jar files using the command `build_jar_files.bat`;
2) Ensure that you have a docker environment running properly and execute the command `clean_and_run.bat`. It will generate the docker images and run the command `docker compose up`; 
3) The web app will be available at http://localhost:3001/. All the required commands to see the final output has been triggered by the web application (e.g.: db creation and data collecting).
4) It was noted that there is a limit for the Alpha Vantage API requests and because of that it was created some static results to ensure that the application will work even when this limit is reached. The backend service has an environment variable "IS_STATIC_RESULTS" to define if the results should come from the static files or the external Api.