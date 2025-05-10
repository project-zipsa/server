# 빌드 단계
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar

# 실행 단계
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY keystore.p12 /app/keystore.p12
EXPOSE 8080
ENTRYPOINT ["java", "-Dserver.ssl.key-store=file:/app/keystore.p12", "-Dserver.ssl.key-store-password=zipsa1234!!", "-Dserver.ssl.key-store-type=PKCS12", "-jar", "app.jar"]
