# 第一阶段：构建 JAR 文件
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：运行 JAR 文件
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/your-springboot-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
