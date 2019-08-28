# WhenWhatFramework

## Реализации паттерна MVVM, навигации, полезные утилиты.
Все кто начинал проект с нуля не раз знают что это интересно только в первые разы, особенно когда проекты похожи друг на друга. Подключение и настройка библиотек, реализация какого либо паттерна(MVVM, MVP, MVI, ...), реализацая механизма навигации(особенно актуально если имеются диплинки в приложении), и т.д. Чаще всего большая часть берется из прошлых проектов и копипастится в новый. Для того что бы избежать повторение одного и того же из раза в раз, можно реализовать максимальное число компонентов которые повторяются чаще всего и вынести это в одну библиотеку. Собственно это я и попытаюсь сделать. Для начала нужно определить какие же компоненты было бы не плохо реализовать, а какие не имеет смысла. Обязательное условие при написании этой библиотеки - не использовать стороних библиотек. Это нужно для того что бы не приходилось следить за актульностью этих самых библиотек и не переписывать ничего каждый раз когда они меняются. Из за этого из реализации например придется убрать сетевую часть, так как чаще всего для нее используется Retrofit. В итоге для начала у меня получились следующие пункты.

1. Навигация. Она есть в каждом приложении и для нее есть несколько требований.
- Возможность перехода на любой экран в одно действие. То есть если у вас есть activity на которой есть нужный вам fragment, но вы находитесь на другой activity то вам нужно указать этот fragment при навигации, остальное произойдет само.
- Легкая работа с deep link. 
- Строгая типизация при передаче параметров на экран

### Навигация
Для навигации вначале нужно описать наш ScreenFlow. Примером будет приложение где три экрана: список топиков, список новостей топика, детальная страница новости. Любой экран на который может осуществляться навигаци 
является объектом `NavigationPoint`. Каждой точке мы указываем имя и класс activity-а или fragment-а. Так в `generic` параметре мы указываем тип объекта передаваемого на этот экран. В примере имеется activity NewsHostActivity в которой распологаются те фрагменты которые относятся к новостям. Объявить точку для навигации на фрагмент можно только внутри `HostActivityNavigationPoint`, для этого создается наследник этого класса, в качестве активити тут должен выступать наследник `MVVMHostActivity`. Внутри контексте этого класса нам доступно два метода. `rootFragmentPoint` - указывает фрагмент который будет открыт при навигации на эту активити. `fragmentPoint` - просто создает точку для навигации.

```kotlin
object ScreenFlow {
    val NEWS = News
    val TOPICS = ActivityNavigationPoint<Empty>("TopicActivity", TopicActivity::class.java)
}

object News : HostActivityNavigationPoint<NewsParam>("NewsHostActivity", NewsHostActivity::class.java) {
    val LIST = rootFragmentPoint("NewsFragment", NewsFragment::class.java)
    val DETAIL = fragmentPoint<NewsDetailParam>("NewsDetailFragment", NewsDetailFragment::class.java)
}
```

Предпологается что все объявленые точки навигации будут объеденены в одном объекте, в моем случе это `ScreenFlow`. Дальше навигация осуществляется с помощью `Navigator` который есть в `MVVMActivity` и `MVVMFragment`(о них позже). Предположим мы находимся в `TopicActivity` и хотим перейти на `NewsFragment` который должен разпологаться в `NewsHostActivity`(как описано выше). Код будет выглядять одним из следующих образов.
```kotlin
        navigator?.navigate(ScreenFlow.NEWS, NewsParam(topicCard.id))
        или
        navigator?.navigate(ScreenFlow.NEWS.LIST, NewsParam(topicCard.id))
```
Мы можем указать как просто activity для перехода и тогда откроется `rootFragmentPoint` указынный в `ScreenFlow`, или мы можем указать сразу нужный фрагмент. При навигации вторым параметром от нас будет требоваться передать объект того типа который указан в `ScreenFlow`, таким образом наша навигация является строго типизированной.
Второй случай если у нас имеются deep links или мы просто хотим запомнить для каких то целей куда и с каким параметром нужно навигироваться. Например если мы хотим сообщить следующему экрану куда ему навигироваться после своего завершения.
```kotlin
val navigationAction = ScreenFlow.NEWS.asNavigationAction(NewsParam(topicCard.id))
navigator?.navigate(navigationAction)
```
`NavigationAction` - это просто объект с двумя полями: имя, параметр. Он является `Serializable`, по этому его без труда можно сохранить в Bundle или в файл.

Для навигации назад:
```kotlin
navigator?.back()
navigator?.finishActivity() - появляется у навигатора если мы находимся на фрагменте
```
Для навигации назад с параметров:
```kotlin
val someObject: Serializable
navigator?.backWith(someObject)
```

Для получение результата на прошлом экране необходимо переопределить метод `onForwardScreenResult(result: Serializable)` у `MVVMFragment` или `MVVMActivity`
```kotlin
override fun onForwardScreenResult(result: Serializable) {
    super.onForwardScreenResult(result)
    Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
}
```

Все требования к навигации были реализованны. Что бы добавить переход на новый экран приложения нужно всего лишь добавить одну строку в ваш `ScreenFlow`, что очень удобно и позволяет всегда видеть все экрнаны на которые можно снавигироваться в приложении.

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

### Adapter
С одной стороны совершенно не по теме, но с другой стороны какой проект обходится без списков? И их тоже нужно привязывать к данным. По этому почему бы тоже не написать какую нибудь заготовку. Именно по этому был написан RecyclerAdapter. Использование его не сильно отличается от стандартного, но сокращает кол-во кода. Вот пример с выводом списка строк:
```kotlin
class TextRecyclerAdapter : RecyclerAdapter<String>() {

    override fun onBind(holder: DefaultViewHolder, item: String) {
        holder.itemView.textView.text = item
    }

    override fun createView(inflater: Inflater, viewType: Int): View {
        return inflater.inflate(R.layout.item_text)
    }
}
```
Выглядит чуть аккуратнее чем обычный адаптер, но не сильно. Смысла в таком базовом адаптаре мало, но тут есть еще кое что. Представим что у нас есть какой нибудь общий элемент который может отображаться в разных адаптерах, и отображается он одинаково(в моих проектах такое встречалось часто). В этом случае можно сделать так.
Предположим на одном экране у нас распологается несколько элементо, объявим их так 
 ```kotlin
interface CartItem

class CartPromo(val text: String) : CartItem
class CartProduct(val product: Product) : CartItem
```

Сам адаптер будет выглядеть так:
```kotlin
class CartAdapter : RecyclerAdapter<CartItem>() {

    override fun onBind(holder: DefaultViewHolder, item: CartItem) {
        when(item) {
            is CartPromo -> holder.itemView.textView.text = item.text
            is CartProduct -> {
                // cartProduct binding
            }
        }
    }

    override fun createView(inflater: Inflater, viewType: Int): View {
        return when(viewType) {
            CartPromo::class.java.hashCode() -> R.layout.item_text
            CartProduct::class.java.hashCode() -> R.layout.item_product
            else -> throw IllegalArgumentException("unknown item")
        }.let(inflater::inflate)
    }

    override fun getItemType(position: Int): Int {
        return items[position]::class.java.hashCode()
    }
}
```
Пункт `cartProduct binding` может занимать много строк кода. Реальная карточка продукта вполне может содержать >50 строк. И если карточка используется на разных экранах и в разных адаптерах то это становится проблемой. Но можно сделать так.
Объявление элементов заменится на:
```kotlin
interface CartItem

class CartPromo(val text: String) : CartItem
class CartProduct(override val product: Product) : CartItem, ProductHolder
```
Сам `ProductHolder`:
```kotlin
interface ProductHolder {
    val product: Product
}
```
Теперь выносим код связанный с продуктом:
```kotlin
object ProductBinder : RecyclerAdapter.ItemBinder<ProductHolder> {

    override fun createView(inflater: RecyclerAdapter.Inflater): View = inflater.inflate(R.layout.item_product)

    override fun bind(holder: DefaultViewHolder, item: ProductHolder) {
        // cartProduct binding
    }
}
```
Адапрет теперь будет выглядеть так:
```kotlin
class CartAdapter : RecyclerAdapter<CartItem>() {

    init {
        registerBinder(CartProduct::class.java, ProductBinder)
    }

    override fun onBind(holder: DefaultViewHolder, item: CartItem) {
        when (item) {
            is CartPromo -> holder.itemView.textView.text = item.text
        }
    }

    override fun createView(inflater: Inflater, viewType: Int): View {
        return when (viewType) {
            CartPromo::class.java.hashCode() -> R.layout.item_text
            else -> throw IllegalArgumentException("unknown item")
        }.let(inflater::inflate)
    }

    override fun getItemType(position: Int): Int {
        return items[position]::class.java.hashCode()
    }
}
```
Таким образом мы вынесли код из адаптера благодаря чему уменьшилось кол-во кода в адаптерах используещих его, так же будет проще вностить правки.

Библиотека сосздается для быстрого старта мелких проектов, в нее будет добавляться еще много интересного функционала упрощающего разработку. Самое главное к проекту не будет подключено ни одной стороней библиотеки. 
