package com.whenwhat.mvvmlibrary.example

import android.graphics.Point
import com.whenwhat.mvvmlibrary.example.fragments.FirstFragment
import com.whenwhat.mvvmlibrary.example.fragments.InputFragment
import com.whenwhat.mvvmlibrary.navigation.NavigationFragmentNode
import com.whenwhat.mvvmlibrary.navigation.NavigationNode
import java.io.Serializable

object ScreenFlow : NavigationNode() {

    val SECOND_ACTIVITY = SecondActivityHost

    val MAIN = navigationActivity<MainActivity, String, Int>(MainActivity::class.java)

}

object SecondActivityHost : NavigationFragmentNode<SecondActivity>(SecondActivity::class.java) {
    val FIRST_FRAGMENT = navigationFragment<FirstFragment, Long, Int>(FirstFragment::class.java)
    val INPUT_FRAGMENT = navigationFragment<InputFragment, ParamString, String>(InputFragment::class.java)
}

data class ParamString(val param: String) : Serializable