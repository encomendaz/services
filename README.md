Correios RESTful Web Services
=============================

Como não achei um free por aí, estou construindo RESTful Web Services para os Correios.

Rastreamento
------------

Por enquanto estou empenhado no serviço de rastreamento, mas com o tempo disponibilizarei outros. Estes serviços expõem as informações contidas nas páginas HTML dos Correios. O parse é feito com auxílio do projeto Alfred Library (http://alfredlibrary.org).  

### Exemplo de uso

O serviço de rastreamento está disponível provisoriamente nesta URL, que em breve hospedarei em outro local mais apropriado. Eis aqui um exemplo de uso:

http://rest.cleversonsacramento.com/rastreamento.json?id=PB755604756BR?inicio=1&fim=6&ordem=asc

Os parâmetros "inicio", "fim" e "ordem" são opcionais, mas o "id" é obrigatório.

Contribuição
--------------

O projeto está de portas abertas para contribuição. Se quiser ajudar faça um fork, incremente seu código e faça um pull request.

Este serviço será de graça!