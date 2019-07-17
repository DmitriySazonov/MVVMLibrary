# MVVMLibrary

## Реализации паттерна MVVM и навигации.
### Навигация
Для навигации вначале нужно описать наш ScreenFlow. Корнем навигации должен быть объект NavigationNode который имеет метод
`navigationActivity` для создания объекта `NavigationPoint<IN, OUT>`, где `IN` - входной параметр `OUT` - возвращаемый параметр. Любой экран на который может осуществляться навигаци 
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
При этом при навигации у нас не получится передать ничего кроме `InputParam`, таким образом наша навигация является строго типизированной. Второй вариант не является строго типизированым и нужен для релизации гибкой навигации. Например если мы хотим сообщить следующему экрану куда ему навигироваться после своего завершения.
```kotlin
NavigationAction(
    ScreenFlow.SECOND_ACTIVITY.INPUT_FRAGMENT.identifier,
    InputParam("Hello world")
).also {
    navigator.navigate(it)
}
```
Как видно из кода мы просим навигатор открыть `InputFragment`, при этом при навигации он сам понимает что в данный момент находится не на той активити которая "владеет" этим фрагменто, и вначале навигируется на нужную аквтивити, только после этого открывает нужный фрагмент.
У каждой точки навигации есть identifier(уникальный ключ), по которому можно навигироваться. Так как любой параметр передаваемый на экран должен быть `Serializable`, а сам `identifier` - Long, `NavigationAction` является `Serializable` объектом и его можно сохранить в методах `onSaveInstance`. Таким образом легко реализовывать отложеную навигацию.

При этом все операции навигации логируются в Debug под тегом `NavigationStack`. Так выглядит логирвания навигации описаной выше.
```
2019-07-17 12:00:06.815 17101-17101/com.whenwhat.mvvmlibrary D/NavigationStack: ScreenStack: 
    ActivityScreenMetaData(name=com.whenwhat.mvvmlibrary.example.MainActivity, screenCode=2, enterParams=null, prevScreenCode=null)
    ====================
    ActivityScreenMetaData(name=com.whenwhat.mvvmlibrary.example.SecondActivity, screenCode=3, enterParams=com.whenwhat.mvvmlibrary.navigation.NavigationAction@2a4515f, PointMetaData(prevScreenCode=2), prevScreenCode=2)
    ====================
    FragmentScreenMetaData(hostedOnActivity=ActivityScreenMetaData(name=com.whenwhat.mvvmlibrary.example.SecondActivity, screenCode=3, enterParams=com.whenwhat.mvvmlibrary.navigation.NavigationAction@2a4515f, PointMetaData(prevScreenCode=2), prevScreenCode=2), name=com.whenwhat.mvvmlibrary.example.fragments.InputFragment, screenCode=4, enterParams=com.whenwhat.mvvmlibrary.example.InputParam@d23cb0, PointMetaData(prevScreenCode=2), prevScreenCode=2)

```
Видно что первым идет `MainActivity` затем открывается `SecondActivity` потому что именно она содержит в себе `InputFragment`, и только затем открывается сам `InputFragment`. У каждого экрана есть набор полей которые полезен для отлаживания и понимания как же мы попали на экран(например переданный параметр). У фрагментов по мимо обычной информации так есть поле `hostedOnActivity` что бы было понятно на какой activity был открыт фрагмент.

### MVVM
То что касается бизнес логики еще не сделано, а вот о `ViewModel` и `View` ниже.
В качестве `View` тут выступают `MVVMActivity<VM : MVVMViewModel>` и `MVVMFragment<VM : MVVMViewModel>`. Еще есть `MVVMHostActivity<VM : MVVMHostViewModel>`, но она мало чем отличается от `MVVMActivity`, по сути добаляется лишь один новый метод `getContainerId`. По названиям я думаю и так понятно для чего нужен каждый класс, при это функциональность у них полностью одинакова. В них происходит управелние жизненым циклом ViewModel(обработка поворота экрана, сохранения состояния).

Сначала посмотрим поля, их не много и по сути интересуют нас только два последних.

```kotlin
val name: String // Имя экрана
val screenCode: Int // Код экрана
val prevScreenCode: Int? // Код предыдущего экрана. Может быть null если экран первый.
val screenMetaData: ScreenMetaData // Мета данные экрана, нужно для логирования навигации
val pointMetaData: PointMetaData // Мета данные точки навигации

val navigator: Navigator // Навигатор. У каждого экрана совой instance
val viewModel: VM // ViewModel экрана.
```

Есть один абстрактный метод, который как бы напрашивается с самого начала.
```kotlin
fun createViewModel(): VM
```
В нем как можно заметить нам нужно создать ViewModel когда нас попросят. Вызов его происходит единожды при создании экрана(если такой ViewModel еще нет), при переворотах соответсвенно он не вызывается.

Дальше идут методы которые мы можем переопределять.
```kotlin
/*
* Время жизни ViewModel. LOCAL - уничтожится после смерти экрана, GLOBAL - останется после смерти экрана и прикрепится при 
* следующем окртытии этого экрана.
*/
fun getViewModelLifeType(): ViewModelLifeType
fun onForwardScreenResult(data: Serializable) // Данные возвращенные открытым экраном
fun bindViewMode(viewModel: VM) // метод для привязки данных из ViewModel. Вызовется при создании View
fun unBindViewMode(viewModel: VM) // метод для отвязки данных из ViewModel. Вызовется при уничтожении View
```
Вообще методом unBindViewMode можно не пользоваться, все связи могут оборваться автоматически во избежании LeakMemory, об это ниже.

### Property
Основной смысл MVVM это связывание данных. Часто для этого используют RX(<img src="https://d50m6q67g4bn3.cloudfront.net/avatars/29116fa8-eb50-4994-8275-928a20d57492_1523461216021" width="24px" height= "24px"/>). В текущей реализации используются делегаты. Использование выглядит так.
```kotlin
class MainActivity : MVVMActivity<MainViewModel>() {

    private val adapter = TextRecyclerAdapter()

    override fun createViewModel(): MainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inc.setOnClickListener(viewModel::increase)
        dec.setOnClickListener(viewModel::decrease)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun bindViewMode(viewModel: MainViewModel) {
        super.bindViewMode(viewModel)
        viewModel::counter.tryBindAsText(counter).autoRelease()
        -viewModel.elements.tryBindAsSource(adapter)
    }
}
```
Обратите внимание в методе bindViewMode при связывании данных в одном случае в конце вызван метод `autoRelease` а в другом в начале выражения стоит `-`. Смысл у эти действий один и тот же(на самом деле это один и тот же метод), мы говорим экрану что бы при уничтожении View разорвались и связи во избежании утечек памяти. Первый вариант более понятен, второй короче, решать вам. Вообще метод `tryBind` на основе которого написаны расшерения `tryBindAsText` и `tryBindAsSource`, возращает объект типа Subscription которым при желании можно управлять самому. В примери есть только два расшерения для выставления текста в TextView и данных в адаптер, дальше можно дописывать то что нужно вашему проекту. Выглядет они так:
```kotlin
fun <T> ObservableList<T>.tryBindAsSource(adapter: RecyclerAdapter<T>, skipInitial: Boolean = false): Subscription? {
    if (!skipInitial)
        adapter.setItems(this)
    return subscribeOnChange { _, newList ->
        adapter.setItems(newList)
    }
}

fun KProperty0<Any?>.tryBindAsText(textView: TextView, skipInitial: Boolean = false): Subscription? {
    return tryBind(skipInitial = skipInitial) {
        textView.text = it?.toString()
    }
}
```
Про RecyclerAdapter расскажу позже, а сейчас давайте посмотрим на ViewModel.
```kotlin
class MainViewModel : MVVMViewModel() {

    var counter by property(1)
        private set

    val elements = ObservableList<String>()

    fun increase() {
        counter++
        elements += random().toString()
    }

    fun decrease() {
        counter--
        elements -= elements.random()
    }
}
```
Использую `property` мы можем использовать эти поля как обычные поля класса, при этом если поменять их то обновится и View.
Подписаться на изменения проперти можно и в самой `ViewMode`, для этого у `propery` есть не обязательный параметр `onChange`. 

Есть еще один тип проперти - `sharedProperty`. Эта проперти шарит значение этого поле между полями с таким же именем и топом. Так же она умеет его сохранять в постоянную память. Например тот же счетчик можно сделать так:
```kotlin
class CounterViewModel : MVVMViewModel() {
    var counter by sharedProperty(1, "counter", saved = true)
        private set
}

class DetailCounterViewModel : MVVMViewModel() {
    var counter by sharedProperty(1, "counter", saved = true)
        private set
}
```

При изменении поля `counter` в `CounterViewModel` изменится и поле `counter` в `DetailCounterViewModel`. При этом сработает все тот же механихм связывания данных и все состояния завязанные на это поле так же обновятся. А использую не обязяательный параметр `saved` можно сообщить проперти нужно ли писать это значение в постоянную память. Если поставить true как в примери выше то даже после перезапуска приложения в этих полях останутся прежние значения.
