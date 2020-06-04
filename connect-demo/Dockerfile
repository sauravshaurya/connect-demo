FROM confluentinc/cp-kafka-connect:5.4.1-1-ubi8

USER appuser
COPY demo/test.txt /usr/appuser/
COPY target/connect-demo-1.0.0-SNAPSHOT.jar /usr/share/confluent-hub-components/