package dev.jameido.pokedex.di

import dev.jameido.pokedex.usecase.GetPkmnDetail
import dev.jameido.pokedex.usecase.GetPkmnListPage
import dev.jameido.pokedex.usecase.GetPkmnSpecies
import org.koin.dsl.module

/**
 * Created by Jameido on 21/12/2020.
 */
val useCaseModule = module {
    factory { GetPkmnListPage(repository = get()) }
    factory { GetPkmnDetail(repository = get()) }
    factory { GetPkmnSpecies(repository = get()) }
}