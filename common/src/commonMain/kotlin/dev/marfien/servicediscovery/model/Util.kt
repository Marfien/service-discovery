package dev.marfien.servicediscovery.model

data class TopicGroup(
    val topic: String?,
    val services: List<ServiceType>?
) {

    companion object {

        fun create(topic: String, services: List<ServiceType>?) =
            TopicGroup(topic, services)

    }

}


data class PaginationInput(
    val page: Int,
    val pageSize: Int
) : InputType {

    override fun toDocument(): String = "{ page: $page pageSize: $pageSize }"

    companion object {

        fun create(page: Int, pageSize: Int) =
            PaginationInput(page, pageSize)

    }

}