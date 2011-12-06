EncomendaZ RESTful Web Services
=============================

Uma alternativa para acessar os serviços do EncomendaZ via RESTful Web Services.

Rastreamento
------------

Este serviço expõe as informações contidas nas páginas HTML dos Correios. O parse é feito com auxílio do projeto Alfred Library (http://alfredlibrary.org).  

### Consulta

O serviço de rastreamento está disponível nesta URL. Eis aqui um exemplo de uso:

http://rest.encomendaz.net/rastreamento.json?id=PB755604756BR&inicio=1&fim=6&ordem=asc

Os parâmetros "inicio", "fim" e "ordem" são opcionais, mas o "id" é obrigatório.

Monitoramento
------------

Este serviço possibilita o monitoramento de encomendas. Sempre que o status da encomenda mudar, uma mensagem será enviada para um determinado e-mail.  

### Cadastro

Para cadastrar um novo monitoramento faça o seguinte via Terminal (ou utilize a ferramenta que preferir):

curl -i -X PUT -d "id=PB883024503BR&email=cleverson.sacramento@gmail.com" http://rest.encomendaz.net/monitoramento.json

Os parâmetros "id" e "email" são obrigatórios.

### Consulta

Consulta todos os monitoramentos associados a um determinado e-mail:

http://rest.encomendaz.net/monitoramento.json?email=cleverson.sacramento@gmail.com

O parâmetro "email" é obrigatório.

Contribuição
--------------

O projeto está de portas abertas para contribuição. Se quiser ajudar faça um fork, incremente seu código e faça um pull request.

Este serviço é de graça!