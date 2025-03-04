package com.example.overlay.astronomyappnodependencies.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    val ioDispatcher: CoroutineContext
    val mainDispatcher: CoroutineContext
    val defaultDistance: CoroutineContext
}

class CoroutineContextDispatcherProvider: CoroutineContextProvider {
    override val ioDispatcher: CoroutineContext = Dispatchers.IO
    override val mainDispatcher: CoroutineContext = Dispatchers.Main
    override val defaultDistance: CoroutineContext = Dispatchers.Default
}