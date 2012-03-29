EncomendaZ Web Services
=============================

Uma alternativa para acessar os serviços do EncomendaZ via Web Services (RESTful).

Rastreamento
------------

Este serviço expõe as informações contidas nas páginas HTML dos Correios. O parse é feito com auxílio do projeto Alfred Library (http://alfredlibrary.org).  

### Consulta

O serviço de rastreamento está disponível nesta URL:

http://services.encomendaz.net/tracking.json?id=PB882615209BR&start=1&end=6

Os parâmetros "start" e "end" são opcionais, mas o "id" é obrigatório.

Para resquisições JSONP, use esta URL:

http://services.encomendaz.net/tracking.json?id=PB882615209BR&start=1&end=6&callback=myJSONPCallback

Contribuição
--------------

O projeto está de portas abertas para contribuição. Se quiser ajudar, experimente diversos códigos de rastreio e registre os bugs aqui:

https://github.com/encomendaz/services/issues

Se preferir submeter código-fonte, faça um fork e envie seu pull request. Este serviço é de graça!

Leia mais
--------------

http://cleversonsacramento.com/2012/03/12/restful-web-services-dos-correios/