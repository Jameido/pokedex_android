package dev.jameido.pokedex.data.usecase

import dev.jameido.pokedex.data.repository.PkmnRepository
import dev.jameido.pokedex.domain.entity.PkmnListPageEntity
import dev.jameido.pokedex.domain.usecase.GetPkmnListPage

/**
 * Created by Jameido on 20/12/2020.
 */
class GetPkmnListPageImpl(private val repository: PkmnRepository) : GetPkmnListPage {

   override suspend fun load(query: String?, page: Int, pageSize: Int, refresh: Boolean): PkmnListPageEntity {
        return repository.pkmnList(query, page, pageSize, refresh)
    }
}