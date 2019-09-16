package com.kcrason.appbasic.network.exception

import java.lang.Exception

/**
 * @author KCrason
 * @date 2019/5/31 10:42
 * @description
 */
open class ApiException(var code: Int, msg: String?) : Exception(msg)