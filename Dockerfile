FROM eclipse-temurin:17-jre-alpine
RUN mkdir /app

WORKDIR /app

ADD ./api/target/uporabniki-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "uporabniki-api-1.0.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "uporabniki-api-1.0.0-SNAPSHOT.jar"]
#CMD java -jar uporabniki-api-1.0.0-SNAPSHOT.jar
