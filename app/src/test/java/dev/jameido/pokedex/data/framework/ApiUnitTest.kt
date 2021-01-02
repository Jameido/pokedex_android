package dev.jameido.pokedex.data.framework

import dev.jameido.pokedex.data.models.PkmnElement
import dev.jameido.pokedex.framework.RetrofitPkmnDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Jameido on 18/12/2020.
 */
class ApiUnitTest {

    private lateinit var server: MockWebServer
    private lateinit var mockApi: RetrofitPkmnDataSource

    @Test
    fun getPkmnList() {

        server.enqueue(MockResponse().setBody(LIST_FIRST_ELEMENTS))
        val response = runBlocking {
            mockApi.list(0, 20)
        }

        assertNotNull(response)
        assertEquals(response.count, 1118)
        assertEquals(response.next, "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20")
        assertNull(response.previous)
        assertNotNull(response.results)
        assertEquals(response.results.size, 20)
        assertEquals(response.results[0], PkmnElement("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"))

    }
    @Test
    fun getPkmnDetail() {

        server.enqueue(MockResponse().setBody(DETAIL_DITTO))
        val response = runBlocking {
            mockApi.detail("ditto")
        }

        assertNotNull(response)
        assertNotNull(response)
        assertEquals(response!!.id, 132)
        assertEquals(response!!.name, "ditto")
        assertNotNull(response!!.sprites)
        assertEquals(response!!.sprites.front_default, "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png")
        assertEquals(response!!.stats.size, 6)
        assertNotNull(response!!.stats[0])
        assertEquals(response!!.stats[0].base_stat, 48)
        assertNotNull(response!!.types[0].type)
        assertEquals(response!!.types[0].type.name, "normal")

    }

    @Before
    fun setUp() {
        server = MockWebServer().apply {
            start()
        }
        mockApi = Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(RetrofitPkmnDataSource::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    companion object {
        //Response of pokemon?limit=20&offset=0
        const val LIST_FIRST_ELEMENTS = """{"count":1118,"next":"https://pokeapi.co/api/v2/pokemon?offset=20&limit=20","previous":null,"results":[{"name":"bulbasaur","url":"https://pokeapi.co/api/v2/pokemon/1/"},{"name":"ivysaur","url":"https://pokeapi.co/api/v2/pokemon/2/"},{"name":"venusaur","url":"https://pokeapi.co/api/v2/pokemon/3/"},{"name":"charmander","url":"https://pokeapi.co/api/v2/pokemon/4/"},{"name":"charmeleon","url":"https://pokeapi.co/api/v2/pokemon/5/"},{"name":"charizard","url":"https://pokeapi.co/api/v2/pokemon/6/"},{"name":"squirtle","url":"https://pokeapi.co/api/v2/pokemon/7/"},{"name":"wartortle","url":"https://pokeapi.co/api/v2/pokemon/8/"},{"name":"blastoise","url":"https://pokeapi.co/api/v2/pokemon/9/"},{"name":"caterpie","url":"https://pokeapi.co/api/v2/pokemon/10/"},{"name":"metapod","url":"https://pokeapi.co/api/v2/pokemon/11/"},{"name":"butterfree","url":"https://pokeapi.co/api/v2/pokemon/12/"},{"name":"weedle","url":"https://pokeapi.co/api/v2/pokemon/13/"},{"name":"kakuna","url":"https://pokeapi.co/api/v2/pokemon/14/"},{"name":"beedrill","url":"https://pokeapi.co/api/v2/pokemon/15/"},{"name":"pidgey","url":"https://pokeapi.co/api/v2/pokemon/16/"},{"name":"pidgeotto","url":"https://pokeapi.co/api/v2/pokemon/17/"},{"name":"pidgeot","url":"https://pokeapi.co/api/v2/pokemon/18/"},{"name":"rattata","url":"https://pokeapi.co/api/v2/pokemon/19/"},{"name":"raticate","url":"https://pokeapi.co/api/v2/pokemon/20/"}]}"""

        //Response of pokemon?limit=20&offset=1100
        const val LIST_LAST_ELEMENTS = """{"count":1118,"next":null,"previous":"https://pokeapi.co/api/v2/pokemon?offset=1080&limit=20","results":[{"name":"corviknight-gmax","url":"https://pokeapi.co/api/v2/pokemon/10203/"},{"name":"orbeetle-gmax","url":"https://pokeapi.co/api/v2/pokemon/10204/"},{"name":"drednaw-gmax","url":"https://pokeapi.co/api/v2/pokemon/10205/"},{"name":"coalossal-gmax","url":"https://pokeapi.co/api/v2/pokemon/10206/"},{"name":"flapple-gmax","url":"https://pokeapi.co/api/v2/pokemon/10207/"},{"name":"appletun-gmax","url":"https://pokeapi.co/api/v2/pokemon/10208/"},{"name":"sandaconda-gmax","url":"https://pokeapi.co/api/v2/pokemon/10209/"},{"name":"toxtricity-amped-gmax","url":"https://pokeapi.co/api/v2/pokemon/10210/"},{"name":"centiskorch-gmax","url":"https://pokeapi.co/api/v2/pokemon/10211/"},{"name":"hatterene-gmax","url":"https://pokeapi.co/api/v2/pokemon/10212/"},{"name":"grimmsnarl-gmax","url":"https://pokeapi.co/api/v2/pokemon/10213/"},{"name":"alcremie-gmax","url":"https://pokeapi.co/api/v2/pokemon/10214/"},{"name":"copperajah-gmax","url":"https://pokeapi.co/api/v2/pokemon/10215/"},{"name":"duraludon-gmax","url":"https://pokeapi.co/api/v2/pokemon/10216/"},{"name":"eternatus-eternamax","url":"https://pokeapi.co/api/v2/pokemon/10217/"},{"name":"urshifu-single-strike-gmax","url":"https://pokeapi.co/api/v2/pokemon/10218/"},{"name":"urshifu-rapid-strike-gmax","url":"https://pokeapi.co/api/v2/pokemon/10219/"},{"name":"toxtricity-low-key-gmax","url":"https://pokeapi.co/api/v2/pokemon/10220/"}]}"""

        //Response of pokemon?limit=20&offset=500
        const val LIST_INTERMEDIATE = """{"count":1118,"next":"https://pokeapi.co/api/v2/pokemon?offset=520&limit=20","previous":"https://pokeapi.co/api/v2/pokemon?offset=480&limit=20","results":[{"name":"oshawott","url":"https://pokeapi.co/api/v2/pokemon/501/"},{"name":"dewott","url":"https://pokeapi.co/api/v2/pokemon/502/"},{"name":"samurott","url":"https://pokeapi.co/api/v2/pokemon/503/"},{"name":"patrat","url":"https://pokeapi.co/api/v2/pokemon/504/"},{"name":"watchog","url":"https://pokeapi.co/api/v2/pokemon/505/"},{"name":"lillipup","url":"https://pokeapi.co/api/v2/pokemon/506/"},{"name":"herdier","url":"https://pokeapi.co/api/v2/pokemon/507/"},{"name":"stoutland","url":"https://pokeapi.co/api/v2/pokemon/508/"},{"name":"purrloin","url":"https://pokeapi.co/api/v2/pokemon/509/"},{"name":"liepard","url":"https://pokeapi.co/api/v2/pokemon/510/"},{"name":"pansage","url":"https://pokeapi.co/api/v2/pokemon/511/"},{"name":"simisage","url":"https://pokeapi.co/api/v2/pokemon/512/"},{"name":"pansear","url":"https://pokeapi.co/api/v2/pokemon/513/"},{"name":"simisear","url":"https://pokeapi.co/api/v2/pokemon/514/"},{"name":"panpour","url":"https://pokeapi.co/api/v2/pokemon/515/"},{"name":"simipour","url":"https://pokeapi.co/api/v2/pokemon/516/"},{"name":"munna","url":"https://pokeapi.co/api/v2/pokemon/517/"},{"name":"musharna","url":"https://pokeapi.co/api/v2/pokemon/518/"},{"name":"pidove","url":"https://pokeapi.co/api/v2/pokemon/519/"},{"name":"tranquill","url":"https://pokeapi.co/api/v2/pokemon/520/"}]}"""

        //Response of pokemon?limit=20&offset=2000
        const val LIST_OUT_BOUNDS = """{"count":1118,"next":null,"previous":"https://pokeapi.co/api/v2/pokemon?offset=1980&limit=20","results":[]}"""

        //Response of pokemon/ditto or pokemon/132
        const val DETAIL_DITTO = """{"abilities":[{"ability":{"name":"limber","url":"https://pokeapi.co/api/v2/ability/7/"},"is_hidden":false,"slot":1},{"ability":{"name":"imposter","url":"https://pokeapi.co/api/v2/ability/150/"},"is_hidden":true,"slot":3}],"base_experience":101,"forms":[{"name":"ditto","url":"https://pokeapi.co/api/v2/pokemon-form/132/"}],"game_indices":[{"game_index":76,"version":{"name":"red","url":"https://pokeapi.co/api/v2/version/1/"}},{"game_index":76,"version":{"name":"blue","url":"https://pokeapi.co/api/v2/version/2/"}},{"game_index":76,"version":{"name":"yellow","url":"https://pokeapi.co/api/v2/version/3/"}},{"game_index":132,"version":{"name":"gold","url":"https://pokeapi.co/api/v2/version/4/"}},{"game_index":132,"version":{"name":"silver","url":"https://pokeapi.co/api/v2/version/5/"}},{"game_index":132,"version":{"name":"crystal","url":"https://pokeapi.co/api/v2/version/6/"}},{"game_index":132,"version":{"name":"ruby","url":"https://pokeapi.co/api/v2/version/7/"}},{"game_index":132,"version":{"name":"sapphire","url":"https://pokeapi.co/api/v2/version/8/"}},{"game_index":132,"version":{"name":"emerald","url":"https://pokeapi.co/api/v2/version/9/"}},{"game_index":132,"version":{"name":"firered","url":"https://pokeapi.co/api/v2/version/10/"}},{"game_index":132,"version":{"name":"leafgreen","url":"https://pokeapi.co/api/v2/version/11/"}},{"game_index":132,"version":{"name":"diamond","url":"https://pokeapi.co/api/v2/version/12/"}},{"game_index":132,"version":{"name":"pearl","url":"https://pokeapi.co/api/v2/version/13/"}},{"game_index":132,"version":{"name":"platinum","url":"https://pokeapi.co/api/v2/version/14/"}},{"game_index":132,"version":{"name":"heartgold","url":"https://pokeapi.co/api/v2/version/15/"}},{"game_index":132,"version":{"name":"soulsilver","url":"https://pokeapi.co/api/v2/version/16/"}},{"game_index":132,"version":{"name":"black","url":"https://pokeapi.co/api/v2/version/17/"}},{"game_index":132,"version":{"name":"white","url":"https://pokeapi.co/api/v2/version/18/"}},{"game_index":132,"version":{"name":"black-2","url":"https://pokeapi.co/api/v2/version/21/"}},{"game_index":132,"version":{"name":"white-2","url":"https://pokeapi.co/api/v2/version/22/"}}],"height":3,"held_items":[{"item":{"name":"metal-powder","url":"https://pokeapi.co/api/v2/item/234/"},"version_details":[{"rarity":5,"version":{"name":"ruby","url":"https://pokeapi.co/api/v2/version/7/"}},{"rarity":5,"version":{"name":"sapphire","url":"https://pokeapi.co/api/v2/version/8/"}},{"rarity":5,"version":{"name":"emerald","url":"https://pokeapi.co/api/v2/version/9/"}},{"rarity":5,"version":{"name":"firered","url":"https://pokeapi.co/api/v2/version/10/"}},{"rarity":5,"version":{"name":"leafgreen","url":"https://pokeapi.co/api/v2/version/11/"}},{"rarity":5,"version":{"name":"diamond","url":"https://pokeapi.co/api/v2/version/12/"}},{"rarity":5,"version":{"name":"pearl","url":"https://pokeapi.co/api/v2/version/13/"}},{"rarity":5,"version":{"name":"platinum","url":"https://pokeapi.co/api/v2/version/14/"}},{"rarity":5,"version":{"name":"heartgold","url":"https://pokeapi.co/api/v2/version/15/"}},{"rarity":5,"version":{"name":"soulsilver","url":"https://pokeapi.co/api/v2/version/16/"}},{"rarity":5,"version":{"name":"black","url":"https://pokeapi.co/api/v2/version/17/"}},{"rarity":5,"version":{"name":"white","url":"https://pokeapi.co/api/v2/version/18/"}},{"rarity":5,"version":{"name":"black-2","url":"https://pokeapi.co/api/v2/version/21/"}},{"rarity":5,"version":{"name":"white-2","url":"https://pokeapi.co/api/v2/version/22/"}},{"rarity":5,"version":{"name":"x","url":"https://pokeapi.co/api/v2/version/23/"}},{"rarity":5,"version":{"name":"y","url":"https://pokeapi.co/api/v2/version/24/"}},{"rarity":5,"version":{"name":"omega-ruby","url":"https://pokeapi.co/api/v2/version/25/"}},{"rarity":5,"version":{"name":"alpha-sapphire","url":"https://pokeapi.co/api/v2/version/26/"}},{"rarity":5,"version":{"name":"sun","url":"https://pokeapi.co/api/v2/version/27/"}},{"rarity":5,"version":{"name":"moon","url":"https://pokeapi.co/api/v2/version/28/"}},{"rarity":5,"version":{"name":"ultra-sun","url":"https://pokeapi.co/api/v2/version/29/"}},{"rarity":5,"version":{"name":"ultra-moon","url":"https://pokeapi.co/api/v2/version/30/"}}]},{"item":{"name":"quick-powder","url":"https://pokeapi.co/api/v2/item/251/"},"version_details":[{"rarity":50,"version":{"name":"diamond","url":"https://pokeapi.co/api/v2/version/12/"}},{"rarity":50,"version":{"name":"pearl","url":"https://pokeapi.co/api/v2/version/13/"}},{"rarity":50,"version":{"name":"platinum","url":"https://pokeapi.co/api/v2/version/14/"}},{"rarity":50,"version":{"name":"heartgold","url":"https://pokeapi.co/api/v2/version/15/"}},{"rarity":50,"version":{"name":"soulsilver","url":"https://pokeapi.co/api/v2/version/16/"}},{"rarity":50,"version":{"name":"black","url":"https://pokeapi.co/api/v2/version/17/"}},{"rarity":50,"version":{"name":"white","url":"https://pokeapi.co/api/v2/version/18/"}},{"rarity":50,"version":{"name":"black-2","url":"https://pokeapi.co/api/v2/version/21/"}},{"rarity":50,"version":{"name":"white-2","url":"https://pokeapi.co/api/v2/version/22/"}},{"rarity":50,"version":{"name":"x","url":"https://pokeapi.co/api/v2/version/23/"}},{"rarity":50,"version":{"name":"y","url":"https://pokeapi.co/api/v2/version/24/"}},{"rarity":50,"version":{"name":"omega-ruby","url":"https://pokeapi.co/api/v2/version/25/"}},{"rarity":50,"version":{"name":"alpha-sapphire","url":"https://pokeapi.co/api/v2/version/26/"}},{"rarity":50,"version":{"name":"sun","url":"https://pokeapi.co/api/v2/version/27/"}},{"rarity":50,"version":{"name":"moon","url":"https://pokeapi.co/api/v2/version/28/"}},{"rarity":50,"version":{"name":"ultra-sun","url":"https://pokeapi.co/api/v2/version/29/"}},{"rarity":50,"version":{"name":"ultra-moon","url":"https://pokeapi.co/api/v2/version/30/"}}]}],"id":132,"is_default":true,"location_area_encounters":"https://pokeapi.co/api/v2/pokemon/132/encounters","moves":[{"move":{"name":"transform","url":"https://pokeapi.co/api/v2/move/144/"},"version_group_details":[{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"red-blue","url":"https://pokeapi.co/api/v2/version-group/1/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"yellow","url":"https://pokeapi.co/api/v2/version-group/2/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"gold-silver","url":"https://pokeapi.co/api/v2/version-group/3/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"crystal","url":"https://pokeapi.co/api/v2/version-group/4/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"ruby-sapphire","url":"https://pokeapi.co/api/v2/version-group/5/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"emerald","url":"https://pokeapi.co/api/v2/version-group/6/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"firered-leafgreen","url":"https://pokeapi.co/api/v2/version-group/7/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"diamond-pearl","url":"https://pokeapi.co/api/v2/version-group/8/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"platinum","url":"https://pokeapi.co/api/v2/version-group/9/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"heartgold-soulsilver","url":"https://pokeapi.co/api/v2/version-group/10/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"black-white","url":"https://pokeapi.co/api/v2/version-group/11/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"colosseum","url":"https://pokeapi.co/api/v2/version-group/12/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"xd","url":"https://pokeapi.co/api/v2/version-group/13/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"black-2-white-2","url":"https://pokeapi.co/api/v2/version-group/14/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"x-y","url":"https://pokeapi.co/api/v2/version-group/15/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"omega-ruby-alpha-sapphire","url":"https://pokeapi.co/api/v2/version-group/16/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"sun-moon","url":"https://pokeapi.co/api/v2/version-group/17/"}},{"level_learned_at":1,"move_learn_method":{"name":"level-up","url":"https://pokeapi.co/api/v2/move-learn-method/1/"},"version_group":{"name":"ultra-sun-ultra-moon","url":"https://pokeapi.co/api/v2/version-group/18/"}}]}],"name":"ditto","order":203,"species":{"name":"ditto","url":"https://pokeapi.co/api/v2/pokemon-species/132/"},"sprites":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/132.png","back_female":null,"back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/132.png","back_shiny_female":null,"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/132.png","front_female":null,"front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/132.png","front_shiny_female":null,"other":{"dream_world":{"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/132.svg","front_female":null},"official-artwork":{"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/132.png"}},"versions":{"generation-i":{"red-blue":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/back/132.png","back_gray":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/back/gray/132.png","front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/132.png","front_gray":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/red-blue/gray/132.png"},"yellow":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/yellow/back/132.png","back_gray":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/yellow/back/gray/132.png","front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/yellow/132.png","front_gray":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-i/yellow/gray/132.png"}},"generation-ii":{"crystal":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/crystal/back/132.png","back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/crystal/back/shiny/132.png","front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/crystal/132.png","front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/crystal/shiny/132.png"},"gold":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/back/132.png","back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/back/shiny/132.png","front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/132.png","front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/shiny/132.png"},"silver":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/silver/back/132.png","back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/silver/back/shiny/132.png","front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/silver/132.png","front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/silver/shiny/132.png"}},"generation-iii":{"emerald":{"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/emerald/132.png","front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/emerald/shiny/132.png"},"firered-leafgreen":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/firered-leafgreen/back/132.png","back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/firered-leafgreen/back/shiny/132.png","front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/firered-leafgreen/132.png","front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/firered-leafgreen/shiny/132.png"},"ruby-sapphire":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/ruby-sapphire/back/132.png","back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/ruby-sapphire/back/shiny/132.png","front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/ruby-sapphire/132.png","front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iii/ruby-sapphire/shiny/132.png"}},"generation-iv":{"diamond-pearl":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/diamond-pearl/back/132.png","back_female":null,"back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/diamond-pearl/back/shiny/132.png","back_shiny_female":null,"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/diamond-pearl/132.png","front_female":null,"front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/diamond-pearl/shiny/132.png","front_shiny_female":null},"heartgold-soulsilver":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/heartgold-soulsilver/back/132.png","back_female":null,"back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/heartgold-soulsilver/back/shiny/132.png","back_shiny_female":null,"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/heartgold-soulsilver/132.png","front_female":null,"front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/heartgold-soulsilver/shiny/132.png","front_shiny_female":null},"platinum":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/platinum/back/132.png","back_female":null,"back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/platinum/back/shiny/132.png","back_shiny_female":null,"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/platinum/132.png","front_female":null,"front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-iv/platinum/shiny/132.png","front_shiny_female":null}},"generation-v":{"black-white":{"animated":{"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/back/132.gif","back_female":null,"back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/back/shiny/132.gif","back_shiny_female":null,"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/132.gif","front_female":null,"front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/shiny/132.gif","front_shiny_female":null},"back_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/back/132.png","back_female":null,"back_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/back/shiny/132.png","back_shiny_female":null,"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/132.png","front_female":null,"front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/shiny/132.png","front_shiny_female":null}},"generation-vi":{"omegaruby-alphasapphire":{"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vi/omegaruby-alphasapphire/132.png","front_female":null,"front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vi/omegaruby-alphasapphire/shiny/132.png","front_shiny_female":null},"x-y":{"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vi/x-y/132.png","front_female":null,"front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vi/x-y/shiny/132.png","front_shiny_female":null}},"generation-vii":{"icons":{"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/132.png","front_female":null},"ultra-sun-ultra-moon":{"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/ultra-sun-ultra-moon/132.png","front_female":null,"front_shiny":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/ultra-sun-ultra-moon/shiny/132.png","front_shiny_female":null}},"generation-viii":{"icons":{"front_default":"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/132.png","front_female":null}}}},"stats":[{"base_stat":48,"effort":1,"stat":{"name":"hp","url":"https://pokeapi.co/api/v2/stat/1/"}},{"base_stat":48,"effort":0,"stat":{"name":"attack","url":"https://pokeapi.co/api/v2/stat/2/"}},{"base_stat":48,"effort":0,"stat":{"name":"defense","url":"https://pokeapi.co/api/v2/stat/3/"}},{"base_stat":48,"effort":0,"stat":{"name":"special-attack","url":"https://pokeapi.co/api/v2/stat/4/"}},{"base_stat":48,"effort":0,"stat":{"name":"special-defense","url":"https://pokeapi.co/api/v2/stat/5/"}},{"base_stat":48,"effort":0,"stat":{"name":"speed","url":"https://pokeapi.co/api/v2/stat/6/"}}],"types":[{"slot":1,"type":{"name":"normal","url":"https://pokeapi.co/api/v2/type/1/"}}],"weight":40}"""
    }
}
