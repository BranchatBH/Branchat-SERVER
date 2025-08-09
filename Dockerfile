# 1) 빌드 단계
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Gradle Wrapper 및 설정 먼저 복사(캐시 효과)
COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle* settings.gradle
COPY build.gradle* build.gradle
RUN chmod +x gradlew

# 소스 복사 후 테스트 & 패키징
COPY src src
# Application.yml의 프로필을 강제로 test로 바꿈/ gradle daemon 사용하지 않음(일회성 빌드이므로 쓸 이유 없음)
RUN ./gradlew clean test -Dspring.profiles.active=test --no-daemon
###여기까지가 test과정

# 실행파일 생성(JAR 생성) -> cd에 전달
RUN ./gradlew bootJar --no-daemon

# 2) 런타임 단계(JRE만)
FROM eclipse-temurin:17-jre-slim
WORKDIR /app

ENV TZ=Asia/Seoul
RUN apt-get update \
 && apt-get install -y --no-install-recommends tzdata \
 && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
 && echo $TZ > /etc/timezone \
 && rm -rf /var/lib/apt/lists/*

# 빌드에서 생성된 jar파일을 가져와서 app.jar로 저장
COPY --from=builder /app/build/libs/app.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8080

ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -Duser.timezone=Asia/Seoul -jar /app/app.jar"]
