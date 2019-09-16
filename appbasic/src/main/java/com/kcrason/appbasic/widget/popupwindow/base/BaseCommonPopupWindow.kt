package com.kcrason.appbasic.widget.popupwindow.base

import android.content.Context
import android.view.LayoutInflater

/**
 * @author KCrason
 * @date 2019/5/17 21:55
 * @description
 */
abstract class BaseCommonPopupWindow(context: Context, layoutId: Int) :
    BasePopupWindow(context, LayoutInflater.from(context).inflate(layoutId, null))