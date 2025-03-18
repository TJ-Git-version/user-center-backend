# 第一阶段：构建 JAR 文件
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 第二阶段：运行 JAR 文件
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/user-center-0.0.1-SNAPSHOT.jar user-center-0.0.1-SNAPSHOT.jar
EXPOSE 8800
ENTRYPOINT ["java", "-jar", "user-center-0.0.1-SNAPSHOT.jar"]
