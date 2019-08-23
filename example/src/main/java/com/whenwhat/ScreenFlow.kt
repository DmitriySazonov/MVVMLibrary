package com.whenwhat

import com.whenwhat.fragments.FirstFragment
import com.whenwhat.fragments.InputFragment
import com.whenwhat.framework.navigation.point.ActivityNavigationPoint
import com.whenwhat.framework.navigation.point.Empty
import com.whenwhat.framework.navigation.point.HostActivityNavigationPoint
import java.io.Serializable

object ScreenFlow {
    val SECOND_ACTIVITY = SecondActivityHost
    val MAIN = ActivityNavigationPoint<ParamString>("MainActivity", MainActivity::class.java)
}

object SecondActivityHost : HostActivityNavigationPoint<Empty>("SecondActivity", SecondActivity::class.java) {
    val FIRST_FRAGMENT = fragmentPoint<ParamString>("FirstFragment", FirstFragment::class.java)
    val INPUT_FRAGMENT = fragmentPoint<ParamString>("InputFragment", InputFragment::class.java)
}

data class ParamString(val param: String) : Serializable