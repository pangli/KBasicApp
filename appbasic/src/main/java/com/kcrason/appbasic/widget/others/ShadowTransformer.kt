package com.kcrason.appbasic.widget.others

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.kcrason.appbasic.adapter.base.CardAdapter


class ShadowTransformer(mViewPager: ViewPager, private val mAdapter: CardAdapter) :
    ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    private var mLastOffset: Float = 0.toFloat()
    private var mScalingEnabled: Boolean = true

    init {
        mViewPager.addOnPageChangeListener(this)
    }

    override fun transformPage(page: View, position: Float) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val realCurrentPosition: Int
        val nextPosition: Int
        val baseElevation = mAdapter.getBaseElevation()
        val realOffset: Float
        val goingLeft = mLastOffset > positionOffset

        if (goingLeft) {
            realCurrentPosition = position + 1
            nextPosition = position
            realOffset = 1 - positionOffset

        } else {
            nextPosition = position + 1
            realCurrentPosition = position
            realOffset = positionOffset
        }

        if (nextPosition > mAdapter.getCount() - 1 || realCurrentPosition > mAdapter.getCount() - 1) {
            val currentCard = mAdapter.getCardViewAt(realCurrentPosition)
            currentCard.animate().scaleY(1.2f)
            currentCard.animate().scaleX(1.2f)
            return
        }

        val currentCard = mAdapter.getCardViewAt(realCurrentPosition)

        if (mScalingEnabled) {
            currentCard.scaleX = (1 + 0.2 * (1 - realOffset)).toFloat()
            currentCard.scaleY = (1 + 0.2 * (1 - realOffset)).toFloat()
        }
        currentCard.cardElevation = baseElevation + (baseElevation
                * (CardAdapter.MAX_ELEVATION_FACTOR - 1) * (1 - realOffset))

        val nextCard = mAdapter.getCardViewAt(nextPosition)

        if (mScalingEnabled) {
            nextCard.scaleX = (1 + 0.2 * (realOffset)).toFloat()
            nextCard.scaleY = (1 + 0.2 * (realOffset)).toFloat()
        }
        nextCard.cardElevation = ((baseElevation + (baseElevation
                * (CardAdapter.MAX_ELEVATION_FACTOR - 1) * (realOffset))))

        mLastOffset = positionOffset
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}
