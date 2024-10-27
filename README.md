# Swap Test

Seja bem vindo ao nosso projeto. Nosso objetivo é implementar o Swap Teste, de acordo com as boas práticas de
desenvolvimento de software.

Para implementação, utilizamos as seguintes tecnologias e frameworks:

- Java 17
- Springboot 3
- Docker
- Maven

## Veja abaixo os principais comandos para execução do projeto:

###Configurar o endereço do webhook
Edite o arquivo src/main/resources/application.yml e adicione o endereço do seu webhook. 

Exemplo:
```yml
webhook:
url: https://webhook.site/d2adea52-4308-4bee-aeef-cb2f87f89307
```
###Buildar o projeto

```
docker build -t swap-test .
```

###Deploy in local docker

```
docker run -d -p 8080:8080 --name test-service-container test-swap
```

###View logs
```
docker logs test-service-container
```

###CURL da API

```curl 
curl --location 'localhost:8080/api/process-repository' \
--header 'Content-Type: application/json' \
--data '{
    "user": "TIBCOSoftware",
    "repository":"jasperreports"
}'
```