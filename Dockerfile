FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY target/pedidos-0.0.1-SNAPSHOT.jar pedidos.jar
ENTRYPOINT ["java","-jar","/pedidos.jar"]