FROM openjdk:17-jdk-slim

WORKDIR /app

# Mavenラッパーと設定ファイルをコピー
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# 実行権限を明示的に付与
RUN chmod +x mvnw

# 依存関係の事前ダウンロード
RUN ./mvnw dependency:go-offline -B

# ソースコードをコピー
COPY src ./src

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-Dserver.port=8080'"]