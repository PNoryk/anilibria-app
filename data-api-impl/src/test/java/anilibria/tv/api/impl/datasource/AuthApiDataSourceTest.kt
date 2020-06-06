package anilibria.tv.api.impl.datasource

import anilibria.tv.api.impl.converter.AuthConverter
import anilibria.tv.api.impl.service.AuthService
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import org.junit.Test

//todo дописать тесты, когда будет готово апи
class AuthApiDataSourceTest {

    private val service = mockk<AuthService>()
    private val converter = mockk<AuthConverter>()
    private val dataSource = AuthApiDataSourceImpl(service, converter)

    //@Test
    fun `signin EXPECT success`() {
        val params = mapOf(
            "query" to "login",
            "mail" to "login",
            "passwd" to "password",
            "fa2code" to "fa2code"
        )
    }
}