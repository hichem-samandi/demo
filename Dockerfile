# ---- Étape 1 : Build Maven ----
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Dossier de travail
WORKDIR /app

# Copier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Construire le JAR
RUN mvn clean package -DskipTests

# ---- Étape 2 : Image finale ----
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copier le JAR compilé depuis l'étape précédente
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port de ton API
EXPOSE 8080

# Lancer ton application
ENTRYPOINT ["java", "-jar", "app.jar"]
