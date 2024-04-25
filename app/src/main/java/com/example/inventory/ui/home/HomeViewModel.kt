package com.example.inventory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(itemsRepository: ItemsRepository) : ViewModel() {

    private val _homeUiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState())

    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    init {
        itemsRepository.getAllItemsStream()
            .map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), // Adjust timeout if necessary
                initialValue = HomeUiState()
            )
            .onEach { _homeUiState.value = it }
            .launchIn(viewModelScope)
    }

    fun searchQuery(query: String) {
        // Fetch all items from UI state
        val allItems = homeUiState.value.itemList

        // Filter items based on the query
        val filteredItems = allItems.filter { it.name.contains(query, ignoreCase = true) }

        // Update the UI state with the filtered items
        _homeUiState.value = HomeUiState(filteredItems)
    }
}


/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<Item> = listOf())
