package com.kcrason.appbasic.mvp.model.base


data class BaseArrayObj<T>(
    var data: List<T>? = null
) :
    BaseModel()