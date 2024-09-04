package project.loja.hardware.repository;

import java.util.List;

public interface CrudRepository<T> {

    /**
     * Salva uma nova entidade ou atualiza uma existente.
     *
     * @param entity a entidade a ser salva ou atualizada
     * @return o ID da entidade salva
     */
    int save(T entity);

    /**
     * Encontra uma entidade pelo seu ID.
     *
     * @param id o ID da entidade a ser encontrada
     * @return a entidade encontrada ou null se não for encontrada
     */
    T findById(int id);

    /**
     * Retorna todas as entidades.
     *
     * @return uma lista de todas as entidades
     */
    List<T> findAll();

    /**
     * Atualiza uma entidade existente.
     *
     * @param entity a entidade a ser atualizada
     * @return o ID da entidade atualizada
     */
    int update(T entity);

    /**
     * Deleta uma entidade pelo seu ID.
     *
     * @param id o ID da entidade a ser deletada
     * @return o ID da entidade deletada
     */
    int deleteById(int id);
}
