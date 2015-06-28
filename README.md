Android Canarinho
=================

Esta biblioteca é um conjunto de utilitários para trabalhar com padrões brasileiros no Android. Inspirado em: https://github.com/caelum/caelum-stella.

O foco aqui é o Android. Portanto, não é compatível com aplicações Java puras.

Entre os padrões implementados temos:

- Formatadores e validadores de CPF
- Formatadores e validadores de CNPJ
- Formatadores e validadores de boleto bancário
- Formatadores de telefone

Estes são utilizados para implementar `TextWatcher`s que formatam e validam a digitação do usuário.

Para exemplos de uso, veja os testes na aplicação de exemplo (pasta sample).

Changelog
---------
- 0.0.2:
    - Formatadores de telefone
- 0.0.1:
    - Release inicial

Backlog
-------

- Incluir validadores e formatadores para inscrições estaduais.
- Incluir formatador e watcher para CEP.
- Melhorar o Sample para ser publicado.
- Adicionar o logotipo.
- Criar uma página gh-pages que tenha um link para Javadoc.
- Criar GIFs do uso.
- Aumentar a documentação do projeto sobre o uso.
- Incluir documentação de como testar.
- Incluir documentação sobre a configuração de ProGuard.
- Terminar de documentar com Javadoc.
- Incluir Checkstyle, PMD e FindBugs (com refinamento se não todos endoidamos).
- Analisar a viabilidade de rodar os testes de JVM no Travis.
- Incluir mais testes de JVM (Watchers estão apenas como testes instrumentados e dependem da interface).
- Incluir uma tradução para o inglês das interfaces públicas apenas na documentação.

ATENÇÃO
-------

Este projeto é desenvolvido de boa vontade e com o intuito de ajudar. No entanto, todo o desenvolvimento é feito SEM GARANTIAS.

LICENÇA
-------

Este projeto é disponibilizado sob a licença Apache vesão 2.0. Ver declaração no arquivo LICENSE.txt