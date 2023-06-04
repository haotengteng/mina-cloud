#FROM java:8
FROM registry.cn-hangzhou.aliyuncs.com/haot_repo/jdk8-mvn:v3
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#nocalhost 配置
#  debug:
#    - mvn
#    - package
#    - -pl
#    - mina-boot-example/mina-boot-example-docker
#    - -Dmaven.test.skip=true
#    - '&&'
#    - mvn
#    - -e
#    - spring-boot:run
#    - -pl
#    - mina-boot-example/mina-boot-example-docker
#    - -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5345"
