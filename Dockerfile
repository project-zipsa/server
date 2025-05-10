FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY src/main/resources/keystore.p12 /app/keystore.p12
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.ssl.key-store=file:/app/keystore.p12", "-Dserver.ssl.key-store-password=${KEYSTORE_PASSWORD}", "-Dserver.ssl.key-store-type=PKCS12", "-jar", "app.jar"]
