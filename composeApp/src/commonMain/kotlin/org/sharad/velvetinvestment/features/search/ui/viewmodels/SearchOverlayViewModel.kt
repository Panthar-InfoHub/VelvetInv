package org.sharad.velvetinvestment.features.search.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.mutualfund.domain.models.MutualFundDomain
import org.sharad.velvetinvestment.features.mutualfund.domain.usecases.GetMutualFundSearchResultUseCase
import org.sharad.velvetinvestment.features.search.domain.model.RecentSearch
import org.sharad.velvetinvestment.features.search.domain.usecases.ClearRecentSearchesUseCase
import org.sharad.velvetinvestment.features.search.domain.usecases.DeleteRecentSearchUseCase
import org.sharad.velvetinvestment.features.search.domain.usecases.ObserveRecentSearchesUseCase
import org.sharad.velvetinvestment.features.search.domain.usecases.SaveRecentSearchUseCase

data class SearchOverlayUiState(
    val query: String = "",
    val recentSearches: List<RecentSearch> = emptyList(),
    /** Funds matching the typed term, fetched once typing pauses; at most [SUGGESTION_LIMIT]. */
    val suggestions: List<MutualFundDomain> = emptyList(),
    val isLoadingSuggestions: Boolean = false
) {
    /** The live results sit under the field only while there's a term long enough to search. */
    val showSuggestions: Boolean
        get() = query.trim().length >= MIN_SUGGESTION_QUERY_LENGTH &&
                (isLoadingSuggestions || suggestions.isNotEmpty())

    val canSubmit: Boolean
        get() = query.isNotBlank()

    /** The recents card is hidden entirely rather than shown empty on a first run. */
    val showRecents: Boolean
        get() = recentSearches.isNotEmpty()
}

sealed interface SearchOverlayEvent {
    data class OnQueryChange(val query: String) : SearchOverlayEvent

    /** Keyboard search action, or the magnifier in the field. */
    data object OnSubmit : SearchOverlayEvent

    /** Tapping a recent term searches it immediately rather than only filling the field. */
    data class OnRecentClick(val query: String) : SearchOverlayEvent
    data class OnRecentDelete(val query: String) : SearchOverlayEvent
    data object OnClearRecents : SearchOverlayEvent
}

sealed interface SearchOverlayEffect {
    /** The term has already been recorded by the time this is emitted. */
    data class Search(val query: String) : SearchOverlayEffect
}

/**
 * Backs the search overlay: the field, the recent terms and what happens when one is run.
 *
 * The overlay does no searching of its own — it hands the term back through [effect] and the
 * caller navigates. All this owns is the recents list.
 */
class SearchOverlayViewModel(
    observeRecentSearches: ObserveRecentSearchesUseCase,
    private val saveRecentSearch: SaveRecentSearchUseCase,
    private val deleteRecentSearch: DeleteRecentSearchUseCase,
    private val clearRecentSearches: ClearRecentSearchesUseCase,
    private val getMutualFundSearchResult: GetMutualFundSearchResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchOverlayUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SearchOverlayEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeRecentSearches().collect { searches ->
                _uiState.update { it.copy(recentSearches = searches) }
            }
        }
        observeQueryForSuggestions()
    }

    /**
     * Fetches the live results once typing pauses for [SUGGESTION_DEBOUNCE_MS]. A newer term
     * cancels a fetch still in flight, so results never land for a term already replaced. Failures
     * are quiet — the suggestions just stay empty and the full search still works on submit.
     */
    @OptIn(FlowPreview::class)
    private fun observeQueryForSuggestions() {
        viewModelScope.launch {
            _uiState
                .map { it.query.trim() }
                .distinctUntilChanged()
                .debounce(SUGGESTION_DEBOUNCE_MS)
                .collectLatest { term ->
                    if (term.length < MIN_SUGGESTION_QUERY_LENGTH) {
                        _uiState.update { it.copy(suggestions = emptyList(), isLoadingSuggestions = false) }
                        return@collectLatest
                    }

                    _uiState.update { it.copy(isLoadingSuggestions = true) }
                    val response = getMutualFundSearchResult(
                        search = term,
                        page = 1,
                        limit = SUGGESTION_LIMIT
                    )
                    _uiState.update {
                        it.copy(
                            isLoadingSuggestions = false,
                            suggestions = (response as? NetworkResponse.Success)
                                ?.data?.items?.take(SUGGESTION_LIMIT)
                                .orEmpty()
                        )
                    }
                }
        }
    }

    fun handleEvent(event: SearchOverlayEvent) {
        when (event) {
            is SearchOverlayEvent.OnQueryChange ->
                _uiState.update { it.copy(query = event.query) }

            SearchOverlayEvent.OnSubmit -> submit(_uiState.value.query)

            is SearchOverlayEvent.OnRecentClick -> submit(event.query)

            is SearchOverlayEvent.OnRecentDelete -> viewModelScope.launch {
                deleteRecentSearch(event.query)
            }

            SearchOverlayEvent.OnClearRecents -> viewModelScope.launch {
                clearRecentSearches()
            }
        }
    }

    /**
     * Records the term first, then hands it on. Saving before navigating means a term is in the
     * list even if the user comes straight back — the recents list tracks what was searched, not
     * what the search returned.
     */
    private fun submit(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return

        viewModelScope.launch {
            saveRecentSearch(trimmed)
            _effect.send(SearchOverlayEffect.Search(trimmed))
        }
    }

    /** Clears the field so reopening the overlay does not start on the previous term. */
    fun resetQuery() {
        _uiState.update { it.copy(query = "", suggestions = emptyList(), isLoadingSuggestions = false) }
    }

    /**
     * A live result was tapped: the typed term is recorded like a submitted search, and the
     * caller opens the fund. Saving is fire-and-forget, so the navigation isn't held up.
     */
    fun onSuggestionOpened() {
        val trimmed = _uiState.value.query.trim()
        if (trimmed.isNotEmpty()) viewModelScope.launch { saveRecentSearch(trimmed) }
        resetQuery()
    }
}

/** Live results shown under the field; the endpoint's page size for them. */
const val SUGGESTION_LIMIT = 6
private const val SUGGESTION_DEBOUNCE_MS = 350L
private const val MIN_SUGGESTION_QUERY_LENGTH = 2
