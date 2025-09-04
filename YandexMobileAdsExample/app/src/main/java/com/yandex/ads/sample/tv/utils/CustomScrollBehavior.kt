package com.yandex.ads.sample.tv.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.BringIntoViewSpec
import androidx.compose.foundation.gestures.LocalBringIntoViewSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BringPartiallyVisibleItemIntoViewWithPeek(
    containerPeekFraction: Float = 0.05f,
    content: @Composable () -> Unit,
) {
    val bringIntoViewSpec = remember(containerPeekFraction) {
        object : BringIntoViewSpec {
            override fun calculateScrollDistance(
                offset: Float,
                size: Float,
                containerSize: Float
            ): Float {
                val gap = containerSize * containerPeekFraction
                val trailingEdge = offset + size

                return when {
                    isTrailingEdgeOverflow(trailingEdge, containerSize) -> {
                        getTrailingOverflowScroll(offset, size, containerSize, gap)
                    }
                    isLeadingEdgeOverflow(offset) -> {
                        getLeadingOverflowScroll(offset, size, containerSize, gap)
                    }
                    needsTrailingGapAdjustment(trailingEdge, containerSize, gap) -> {
                        getTrailingGapAdjustment(trailingEdge, containerSize, gap)
                    }
                    needsLeadingGapAdjustment(offset, gap) -> {
                        getLeadingGapAdjustment(offset, gap)
                    }
                    else -> 0f
                }
            }

            private fun isTrailingEdgeOverflow(
                trailingEdge: Float,
                containerSize: Float
            ) = trailingEdge > containerSize

            private fun isLeadingEdgeOverflow(offset: Float) = offset < 0

            private fun needsTrailingGapAdjustment(
                trailingEdge: Float,
                containerSize: Float,
                gap: Float
            ): Boolean {
                val spaceAfter = containerSize - trailingEdge
                return spaceAfter in 0f..(gap - 1)
            }

            private fun needsLeadingGapAdjustment(offset: Float, gap: Float): Boolean {
                return offset in 0f..(gap - 1)
            }

            private fun getTrailingOverflowScroll(
                offset: Float,
                size: Float,
                containerSize: Float,
                gap: Float
            ): Float {
                val trailingEdge = offset + size
                return if (size <= containerSize - gap) {
                    trailingEdge - (containerSize - gap)
                } else {
                    trailingEdge - containerSize
                }
            }

            private fun getLeadingOverflowScroll(
                offset: Float,
                size: Float,
                containerSize: Float,
                gap: Float
            ) = if (size <= containerSize - gap) offset - gap else offset

            private fun getTrailingGapAdjustment(
                trailingEdge: Float,
                containerSize: Float,
                gap: Float
            ): Float {
                val spaceAfter = containerSize - trailingEdge
                return gap - spaceAfter
            }

            private fun getLeadingGapAdjustment(offset: Float, gap: Float) = offset - gap
        }
    }

    CompositionLocalProvider(
        LocalBringIntoViewSpec provides bringIntoViewSpec,
        content = content,
    )
}
