package dev.marfien.servicediscovery.client.mutation

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.Mutation
import com.benasher44.uuid.Uuid
import dev.marfien.servicediscovery.client.model.ServiceReturnTypeBuilder
import dev.marfien.servicediscovery.json.JsonAdapter
import dev.marfien.servicediscovery.json.JsonReader
import dev.marfien.servicediscovery.model.ServiceInput
import dev.marfien.servicediscovery.model.ServiceType
import kotlin.random.Random

class RegisterServiceMutation(
    service: ServiceInput,
    block: ServiceReturnTypeBuilder.() -> Unit
) : SDMutation<RegisterServiceData>(
    CustomSDMutationBuilder().registerService(service, block)
) {

    override fun adapter(): Adapter<RegisterServiceData> = RegisterServiceDataAdapter

    override fun id(): String = id.toString()

    companion object {

        val id = Uuid(Random.nextLong(), Random.nextLong())

    }

}

data class RegisterServiceData(
    val service: ServiceType
) : Mutation.Data

object RegisterServiceDataAdapter : JsonAdapter<RegisterServiceData> {

    override fun fromJson(reader: JsonReader): RegisterServiceData = RegisterServiceData(
        ServiceType.fromJson(reader)
    )
}