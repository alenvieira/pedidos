# pedidos

Sistema de pedidos. Mais detalhes no [relatório](doc/relatorio.md).

## Para rodar a aplicação localmente:
```console
## Subir primeiro as depedências
docker run -itd -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgresq -e POSTGRES_DB=pedidos -p 5432:5432 --name postgresql postgres
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:management
## Depois que ambas tiverem ok, podemos rodar a aplicação
./mvnw spring-boot:run
```

## Para rodar os testes:
```console
./mvnw test
```