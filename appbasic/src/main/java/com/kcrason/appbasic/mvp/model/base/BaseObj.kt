package com.kcrason.appbasic.mvp.model.base


data class BaseObj<T>(
    var data: T? = null
) : BaseModel()