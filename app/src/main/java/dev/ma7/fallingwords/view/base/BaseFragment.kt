package dev.ma7.fallingwords.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dev.ma7.fallingwords.view.util.Navigation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectNavigationFlow()
    }

    private fun collectNavigationFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationSharedFlow.collectLatest(::navigate)
            }
        }
    }

    protected fun navigate(navigation: Navigation) {
        when (navigation) {
            is Navigation.Pop -> findNavController().popBackStack()
            is Navigation.Direction -> findNavController().navigate(navigation.directions)
        }
    }
}