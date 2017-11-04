package com.guardafilme.ui.welcome

import com.guardafilme.data.UserRepository
import com.guardafilme.model.WatchedMovie
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertThat
import org.mockito.*
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.hamcrest.core.IsEqual.equalTo

/**
 * Created by lucassantos on 03/11/17.
 */
class WelcomePresenterTest {

    lateinit var showWatchedMoviesArgumentCaptor: ArgumentCaptor<List<WatchedMovie>>

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var welcomeView: WelcomeContract.View

    lateinit var welcomePresenter: WelcomePresenter

    private fun <T> anyObject(): T {
        return Mockito.any<T>()
    }

    lateinit var watchedMovies: List<WatchedMovie>

    inline fun <reified T : Any> argumentCaptor() = ArgumentCaptor.forClass(T::class.java)

    @Before
    fun setupWelcomePresenter() {
        MockitoAnnotations.initMocks(this)

        showWatchedMoviesArgumentCaptor = argumentCaptor<List<WatchedMovie>>()

        watchedMovies = mutableListOf(
                WatchedMovie("1", 1, "Teste 1", "Teste 1", 12345),
                WatchedMovie("2", 2, "Teste 2", "Teste 2", 12345),
                WatchedMovie("3", 3, "Teste 3", "Teste 3", 12345)
        )

        Mockito.`when`(userRepository.getWatchedMovies(anyObject())).thenAnswer { invocation ->
            val listener: (List<WatchedMovie>) -> Unit = invocation.arguments[0] as (List<WatchedMovie>) -> Unit
            listener.invoke(watchedMovies)
        }

        welcomePresenter = WelcomePresenter(userRepository)
        welcomePresenter.attach(welcomeView)
    }

    @Test
    fun loadMovies_shouldWorkCorrectly() {
        welcomePresenter.load()

        // verifica se coloca o loadingView
        verify(welcomeView).hideMoviesList()
        verify(welcomeView).showLoading()

        // verifica se a view recebe a quantidade certa de filmes
        verify(welcomeView).addWatchedMovies(showWatchedMoviesArgumentCaptor.capture())
        assertThat(showWatchedMoviesArgumentCaptor.value.size, equalTo(3))

        // verifica se o loading foi tirado
        verify(welcomeView).hideLoading()
        verify(welcomeView).showMoviesList()
    }

    @Test
    fun clickOnAddMovie_shouldShowSearchMovieUi() {
        welcomePresenter.addMovie()

        verify(welcomeView).showAddMovie()
    }

}