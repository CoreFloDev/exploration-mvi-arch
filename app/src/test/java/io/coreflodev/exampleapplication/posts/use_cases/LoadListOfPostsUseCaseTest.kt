package io.coreflodev.exampleapplication.posts.use_cases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.coreflodev.exampleapplication.posts.repo.PostsRepository
import io.reactivex.Observable
import org.junit.Test

class LoadListOfPostsUseCaseTest {

    private val repoMock : PostsRepository = mock()
    private val useCase = LoadListOfPostsUseCase(repoMock)

    @Test
    fun `given an initial action is received when the list of post fails then a result loading and error are returned`() {
        whenever(repoMock.getListOfPosts()).thenReturn(Observable.error(Throwable()))

        Observable.just(Action.InitialAction)
            .compose(useCase())
            .test()
            .assertValues(Result.UiUpdate.Loading, Result.UiUpdate.Error)
    }

    @Test
    fun `given an initial action is received when the list is empty then a result loading is returned`() {
        whenever(repoMock.getListOfPosts()).thenReturn(Observable.empty())

        Observable.just(Action.InitialAction)
            .compose(useCase())
            .test()
            .assertValue(Result.UiUpdate.Loading)
    }

    @Test
    fun `given an initial action is received when the list contains some items then a result loading and display are returned`() {
        whenever(repoMock.getListOfPosts()).thenReturn(Observable.just(listOf(A_POST)))

        Observable.just(Action.InitialAction)
            .compose(useCase())
            .test()
            .assertValues(Result.UiUpdate.Loading, Result.UiUpdate.Display(listOf(A_POST)))
    }

    companion object {

        private val A_POST = PostsRepository.Post("0", "any content")
    }
}