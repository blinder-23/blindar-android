package com.practice.main.state

import com.hsk.ktx.date.Date
import com.practice.domain.Memo
import com.practice.main.main.state.MemoDialogElement
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class UiMemos(
    val date: Date,
    val memos: ImmutableList<UiMemo>,
) {

    val description: String
        get() = memos.joinToString(", ") { it.displayText }

    fun addUiMemoAtLast(memoId: String, userId: String, date: Date): UiMemos {
        return createNewMemoUiState {
            add(UiMemo.getEmptyMemo(memoId, userId, date))
        }
    }

    fun updateMemo(uiMemo: UiMemo): UiMemos {
        return createNewMemoUiState {
            val index = indexOfFirst { it.id == uiMemo.id }
            catchIndexError {
                this[index] = uiMemo
            }
        }
    }

    fun deleteUiMemo(uiMemo: UiMemo): UiMemos {
        return createNewMemoUiState { removeAll { it.id == uiMemo.id } }
    }

    private fun createNewMemoUiState(block: MutableList<UiMemo>.() -> Unit): UiMemos {
        return this.copy(memos = editMemoList(block))
    }

    private fun editMemoList(block: MutableList<UiMemo>.() -> Unit): ImmutableList<UiMemo> {
        return memos.toMutableList()
            .apply { block() }
            .toImmutableList()
    }

    private fun catchIndexError(block: () -> Unit) {
        try {
            block()
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    companion object {
        val EmptyUiMemos = UiMemos(
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
) : MemoDialogElement {
    override val sortOrder: Int
        get() = 2
    override val displayText: String
        get() = contents

    val date: Date
        get() = Date(year, month, day)

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

fun List<Memo>.toUiMemo() = map { it.toUiMemo() }

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