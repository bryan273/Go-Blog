FROM gradle:jdk17-alpine
ARG PRODUCTION
ARG JDBC_DATABASE_PASSWORD
ARG JDBC_DATABASE_URL_PRODUCTION
ARG JDBC_DATABASE_USERNAME
ARG API_KEY
ARG AUTH_SERVICE_BASE_URL

ENV PRODUCTION ${PRODUCTION}
ENV JDBC_DATABASE_PASSWORD ${JDBC_DATABASE_PASSWORD}
ENV JDBC_DATABASE_URL_PRODUCTION ${JDBC_DATABASE_URL_PRODUCTION}
ENV JDBC_DATABASE_USERNAME ${JDBC_DATABASE_USERNAME}
ENV API_KEY ${API_KEY}
ENV AUTH_SERVICE_BASE_URL ${AUTH_SERVICE_BASE_URL}

WORKDIR /app
COPY ./build/libs/go-blog-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java","-jar","go-blog-0.0.1-SNAPSHOT.jar"]
