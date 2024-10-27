# Swap Test

Seja bem vindo ao nosso projeto. Nosso objetivo é implementar o Swap Teste, de acordo com as boas práticas de
desenvolvimento de software.

Para implementação, utilizamos as seguintes tecnologias e frameworks:

- Java 17
- Springboot 3
- Docker
- Maven

## Veja abaixo os principais comandos para execução do projeto:

###Buildar o projeto

docker build -t swap-test .

###Deploy in local docker

docker run -d -p 8080:8080 --name test-service-container test-swap

###View logs
docker logs test-service-container