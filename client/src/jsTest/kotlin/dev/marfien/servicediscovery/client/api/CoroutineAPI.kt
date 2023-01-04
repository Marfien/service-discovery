package dev.marfien.servicediscovery.client.api

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

@OptIn(DelicateCoroutinesApi::class)
actual fun runTest(block: suspend () -> Unit): dynamic = GlobalScope.promise { block() }