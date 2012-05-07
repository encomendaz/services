EncomendaZ Web Services
=============================

Uma alternativa para acessar os serviços do EncomendaZ via Web Services (RESTful).

Rastreamento
------------

Este serviço expõe as informações contidas nas páginas HTML dos Correios. O parse é feito com auxílio do projeto Alfred Library (http://alfredlibrary.org).  

### Consulta (GET Method)

O serviço de rastreamento está disponível nesta URL:

http://services.encomendaz.net/tracking.json?id=PB882615209BR&start=1&end=6

Os parâmetros "start" e "end" são opcionais, mas o "id" é obrigatório e devem ser enviados via query.

Para resquisições JSONP, use esta URL:

http://services.encomendaz.net/tracking.json?id=PB882615209BR&start=1&end=6&callback=myJSONPCallback

Monitoramento
------------

Este serviço disponibiliza o cadastro, exclusão e consulta de monitoramento de encomendas dos Correios

### Consulta (GET Method)

A consulta de monitoramentos está disponível nesta URL:

http://services.encomendaz.net/monitoring.json?clientId=cleverson.sacramento@gmail.com

O parâmetro "clientId" é obrigatório e deve corresponder ao e-mail do interessado no monitoramento da encomenda enviado via query.

Para resquisições JSONP, use esta URL:

http://services.encomendaz.net/monitoring.json?clientId=cleverson.sacramento@gmail.com&callback=myJSONPCallback

### Inclusão (PUT Method)

Os parâmetros "clientId" e "trackId" são obrigatórios e devem corresponder ao e-mail do interessado (clientId) no monitoramento de uma determianda encomenda (trackId) e devem ser enviados via post.

### Exclusão (DELETE Method)

http://services.encomendaz.net/monitoring.json?clientId=cleverson.sacramento@gmail.com&trackId=XX000000000XX

Os parâmetros "clientId" e "trackId" são obrigatórios e devem ser enviados via query.

Contribuição
--------------

O projeto está de portas abertas para contribuição. Se quiser ajudar, experimente diversos códigos de rastreio e registre os bugs aqui:

https://github.com/encomendaz/services/issues

Se preferir submeter código-fonte, faça um fork e envie seu pull request. Este serviço é de graça!

Leia mais
--------------

http://cleversonsacramento.com/2012/03/12/restful-web-services-dos-correios/