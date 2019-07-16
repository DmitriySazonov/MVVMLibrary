package com.whenwhat.mvvmlibrary.example

import android.os.Bundle
import com.whenwhat.mvvmlibrary.R
import com.whenwhat.mvvmlibrary.property.tryBind
import com.whenwhat.mvvmlibrary.view.MVVMActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MVVMActivity<ExampleViewModel>() {

    override fun createViewModel(): ExampleViewModel {
        return ExampleViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel::counter.tryBind {
            counter.text = it.toString()
        }
        viewModel::a.tryBind {

        }
        inc.setOnClickListener {
            viewModel.increase()
        }
        dec.setOnClickListener {
            viewModel.decrease()
        }

        next.setOnClickListener {
            navigator.navigate(ScreenFlow.SECOND_ACTIVITY.INPUT_FRAGMENT, ParamString("Hello world"))
            /* val fragment = FirstFragment()
             supportFragmentManager.beginTransaction()
                 .replace(R.id.container, fragment)
                 .addToBackStack(fragment.hashCode().toString())
                 .commit()*/
        }

    }
}
