package com.kcrason.appbasic.mvp.model.base


open class BaseModel(
    var code: Int = 2000,
    var msg: String = "success"
) {
    fun isOk(): Boolean = (code == 0 || code == 2000 || code == 1)
}