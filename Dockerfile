FROM openjdk:17-slim

WORKDIR /app

COPY build/libs/dna-0.0.1-SNAPSHOT.jar /app/dna.jar

EXPOSE 3000

CMD ["java", "-jar", "/app/dna.jar"]