package project.loja.hardware.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CrudController<T> {

    /**
     * Cria uma nova entidade.
     *
     * @param entity a entidade a ser criada
     * @return a entidade criada
     */
    ResponseEntity<String> create(@RequestBody T entity);

    /**
     * Busca uma entidade pelo seu ID.
     *
     * @param id o ID da entidade a ser buscada
     * @return a entidade encontrada ou uma resposta de erro se não for encontrada
     */
    ResponseEntity<T> getById(@PathVariable int id);

    /**
     * Retorna todas as entidades.
     *
     * @return uma lista de todas as entidades
     */
    ResponseEntity<List<T>> getAll();

    /**
     * Atualiza uma entidade existente.
     *
     * @param id     o ID da entidade a ser atualizada
     * @param entity a entidade com os novos dados
     * @return a entidade atualizada
     */
    ResponseEntity<String> update(@PathVariable int id, @RequestBody T entity);

    /**
     * Deleta uma entidade pelo seu ID.
     *
     * @param id o ID da entidade a ser deletada
     * @return uma resposta vazia indicando sucesso ou erro
     */
    ResponseEntity<String> delete(@PathVariable int id);
}