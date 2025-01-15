FROM openjdk:21

WORKDIR /app

COPY target/orderpayment-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
EXPOSE 3306
EXPOSE 9092
EXPOSE 2181

CMD ["sh", "-c", "kafka-topics --create --topic registros-topic --bootstrap-server kakfa-broker:9092 --partitions 1 --replication-factor 1"]
ENTRYPOINT ["java", "-jar", "app.jar"]
