FROM openjdk:21
WORKDIR /app
COPY build/libs/app.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=dev,gcp
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/app/app.jar"]