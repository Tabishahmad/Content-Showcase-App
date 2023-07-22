package com.example.fitpeo.presentation.core

sealed class ViewState<out T : Any> {
    data class Loading(val isLoading: Boolean) : ViewState<Nothing>()
    data class Success<out T : Any>(val result: T) : ViewState<T>()
    data class Failure(val failMessage: String) : ViewState<Nothing>()
}