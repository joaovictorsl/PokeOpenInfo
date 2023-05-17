package me.joaovictorsl.pokeopeninfo

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.joaovictorsl.pokeopeninfo.ui.pokemonlist.PokemonListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecyclerViewTest {

    @get:Rule
    val rule = ActivityScenarioRule(PokemonListActivity::class.java)

    @Test
    fun testScrollAndClick() {
        // Rolar para a célula de índice 10
        Espresso.onView(ViewMatchers.withId(R.id.pokemon_list))
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    10,
                    ViewActions.click()
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.pokemon_detail_name))
            .check(ViewAssertions.matches(ViewMatchers.withText("Metapod")))
    }
}
