package io.coreflodev.exampleapplication.posts.repo

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.coreflodev.exampleapplication.common.network.posts.Post
import io.coreflodev.exampleapplication.common.network.TypicodeApi
import io.reactivex.Single
import org.junit.Test

class TypicodePostsRepositoryTest {

    private val apiMock: TypicodeApi = mock()
    private val repo = TypicodePostsRepository(apiMock)

    @Test
    fun `when the api return an error then the error is received`() {
        val expectedError = Throwable()
        whenever(apiMock.getPosts()).thenReturn(Single.error(expectedError))

        repo.getListOfPosts()
            .test()
            .assertError(expectedError)
    }

    @Test
    fun `when the api return a list of post then that list is transformed into an expected object`() {
        whenever(apiMock.getPosts()).thenReturn(Single.just(listOf(AN_API_POST)))

        repo.getListOfPosts()
            .test()
            .assertValue(listOf(EXPECTED_POST))
    }

    companion object {

        private const val AN_ID = "anId"
        private const val A_BODY = "aBody"

        private val AN_API_POST = Post(
            id = AN_ID,
            body = A_BODY,
            title = "aTitle",
            userId = "aUserId"
        )

        private val EXPECTED_POST = PostsRepository.Post(
            id = AN_ID,
            content = A_BODY
        )
    }
}