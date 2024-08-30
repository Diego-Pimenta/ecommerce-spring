[X] Permitir que os usuários criem, leiam, atualizem e excluam produtos. PESO (3)

[X] Criar validações que façam sentido para os dados de entrada, por exemplo: Preço do produto só pode ser positivo. PESO (2)

[X] Um produto não pode ser DELETADO após ser incluso em uma venda, porém deve ter alguma maneira de INATIVAR ele. PESO (2)

[X] Controlar o estoque do produto de forma que ele não possa ser vendido caso o estoque seja menor do que a quantidade necessária para a venda ou igual a zero. PESO (3)

[X] Permitir que os usuários criem, leiam, atualizem e excluam vendas (Uma venda tem que ter no mínimo 1 produto para ser concluída). PESO (3)

[] Criar métodos de relatório de vendas por data (informada pelo cliente), por mês e pela semana atual (considerar dias úteis). PESO (1)

[X] Os métodos “GET ALL” de Produtos e Vendas devem salvar as informações no CACHE da aplicação, para que as próximas buscas sejam mais rápidas. Deve ser feito um bom gerenciamento do cache, por exemplo: ao inserir uma nova venda, deletar o cache, para que a informação seja buscada no banco de dados diretamente e venha atualizada. PESO (1)

[X] Todas as EXCEÇÕES devem ser tratadas e seguir o mesmo padrão de resposta. PESO (1)

[X] Todos os campos data, devem seguir o padrão ISO 8601 (exemplo: 2023-07-20T12:00:00Z). PESO (1)

[X] Implementar Autenticação via Token JWT. PESO (3)

[X] Implementar Autorização para os usuários autenticados. PESO (3)

[X] Implementar um método para resetar a senha do usuário. PESO (3)

[X] Somente usuários com permissão de ADMIN podem deletar informações do sistema. PESO (3)

[] Somente usuários com permissão de ADMIN podem cadastrar e atualizar novos produtos e outros usuários ADMIN. PESO (2)