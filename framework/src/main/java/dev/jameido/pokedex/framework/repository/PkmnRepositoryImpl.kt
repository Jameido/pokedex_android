package dev.jameido.pokedex.framework.repository

import dev.jameido.pokedex.data.datasource.LocalPkmnDataSource
import dev.jameido.pokedex.data.datasource.NetworkPkmnDataSource
import dev.jameido.pokedex.data.mappers.*
import dev.jameido.pokedex.data.models.RemotePageKey
import dev.jameido.pokedex.data.repository.PkmnRepository
import dev.jameido.pokedex.domain.entity.*

/**
 * Created by Jameido on 17/12/2020.
 */
class PkmnRepositoryImpl(
        private val networkDataSource: NetworkPkmnDataSource,
        private val localPkmnDataSource: LocalPkmnDataSource
) : PkmnRepository {

    companion object {
        const val REMOTE_PAGE_SIZE = 60
        const val REMOTE_SPECIES_LIST = "species_list"
    }

    /**
     * Get list of pokemon species page.
     * First read from local data source. If the retrieved page is empty or not fully populated
     * update the local data from the network data source, then read it again from local data source.
     * If refresh == true and page == 0 force the local data to be updated by wiping it, this
     * triggers loading data from network data source.
     */
    override suspend fun pkmnList(query: String?, page: Int, pageSize: Int, refresh: Boolean): PkmnListPageEntity {
        if (refresh && page == 0) {
            localPkmnDataSource.wipeData()
        }
        return pkmnListInternal(query, page, pageSize, false)
    }

    private suspend fun pkmnListInternal(query: String?, page: Int, pageSize: Int, applyExitCondition: Boolean): PkmnListPageEntity {
        var localPage = readListFromDatabase(query, page, pageSize)
        if (localPage.next == null) {
            val nextRemoteKey = localPkmnDataSource.getNextRemotePageKey(REMOTE_SPECIES_LIST)
            if (nextRemoteKey?.nextPage != null || (page == 0 && !applyExitCondition)) {
                updateListFromNetwork(query, nextRemoteKey?.nextPage ?: 0, REMOTE_PAGE_SIZE)
                localPage = pkmnListInternal(query, page, pageSize, true)
            }
        }
        return localPage
    }

    /**
     * Get pokemon detail data.
     * First read from local data source. If the local data is null readt the new data from the
     * network data from the data source and return it after updating the local data.
     */
    override suspend fun pkmnDetail(name: String): PkmnDetailEntity {
        return readDetailFromDb(name) ?: readDetailFromNetwork(name)!!
    }

    /**
     * Get pokemon species detail data.
     * First read from local data source. If the local data is null readt the new data from the
     * network data from the data source and return it after updating the local data.
     */
    override suspend fun pkmnSpecies(name: String): PkmnSpeciesEntity {
        return readSpeciesFromDb(name) ?: readSpeciesFromNetwork(name)!!
    }

    //region list
    /**
     * Get list of pokemon species page from local data source
     */
    private suspend fun readListFromDatabase(query: String?, page: Int, pageSize: Int): PkmnListPageEntity {
        return localPkmnDataSource.list(query, pageSize, page)?.let {
            return PkmnListEntityMapper(PkmnEntityMapper(IdMapper(), SpriteMapper())).map(it)
        } ?: PkmnListPageEntity(null, null, emptyList())
    }

    /**
     * Get list of pokemon species page from network data source, then store the retrieved data and
     * next page key into local data source.
     */
    private suspend fun updateListFromNetwork(query: String?, page: Int, pageSize: Int): PkmnListPageEntity {
        return networkDataSource.list(query, pageSize, page)?.let {
            localPkmnDataSource.insertPokemon(it.results)
            localPkmnDataSource.insertNextRemotePageKey(RemotePageKey(REMOTE_SPECIES_LIST, it.next))
            return PkmnListEntityMapper(PkmnEntityMapper(IdMapper(), SpriteMapper())).map(it)
        } ?: PkmnListPageEntity(null, null, emptyList())
    }
    //endregion

    //region detail
    /**
     * Get pokemon detail data from local data source.
     */
    private suspend fun readDetailFromDb(name: String): PkmnDetailEntity? {
        return localPkmnDataSource.detail(name)?.let {
            PkmnDetailEntityMapper(SpriteMapper(), StatEntityMapper()).map(it)
        }
    }

    /**
     * Get pokemon detail data from network data source and store it in local data source
     */
    private suspend fun readDetailFromNetwork(name: String): PkmnDetailEntity? {
        return networkDataSource.detail(name)?.let {
            localPkmnDataSource.insertDetail(it)
            return PkmnDetailEntityMapper(SpriteMapper(), StatEntityMapper()).map(it)
        }
    }
    //endregion


    //region species
    /**
     * Get pokemon species detail data from local data source
     */
    private suspend fun readSpeciesFromDb(name: String): PkmnSpeciesEntity? {
        return localPkmnDataSource.species(name)?.let {
            PkmnSpeciesEntityMapper(PkmnEntityMapper(IdMapper(), SpriteMapper())).map(it)
        }
    }

    /**
     * Get pokemon species detail data from network data source and store it in local data source
     */
    private suspend fun readSpeciesFromNetwork(name: String): PkmnSpeciesEntity? {
        return networkDataSource.species(name)?.let {
            localPkmnDataSource.insertSpecies(it)
            return PkmnSpeciesEntityMapper(PkmnEntityMapper(IdMapper(), SpriteMapper())).map(it)
        }
    }
    //endregion
}