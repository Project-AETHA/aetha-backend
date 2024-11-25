FROM bellsoft/liberica-openjdk-alpine:17 AS builder

WORKDIR /home/app
ADD ./ /home/app/aetha
RUN chmod +x /home/app/aetha/mvnw
RUN cd aetha && ./mvnw -Dmaven.test.skip=true clean package


FROM bellsoft/liberica-openjre-alpine:17

WORKDIR /home/app
EXPOSE 8080
SHELL ["/bin/sh", "-c"]
ENTRYPOINT java -jar ./aetha.jar
COPY --from=builder /home/app/aetha/target/*.jar aetha.jar