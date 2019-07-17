# MVVMLibrary

Реализации паттерна MVVM и навигации.
<b>Навигация</b>
Для навигации вначале нужно описать наш ScreenFlow. Корнем навигации должен быть объект NavigationNode который имеет метод
`navigationActivity` для создания объекта `NavigationPoint<IN, OUT>`. Любой экран на который может осуществляться навигаци 
является объектом `NavigationPoint`. Для навигации по фрагментам нужно использовать NavigationFragmentNode, в конструктор которого нужно передать `Class<HostActivity>` в которой будут размещаться описанные фрагменты. 
```kotlin
object ScreenFlow : NavigationNode() {
    val SECOND_ACTIVITY = SecondActivityHost()
    val MAIN = navigationActivity<MainActivity, String, Int>(MainActivity::class.java)
}

class SecondActivityHost : NavigationFragmentNode<SecondActivity>(SecondActivity::class.java) {
    val FIRST_FRAGMENT = navigationFragment<FirstFragment, Long, Int>(FirstFragment::class.java)
    val INPUT_FRAGMENT = navigationFragment<InputFragment, InputParam, String>(InputFragment::class.java)
}
```

Предпологается что все объявленые точки навигации будут объеденены в одном объекте, в моем случе это `ScreenFlow`. Дальше навигация осуществляется с помощью `Navigator` который есть в `MVVMActivity` и `MVVMFragment`(О них позже). Предположим мы находимся в `MainActivity` и хотим перейти на `InputFragment` который должен разпологаться в `SecondActivity`(как описано выше). Код будет выглядять одни из следующих образов.
```kotlin
navigator.navigate(ScreenFlow.SECOND_ACTIVITY.INPUT_FRAGMENT, InputParam("Hello world"))
```
