package com.whenwhat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.whenwhat.framework.property.PropertyDelegate
import com.whenwhat.framework.property.tryBindAsSource
import com.whenwhat.framework.property.tryBindAsText
import com.whenwhat.framework.view.MVVMActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import kotlin.system.measureNanoTime

class MainActivity : MVVMActivity<ExampleViewModel>() {

    private val adapter = TextRecyclerAdapter()

    override fun createViewModel(): ExampleViewModel {
        return ExampleViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inc.setOnClickListener {
            viewModel?.increase()
        }
        dec.setOnClickListener {
            viewModel?.decrease()
        }

        next.setOnClickListener {
            navigator?.navigate(SecondActivityHost.FIRST_FRAGMENT, ParamString("Hello"))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onForwardScreenResult(result: Serializable) {
        super.onForwardScreenResult(result)

        Toast.makeText(this, "$result", Toast.LENGTH_SHORT).show()
    }

    override fun bindViewModel(viewModel: ExampleViewModel) {
        super.bindViewModel(viewModel)

        viewModel::counter.tryBindAsText(counter).autoRelease()
        -viewModel::counter2.tryBindAsText(counter2)
        viewModel.elements.tryBindAsSource(adapter)
    }
}
