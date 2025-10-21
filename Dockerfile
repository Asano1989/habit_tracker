# -----------------------------------------------------------
# STAGE 1: ビルドステージ (MavenとJDKを含む環境)
# -----------------------------------------------------------
FROM eclipse-temurin:17-jdk-alpine AS builder

# 作業ディレクトリを app のプロジェクトルートに設定
WORKDIR /app

# Maven の設定ファイルとプロジェクトファイルだけをコピーして依存関係をダウンロード
# これにより、pom.xmlが変わらない限りキャッシュが効き、ビルドを高速化
COPY app/pom.xml .
RUN mvn dependency:go-offline

# すべてのソースコードをコピー
COPY app .

# Mavenでアプリケーションをビルド
RUN mvn clean package -DskipTests

# -----------------------------------------------------------
# STAGE 2: 実行ステージ (軽量なJREのみの環境)
# -----------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

# ビルドステージから JAR ファイルをコピー
# JARファイルの名前は pom.xml に依存しますが、ここでは一般的な名前を使用
COPY /app/target/*.jar app.jar

# Spring Bootアプリケーションの起動
ENTRYPOINT ["java","-jar","/app.jar"]