# 1. 빌드된 .jar 파일을 실행할 수 있는 자바 환경(JDK)을 가져옵니다.
FROM amazoncorretto:17-alpine

# 2. 빌드 결과물인 jar 파일의 위치를 변수로 지정합니다. (보통 build/libs 폴더에 생성됨)
ARG JAR_FILE=build/libs/*.jar

EXPOSE 8080

# 3. 위에서 지정한 jar 파일을 컨테이너 내부로 복사하고 이름을 'app.jar'로 바꿉니다.
COPY ${JAR_FILE} app.jar

# 4. 컨테이너가 시작될 때 실행할 명령어를 입력합니다.
# -Dspring.profiles.active=prod 를 추가하면 배포용 설정을 사용하게 됩니다.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]