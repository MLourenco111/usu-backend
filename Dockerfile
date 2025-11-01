# ============================================
# Build stage
# ============================================
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copia arquivos essenciais
COPY pom.xml .
COPY src src

# Build da aplicação sem rodar testes
RUN mvn clean package -DskipTests -B

# ============================================
# Runtime stage
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o jar gerado no build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
