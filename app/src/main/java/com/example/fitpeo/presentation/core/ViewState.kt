package com.example.fitpeo.presentation.core

import androidx.paging.PagingData
import com.example.fitpeo.domain.model.Album

sealed class ViewState {
    object Loading : ViewState()
    data class Success(val result: PagingData<Album>) : ViewState()
    data class Failure(val failMessage: String) : ViewState()
}