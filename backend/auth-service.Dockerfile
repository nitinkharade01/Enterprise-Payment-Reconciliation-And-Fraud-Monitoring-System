FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY backend ./backend

WORKDIR /app/backend

RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/backend/auth-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
