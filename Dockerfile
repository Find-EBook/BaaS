## 베이스 이미지로 OpenJDK 17을 사용합니다.
#FROM openjdk:17
#
## 앱에 필요한 환경 변수 설정 (예: JWT 관련 설정)
#ENV JWT_SECRET=BaaSSecretKey
#ENV JWT_EXPIRATION=604800
#
## JAR 파일을 컨테이너로 복사
#ARG JAR_FILE=build/libs/*.jar
#COPY build/libs/*.jar app.jar
#
#
## 포트 8080을 열어둡니다 (Spring Boot의 기본 포트).
#EXPOSE 8080
#
## 컨테이너 실행 시 app.jar를 실행합니다.
#ENTRYPOINT ["java","-jar","/app.jar"]
