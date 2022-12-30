package dev.marfien.servicediscovery.model

interface Network {

    val host: String?
    val port: Int?

    fun toInput(): NetworkInput = when(this) {
        is NetworkInput -> this
        is NetworkType -> NetworkInput(this.host!!, this.port!!)
        else -> throw NotImplementedError()
    }

    fun toType(): NetworkType = when(this) {
        is NetworkType -> this
        is NetworkInput -> NetworkType(this.host!!, this.port!!)
        else -> throw NotImplementedError()
    }

    companion object {

        fun createInputData(host: String, port: Int) = NetworkInput(host, port)

        fun createType(host: String?, port: Int?) = NetworkType(host, port)

    }

}

data class NetworkInput internal constructor(
    override val host: String,
    override val port: Int
) : Network, InputType

data class NetworkType internal constructor(
    override val host: String?,
    override val port: Int?
) : Network