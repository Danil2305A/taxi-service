FROM eclipse-temurin:21-jdk-ubi10-minimal AS build

WORKDIR /app

COPY build.gradle.kts .
COPY gradlew .
COPY settings.gradle.kts .
COPY gradle/ ./gradle/

RUN ./gradlew dependencies --no-daemon

COPY src/ ./src/

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-ubi10-minimal

RUN groupadd -r danil -g 1000 && \
    useradd -r -g danil -u 1000 -m -s /sbin/nologin danil

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
RUN chown danil:danil app.jar

EXPOSE 8081

USER danil

CMD ["java", "-jar", "app.jar"]