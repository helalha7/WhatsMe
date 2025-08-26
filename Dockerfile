# -------- Build stage (JDK 21) --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy wrapper + pom first (enables dependency caching)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw

# (debug) confirm Java/Maven versions
RUN java -version
RUN ./mvnw -v

# Pre-fetch dependencies into cache layers
RUN ./mvnw -B -DskipTests dependency:go-offline

# Now copy sources and build
COPY src ./src
RUN ./mvnw -B -DskipTests clean package

# -------- Runtime stage (JRE 21) --------
FROM eclipse-temurin:21-jre
WORKDIR /app

# If your artifact name differs, update the wildcard or set exact file name
COPY --from=build /app/target/*SNAPSHOT.jar /app/app.jar

# Spring Boot listens on 8080 by default; change if you use another port
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--server.port=8080"]
