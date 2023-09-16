FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /app
COPY . ./
RUN chmod +x gradlew && ./gradlew build -x test

FROM eclipse-temurin:17-jre-jammy as prod
EXPOSE 8080
COPY --from=build /app/build/libs/photsapp-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=prod"]