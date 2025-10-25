# ============================================
# Build stage
# ============================================
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copia arquivos do Maven Wrapper
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Garante permissão de execução para o mvnw
RUN chmod +x mvnw

# Copia o código-fonte
COPY src src

# Build da aplicação sem rodar testes
RUN ./mvnw clean package -DskipTests -B

# ============================================
# Runtime stage
# ============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o jar gerado no build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
