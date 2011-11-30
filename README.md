EncomendaZ RESTful Web Services
=============================

Uma alternativa para acessar os serviços do EncomendaZ via RESTful Web Services.

Rastreamento
------------

Por enquanto estamos empenhado no serviço de rastreamento, mas com o tempo disponibilizaremos outros. Estes serviços expõem as informações contidas nas páginas HTML dos Correios. O parse é feito com auxílio do projeto Alfred Library (http://alfredlibrary.org).  

### Exemplo de uso

O serviço de rastreamento está disponível nesta URL. Eis aqui um exemplo de uso:

http://rest.encomendaz.net/rastreamento.json?id=PB755604756BR&inicio=1&fim=6&ordem=asc

Os parâmetros "inicio", "fim" e "ordem" são opcionais, mas o "id" é obrigatório.

Contribuição
--------------

O projeto está de portas abertas para contribuição. Se quiser ajudar faça um fork, incremente seu código e faça um pull request.

Este serviço é de graça!