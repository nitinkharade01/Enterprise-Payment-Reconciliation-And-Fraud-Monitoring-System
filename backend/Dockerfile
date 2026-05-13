FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY backend ./backend
WORKDIR /workspace/backend
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/backend/*/target/*.jar /app/jars/
ENV SERVICE_NAME=api-gateway
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS:-} -jar /app/jars/${SERVICE_NAME}-1.0.0.jar"]

