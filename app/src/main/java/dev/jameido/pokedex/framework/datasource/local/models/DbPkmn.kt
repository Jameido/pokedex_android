package dev.jameido.pokedex.framework.datasource.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Jameido on 05/01/2021.
 */
@Entity(tableName = "pokemon")
data class DbPkmn(
        @PrimaryKey val name: String = "",
        val url: String?
)