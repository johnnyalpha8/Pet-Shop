FROM openjdk:11
MAINTAINER PsiCom
COPY build/libs/reactiveKotlin-0.0.1-SNAPSHOT.jar reactiveKotlin-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/reactiveKotlin-0.0.1-SNAPSHOT.jar"]
EXPOSE 9000
COPY ./wait-for-it.sh /wait-for-it.sh
RUN chmod +x wait-for-it.sh
ENTRYPOINT [ "/bin/bash", "-c" ]
CMD ["./wait-for-it.sh 127.0.0.1:3306 --strict -t 120 -- java -jar /reactiveKotlin-0.0.1-SNAPSHOT.jar"]
