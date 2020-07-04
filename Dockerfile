FROM openjdk:14-alpine
COPY build/libs/test-*-all.jar test.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "test.jar"]