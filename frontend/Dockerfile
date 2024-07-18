FROM node:16 AS build
WORKDIR /app
COPY /package.json /package-lock.json ./
RUN npm install
COPY . .
RUN npm run build
FROM nginx:1.21
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 3001
CMD ["nginx", "-g", "daemon off;"]