package com.practice.main.state

import android.util.Log
import com.hsk.ktx.date.Date
import com.practice.domain.Memo
import kotlinx.collections.immutable.persistentListOf

data class MemoUiState(
    val date: Date,
    val memos: List<UiMemo>,
) {

    val description: String
        get() = memos.joinToString(", ") { it.displayText }

    fun addUiMemoAtLast(memoId: String, userId: String, date: Date): MemoUiState {
        return createNewMemoUiState {
            add(UiMemo.getEmptyMemo(memoId, userId, date))
        }
    }

    fun updateMemo(uiMemo: UiMemo): MemoUiState {
        return createNewMemoUiState {
            val index = indexOfFirst { it.id == uiMemo.id }
            catchIndexError {
                this[index] = uiMemo
            }
        }
    }

    fun deleteUiMemo(uiMemo: UiMemo): MemoUiState {
        return createNewMemoUiState { removeAll { it.id == uiMemo.id } }
    }

    private fun createNewMemoUiState(block: MutableList<UiMemo>.() -> Unit): MemoUiState {
        return this.copy(memos = editMemoList(block))
    }

    private fun editMemoList(block: MutableList<UiMemo>.() -> Unit): List<UiMemo> {
        return memos.toMutableList().apply {
            block()
            Log.d("APPLY", "updated: $this")
        }
    }

    private fun catchIndexError(block: () -> Unit) {
        try {
            block()
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    companion object {
        val EmptyMemoUiState = MemoUiState(
            date = Date.now(),
            memos = persistentListOf(),
        )
    }
}

data class UiMemo(
    val id: String,
    val userId: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val contents: String,
    val isSavedOnRemote: Boolean,
) : MemoPopupElement {
    override val sortOrder: Int
        get() = 2
    override val displayText: String
        get() = contents

    companion object {
        fun getEmptyMemo(
            id: String = "",
            userId: String = "",
            date: Date = Date.now()
        ): UiMemo {
            val (year, month, day) = date
            return UiMemo(
                id = id,
                userId = userId,
                year = year,
                month = month,
                day = day,
                contents = "",
                isSavedOnRemote = false,
            )
        }
    }
}

fun Memo.toUiMemo() = UiMemo(
    id = id,
    userId = userId,
    year = year,
    month = month,
    day = day,
    contents = content,
    isSavedOnRemote = isSavedOnRemote,
)

fun UiMemo.toMemo() = Memo(
    id = id,
    userId = userId,
    year = year,
    month = month,
    day = day,
    content = contents,
    isSavedOnRemote = isSavedOnRemote,
)