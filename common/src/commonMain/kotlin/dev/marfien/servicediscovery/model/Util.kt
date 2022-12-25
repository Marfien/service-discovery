package dev.marfien.servicediscovery.model

data class TopicGroup(val topic: String, val services: List<Service>)

data class Pagination(val page: Int, val pageSize: Int)
