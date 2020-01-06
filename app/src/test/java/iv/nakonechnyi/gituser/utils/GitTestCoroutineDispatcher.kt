package iv.nakonechnyi.gituser.utils

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object GitTestCoroutineDispatcher : CoroutineDispatcher(){
    override fun dispatch(context: CoroutineContext, block: Runnable) = block.run()
}