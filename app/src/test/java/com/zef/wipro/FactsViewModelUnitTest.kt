package com.zef.wipro

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zef.wipro.constants.OperationCallback
import com.zef.wipro.data.Rows
import com.zef.wipro.repository.FactsRepository
import com.zef.wipro.request.response.Facts
import com.zef.wipro.viewmodel.FactsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class FactsViewModelUnitTest {
    @Mock
    private lateinit var repository: FactsRepository

    @Mock
    private lateinit var context: Application

    @Captor
    private lateinit var operationCallbackCaptor: ArgumentCaptor<OperationCallback<Facts>>

    private lateinit var viewModel: FactsViewModel
    private lateinit var isViewLoadingObserver: Observer<Boolean>
    private lateinit var onMessageErrorObserver: Observer<Any>
    private lateinit var emptyListObserver: Observer<Boolean>
    private lateinit var onRenderFactsObserver: Observer<List<Facts>>

    private lateinit var factsEmptyList: List<Facts>
    private lateinit var factsList: List<Facts>
    private lateinit var rowList: List<Rows>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`<Context>(context.applicationContext).thenReturn(context)

        viewModel = FactsViewModel(repository)

        mockData()
        setupObservers()
    }

    @Test
    fun `facts with ViewModel and Repository returns empty data`() {
        with(viewModel) {
            fetchData()
            isViewLoading.observeForever(isViewLoadingObserver)
            isEmptyList.observeForever(emptyListObserver)
            facts.observeForever(onRenderFactsObserver)
        }
        verify(repository, times(1)).fetchData(capture(operationCallbackCaptor), true)
        operationCallbackCaptor.value.onSuccess(factsEmptyList)

        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertTrue(viewModel.isEmptyList.value == true)
        Assert.assertTrue(viewModel.facts.value?.size == 0)
    }

    @Test
    fun `facts with ViewModel and Repository returns full data`() {
        with(viewModel) {
            fetchData()
            isViewLoading.observeForever(isViewLoadingObserver)
            facts.observeForever(onRenderFactsObserver)
        }

        verify(repository, times(1)).fetchData(capture(operationCallbackCaptor), true)
        operationCallbackCaptor.value.onSuccess(factsList)
        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertTrue(viewModel.facts.value?.size == 3)
    }

    @Test
    fun `facts with ViewModel and Repository returns an error`() {
        with(viewModel) {
            fetchData()
            isViewLoading.observeForever(isViewLoadingObserver)
            onMessageError.observeForever(onMessageErrorObserver)
        }
        verify(repository, times(1)).fetchData(capture(operationCallbackCaptor), true)
        operationCallbackCaptor.value.onError("An error occurred")
        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertNotNull(viewModel.onMessageError.value)
    }

    private fun setupObservers() {
        isViewLoadingObserver = mock(Observer::class.java) as Observer<Boolean>
        onMessageErrorObserver = mock(Observer::class.java) as Observer<Any>
        emptyListObserver = mock(Observer::class.java) as Observer<Boolean>
        onRenderFactsObserver = mock(Observer::class.java) as Observer<List<Facts>>
    }

    private fun mockData() {
        factsEmptyList = emptyList()
        val rowMockList: MutableList<Rows> = mutableListOf()
        rowMockList.add(Rows("T1", "D1", "IH1"))
        rowMockList.add(Rows("T2", "D2", "IH2"))
        rowMockList.add(Rows("T3", "D3", "IH3"))
        rowList = rowMockList.toList()
        val factsMockList: MutableList<Facts> = mutableListOf()
        factsMockList.add(Facts(rowList, "title"))
        factsList = factsMockList.toList()
    }
}

