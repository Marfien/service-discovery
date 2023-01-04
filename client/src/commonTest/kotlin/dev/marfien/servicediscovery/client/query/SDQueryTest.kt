package dev.marfien.servicediscovery.client.query

import dev.marfien.servicediscovery.client.api.runTest
import dev.marfien.servicediscovery.client.execute
import dev.marfien.servicediscovery.client.model.ServiceReturnTypeBuilder
import kotlin.test.Test
import kotlin.test.asserter

@Test
fun testCustomQuery() = with(asserter) {
    runTest {
        fun ServiceReturnTypeBuilder.full() {
            withId()
            withTopic()
            withNetwork {
                withHost()
                withPort()
            }
        }

        val query = SDQuery.create {
            findAllServices {
                full()
            }
            findAllServicesByHost(host = "localhost") {
                withNetwork {
                    withHost()
                }
            }
        }

        val response = query.execute()
        val data = response.dataAssertNoErrors

        assertTrue("FindAllServices is empty", data.findAllServices.isNotEmpty())
        assertNotNull("Service in FindAllServices has no id", data.findAllServices.first().id)

        assertTrue("FindAllServicesByHost is empty", data.findAllServicesByHost.isNotEmpty())
        assertNotNull("Service.network.host is null in FindAllServicesByHost", data.findAllServicesByHost.first().network?.host)
    }
}