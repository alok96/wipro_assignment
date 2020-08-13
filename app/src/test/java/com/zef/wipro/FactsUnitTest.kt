package com.zef.wipro

import android.app.Application
import android.content.Context
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.zef.wipro.adapter.FactsAdapter
import com.zef.wipro.repository.FactsRepository
import com.zef.wipro.request.response.Facts
import com.zef.wipro.viewmodel.FactsViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

class FactsUnitTest {
    @Mock
    private lateinit var repository: FactsRepository

    @Mock
    private lateinit var context: Application


    private lateinit var viewModel: FactsViewModel

    @Mock
    private lateinit var factsLiveData: LiveData<List<Facts>>

    private lateinit var isViewLoadingObserver: Observer<Boolean>
    private lateinit var onMessageErrorObserver: Observer<Any>
    private lateinit var emptyListObserver: Observer<Boolean>
    private lateinit var onFactsObserver: Observer<List<Facts>>
    private lateinit var factsList: List<Facts>

    @Captor
    private lateinit var factsObserverCaptor: ArgumentCaptor<Observer<List<Facts>>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        `when`<Context>(context.applicationContext).thenReturn(context)
        viewModel = FactsViewModel(repository)
        setupObservers()
    }

    @Test
    fun fetchFactsData() {
        with(viewModel) {
            fetchData()
            isViewLoading.observeForever(isViewLoadingObserver)
            facts.observeForever(onFactsObserver)
        }
    }

    @Test
    fun refreshFactsData() {
        with(viewModel) {
            refresh()
            isViewLoading.observeForever(isViewLoadingObserver)
            facts.observeForever(onFactsObserver)
        }
    }

    @Test
    fun `displays facts list when available`() {
        verify(factsLiveData).observe(
            ArgumentMatchers.any(LifecycleOwner::class.java),
            factsObserverCaptor.capture()
        )
        factsObserverCaptor.value.onChanged(factsList)
        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertTrue(viewModel.facts.value?.size == 3)
    }

    private fun setupObservers() {
        isViewLoadingObserver = mock(Observer::class.java) as Observer<Boolean>
        onMessageErrorObserver = mock(Observer::class.java) as Observer<Any>
        emptyListObserver = mock(Observer::class.java) as Observer<Boolean>
        onFactsObserver = mock(Observer::class.java) as Observer<List<Facts>>
    }
}

