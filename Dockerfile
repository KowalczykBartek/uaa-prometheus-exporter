FROM openjdk:11-jre-slim
WORKDIR /
COPY ./build/libs/uaa-prometheus-exporter-*fat.jar uaa-prometheus-exporter-server.jar
EXPOSE 9090
CMD java -jar -Xms32m -Xmx32m uaa-prometheus-exporter-server.jar
