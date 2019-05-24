FROM java:8
VOLUME /tmp
ADD user-server.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]
