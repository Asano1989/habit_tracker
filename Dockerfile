# -------- Node stage: Tailwind build --------
FROM node:18 AS node-builder
WORKDIR /app

# package.jsonとlockを先にコピー
COPY app/package*.json ./
RUN npm install

# Tailwind設定やCSSソースをコピー
COPY app/tailwind.config.js ./
COPY app/postcss.config.js ./
COPY app/src ./src

# CSSビルド
RUN npm run build:css


# -------- Maven stage: Spring Boot package --------
FROM maven:3.9.5-eclipse-temurin-17 AS maven-builder
WORKDIR /app

COPY app/pom.xml .
RUN mvn dependency:go-offline

COPY app .
# NodeでビルドしたCSSをstatic配下へコピー
COPY --from=node-builder /app/src/main/resources/static/css/output.css ./src/main/resources/static/css/output.css

RUN mvn clean package -DskipTests

# =========================================================
# 証明書生成 (cert-generator)
# =========================================================
FROM cockroachdb/cockroach:latest AS cert-generator
WORKDIR /certs

# 1. 証明書生成コマンドを実行
# CA (認証局) を作成し、ノード用 (roach1) とクライアント用 (postgre, root) の証明書を作成
RUN cockroach cert create-ca --certs-dir=/certs --ca-key=/certs/ca.key && \
    cockroach cert create-node roach1 localhost 127.0.0.1 --certs-dir=/certs --ca-key=/certs/ca.key && \
    cockroach cert create-client postgre --certs-dir=/certs --ca-key=/certs/ca.key && \
    cockroach cert create-client root --certs-dir=/certs --ca-key=/certs/ca.key


# -------- Runtime stage --------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=maven-builder /app/target/learning_tracker-0.0.1-SNAPSHOT.jar app.jar

# 証明書ファイルのコピー (cert-generatorから)
COPY --from=cert-generator /certs/ca.crt /usr/local/certs/ca.crt
COPY --from=cert-generator /certs/client.root.crt /usr/local/certs/client.root.crt
COPY --from=cert-generator /certs/client.root.key /usr/local/certs/client.root.key
COPY --from=cert-generator /certs/client.postgre.crt /usr/local/certs/client.postgre.crt
COPY --from=cert-generator /certs/client.postgre.key /usr/local/certs/client.postgre.key
COPY --from=cert-generator /certs/node.crt /usr/local/certs/node.crt
COPY --from=cert-generator /certs/node.key /usr/local/certs/node.key

# Javaの信頼ストアにCA証明書をインポート
#RUN keytool -import \
#    -file /usr/local/certs/ca.crt \
#    -alias cockroach-ca \
#    -keystore $JAVA_HOME/lib/security/cacerts \
#    -storepass changeit \
#    -noprompt

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
