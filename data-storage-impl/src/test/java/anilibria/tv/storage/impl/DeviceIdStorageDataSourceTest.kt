package anilibria.tv.storage.impl

import anilibria.tv.storage.common.KeyValueStorage
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test

class DeviceIdStorageDataSourceTest {

    private val KEY = "device_uid"
    private val keyValueStorage = mockk<KeyValueStorage>()
    private val dataSource = DeviceIdStorageDataSourceImpl(keyValueStorage)

    @Test
    fun `get when no data EXPECT new id`() {
        val valueSlot = slot<String>()
        every { keyValueStorage.getValue(KEY) } answers {
            Maybe.fromCallable {
                if (valueSlot.isCaptured) {
                    valueSlot.captured
                } else {
                    null
                }
            }
        }
        every { keyValueStorage.putValue(KEY, capture(valueSlot)) } returns Completable.complete()

        dataSource.getDeviceId().test().assertValue { it == valueSlot.captured }

        verify { keyValueStorage.getValue(KEY) }
        verify { keyValueStorage.putValue(KEY, any()) }
        verify { keyValueStorage.getValue(KEY) }

        confirmVerified(keyValueStorage)
    }

    @Test
    fun `get saved EXPECT always same id`() {
        val valueSlot = slot<String>()
        every { keyValueStorage.getValue(KEY) } answers {
            Maybe.fromCallable {
                if (valueSlot.isCaptured) {
                    valueSlot.captured
                } else {
                    null
                }
            }
        }
        every { keyValueStorage.putValue(KEY, capture(valueSlot)) } returns Completable.complete()

        dataSource.getDeviceId().test().assertValue { it == valueSlot.captured }
        dataSource.getDeviceId().test().assertValue { it == valueSlot.captured }
        dataSource.getDeviceId().test().assertValue { it == valueSlot.captured }

        verify { keyValueStorage.getValue(KEY) }
        verify { keyValueStorage.putValue(KEY, any()) }
        verify { keyValueStorage.getValue(KEY) }
        verify { keyValueStorage.getValue(KEY) }
        verify { keyValueStorage.getValue(KEY) }

        confirmVerified(keyValueStorage)
    }
}