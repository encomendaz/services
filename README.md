Correios RESTful Web Services
=============================

Como não achei um free por aí, estou construindo RESTful Web Services para os Correios.

Rastreamento
------------

Por enquanto estou empenhado no serviço de rastreamento, mas com o tempo disponibilizarei outros. Estes serviços expõem as informações contidas nas páginas HTML dos Correios. O parse é feito com auxílio do projeto Alfred Library (http://alfredlibrary.org).  

O serviço de rastreamento está disponível provisoriamente nesta URL, que em breve hospedarei em outro local mais apropriado:

http://rest.cleversonsacramento.com/rastreamento/{identificação-do-objeto}?ini={registro-inicial}&fim={registro-final}

Eis aqui um exemplo de uso:

http://rest.cleversonsacramento.com/rastreamento/PB755604756BR?ini=1&fim=6

Os parâmetros "ini" e "fim" são opcionais, mas a identificação do objeto é obrigatória.

O projeto está de portas abertas para contribuição. Se quiser ajudar faça um fork, incremente seu código e faça um pull request.

Este serviço será de graça!