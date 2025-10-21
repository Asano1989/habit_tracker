# -----------------------------------------------------------
# STAGE 1: ビルドステージ (MavenとJDKを含む環境)
# -----------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

# 作業ディレクトリを /app に設定
WORKDIR /app

# Maven の設定ファイルとプロジェクトファイルだけをコピーして依存関係をダウンロード
# これにより、pom.xmlが変わらない限りキャッシュが効き、ビルドを高速化
COPY app/pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline

# すべてのソースコードをコピー
COPY app .

# Mavenでアプリケーションをビルド
RUN mvn clean package -DskipTests

# -----------------------------------------------------------
# STAGE 2: 実行ステージ (軽量なJREのみの環境)
# -----------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

# ビルドステージから JAR ファイルをコピー
# ファイルパスは、プロジェクト構造とビルドステージのWORKDIRから決定
COPY --from=builder /app/target/*.jar /app.jar

# Spring Bootアプリケーションの起動
ENTRYPOINT ["java","-jar","/app.jar"]