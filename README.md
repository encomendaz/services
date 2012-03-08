EncomendaZ RESTful Web Services
=============================

Uma alternativa para acessar os serviços do EncomendaZ via RESTful Web Services.

Rastreamento
------------

Este serviço expõe as informações contidas nas páginas HTML dos Correios. O parse é feito com auxílio do projeto Alfred Library (http://alfredlibrary.org).  

### Consulta

O serviço de rastreamento está disponível nesta URL. Eis aqui um exemplo de uso:

http://rest.encomendaz.net/tracking.json?id=PB882615209BR&start=1&end=6

Os parâmetros "start", "end" e "jsonp" são opcionais, mas o "id" é obrigatório.

Contribuição
--------------

O projeto está de portas abertas para contribuição. Se quiser ajudar faça um fork, incremente seu código e faça um pull request.

Este serviço é de graça!