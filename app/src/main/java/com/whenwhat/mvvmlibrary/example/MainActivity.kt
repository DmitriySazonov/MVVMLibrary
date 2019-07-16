package com.whenwhat.mvvmlibrary.example

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.whenwhat.mvvmlibrary.R
import com.whenwhat.mvvmlibrary.navigation.NavigationAction
import com.whenwhat.mvvmlibrary.property.tryBind
import com.whenwhat.mvvmlibrary.property.tryBindAsSource
import com.whenwhat.mvvmlibrary.property.tryBindAsText
import com.whenwhat.mvvmlibrary.view.MVVMActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MVVMActivity<ExampleViewModel>() {

    private val adapter = TextRecyclerAdapter()

    override fun createViewModel(): ExampleViewModel {
        return ExampleViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inc.setOnClickListener {
            viewModel.increase()
        }
        dec.setOnClickListener {
            viewModel.decrease()
        }

        next.setOnClickListener {
            NavigationAction(
                ScreenFlow.SECOND_ACTIVITY.INPUT_FRAGMENT.identifier,
                ParamString("Hello world")
            ).also {
                navigator.navigate(it)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun bindViewMode(viewModel: ExampleViewModel) {
        super.bindViewMode(viewModel)
        viewModel::counter.tryBindAsText(counter).autoRelease()
        -viewModel::counter2.tryBindAsText(counter2)
        viewModel.elements.tryBindAsSource(adapter)
    }
}
