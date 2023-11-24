package com.shakelang.shake.util.pointer

/**
 * A pointer points to a value in memory. It can be used to pass a value by reference.
 * The advantage of a pointer is that it can be used to change the value of a variable
 * in a function.
 *
 * @param T The type of the value the pointer points to
 * @since 0.1.0
 * @version 0.1.0
 */
interface Pointer<out T> {

    /**
     * The value the pointer points to
     */
    val value: T

    /**
     * Transform the value of the pointer. Creates a new pointer which will
     * get the value on access, transform it and return it. So if you change
     * the value of the original pointer the value of the new pointer will
     * change too.
     *
     * WARNING: If you access the value of the transformed pointer multiple
     * times the transform function will be called multiple times too. So
     * for performance reasons you should get the value of the pointer and
     * store it in a variable if you need it multiple times (and don't need
     * the changes of the original pointer).
     *
     * @param transform The transform function
     * @return The new pointer
     * @since 0.1.0
     * @version 0.1.0
     */
    fun <A> transform(transform: (T) -> A): Pointer<A> {
        return object : Pointer<A> {
            override val value: A
                get() = transform(this@Pointer.value)
        }
    }

    /**
     * Chain the value of the pointer. Creates a new pointer which will
     * get the value on access, transform it and return it. So if you change
     * the value of the original pointer the value of the new pointer will
     * change too. Is the same as [transform] but the transform functions parameter
     * should return a value, this function parameter should return a pointer.
     *
     * WARNING: If you access the value of the transformed pointer multiple
     * times the transform function will be called multiple times too. So
     * for performance reasons you should get the value of the pointer and
     * store it in a variable if you need it multiple times (and don't need
     * the changes of the original pointer).
     *
     * @param transform The transform function
     * @return The new pointer
     * @since 0.1.0
     * @version 0.1.0
     */
    fun <A> chain(transform: (T) -> Pointer<A>): Pointer<A> {
        return object : Pointer<A> {
            override val value: A
                get() = transform(this@Pointer.value).value
        }
    }


    /**
     * Chain the value of the pointer. Creates a new pointer which will
     * get the value on access, transform it and return it. So if you change
     * the value of the original pointer the value of the new pointer will
     * change too. Is the same as [transform] but the transform functions parameter
     * should return a value, this function parameter should return a pointer.
     * This function allows null values.
     *
     * Additionally, in contrast to [chain], this function allows null values.
     *
     * WARNING: If you access the value of the transformed pointer multiple
     * times the transform function will be called multiple times too. So
     * for performance reasons you should get the value of the pointer and
     * store it in a variable if you need it multiple times (and don't need
     * the changes of the original pointer).
     *
     * @param transform The transform function
     * @return The new pointer
     * @since 0.1.0
     * @version 0.1.0
     */
    fun <A> chainAllowNull(transform: (T) -> Pointer<A?>?): Pointer<A?> {
        return object : Pointer<A?> {
            override val value: A?
                get() = transform(this@Pointer.value)?.value
        }
    }

    companion object {

        /**
         * Create a new pointer pointing to the given value
         *
         * @param value The value the pointer should point to
         * @return The new pointer
         * @since 0.1.0
         * @version 0.1.0
         */
        fun <T> of(value: T) = object : Pointer<T> {
            override val value: T = value
        }

        /**
         * Create a new mutable pointer pointing to the given value
         *
         * @param value The value the pointer should point to
         * @return The new pointer
         * @since 0.1.0
         * @version 0.1.0
         */
        fun <T> mutableOf(value: T) = object : MutablePointer<T> {
            override var value: T = value
        }

        /**
         * Create a new pointer which is not initialized yet and can
         * be initialized later
         *
         * @return The new pointer
         * @since 0.1.0
         * @version 0.1.0
         */
        fun <T> late() = object : LateInitPointer<T> {
            var realValue: T? = null

            override var isInitialized = false
                private set

            @Suppress("UNCHECKED_CAST")
            override val value: T
                get() = if (isInitialized) realValue as T else throw IllegalStateException("lateinit pointer is not initialized")

            override fun init(value: T) {
                if (isInitialized) throw IllegalStateException("late init pointer is already initialized")
                this.realValue = value
                isInitialized = true
            }
        }

        /**
         * Create a new mutable pointer which is not initialized yet
         * and can be initialized later
         *
         * @return The new pointer
         * @since 0.1.0
         * @version 0.1.0
         */
        fun <T> lateMutable() = object : LateInitMutablePointer<T> {
            var realValue: T? = null

            override var isInitialized = false
                private set

            @Suppress("UNCHECKED_CAST")
            override var value: T
                get() = if (isInitialized) realValue as T else throw IllegalStateException("late init pointer is not initialized")
                set(value) {
                    realValue = value
                    isInitialized = true
                }

            override fun init(value: T) {
                if (isInitialized) throw IllegalStateException("late init pointer is already initialized")
                this.realValue = value
                isInitialized = true
            }
        }

        /**
         * Create a new pointer which performs the given task on access
         * and returns the result of the task
         *
         * @param task The task to perform
         * @return The new pointer
         * @since 0.1.0
         * @version 0.1.0
         */
        fun <T> task(task: () -> T) = object : Pointer<T> {
            override val value: T = task()
        }
    }
}

/**
 * Create a new pointer pointing to the given value
 * @param it The value the pointer should point to
 * @return The new pointer
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> point(it: T): Pointer<T> = Pointer.of(it)

/**
 * Create a new mutable pointer pointing to the given value
 * @param it The value the pointer should point to
 * @return The new pointer
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> mutablePoint(it: T): MutablePointer<T> = Pointer.mutableOf(it)

/**
 * Create a new pointer which is not initialized yet and can
 * be initialized later
 * @return The new pointer
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> latePoint(): LateInitPointer<T> = Pointer.late()

/**
 * Create a new mutable pointer which is not initialized yet
 * and can be initialized later
 * @return The new pointer
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> lateMutablePoint(): LateInitMutablePointer<T> = Pointer.lateMutable()

/**
 * Create a new pointer which performs the given task on access
 * and returns the result of the task
 * @param task The task to perform
 * @return The new pointer
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> taskPoint(task: () -> T): Pointer<T> = Pointer.task(task)


/**
 * Create a new pointer pointing to the given value
 * @receiver The value the pointer should point to
 * @return The new pointer
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> T.point(): Pointer<T> = Pointer.of(this)

/**
 * Create a new mutable pointer pointing to the given value
 * @receiver The value the pointer should point to
 * @return The new pointer
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> T.mutablePoint(): MutablePointer<T> = Pointer.mutableOf(this)

/**
 * Create a list of pointers pointing to the values of the list
 * @receiver The list of values the pointers should point to
 * @return The list of pointers
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> Iterable<T>.points(): PointerList<T> = map { Pointer.of(it) }

/**
 * A mutable pointer points to a value in memory. It can be used to pass a value by reference.
 * The advantage of a pointer is that it can be used to change the value of a variable
 * in a function.
 *
 * @param T The type of the value the pointer points to
 * @since 0.1.0
 * @version 0.1.0
 * @see Pointer
 * @see LateInitPointer
 */
interface MutablePointer<T> : Pointer<T> {

    /**
     * The value the pointer points to
     */
    override var value: T
}

/**
 * A late init pointer points to a value in memory. It can be used to pass a value by reference.
 * The advantage of a pointer is that it can be used to change the value of a variable
 * in a function.
 *
 * The difference between a [LateInitPointer] and a [MutablePointer] is that a [LateInitPointer]
 * can be initialized later and a [LateInitPointer] can only be initialized once.
 *
 * @param T The type of the value the pointer points to
 * @since 0.1.0
 * @version 0.1.0
 * @see Pointer
 * @see MutablePointer
 * @see LateInitMutablePointer
 */
interface LateInitPointer<T> : Pointer<T> {

    /**
     * The value the pointer points to
     */
    override val value: T

    /**
     * Is the pointer initialized
     */
    val isInitialized: Boolean

    /**
     * Initialize the pointer
     *
     * @param value The value the pointer should point to
     * @since 0.1.0
     * @version 0.1.0
     */
    fun init(value: T)
}

/**
 * A mutable late init pointer points to a value in memory. It can be used to pass a value by reference.
 * The advantage of a pointer is that it can be used to change the value of a variable
 * in a function.
 *
 * A [LateInitMutablePointer] combines the features of a [MutablePointer] and a [LateInitPointer].
 *
 * @param T The type of the value the pointer points to
 * @since 0.1.0
 * @version 0.1.0
 * @see Pointer
 * @see MutablePointer
 * @see LateInitPointer
 * @see LateInitMutablePointer
 */
interface LateInitMutablePointer<T> : MutablePointer<T>, LateInitPointer<T> {
    /**
     * The value the pointer points to
     */
    override var value: T
}

/**
 * A PointerList refers to a list of pointers.
 *
 * @param T The type of the value the pointers point to
 * @since 0.1.0
 * @version 0.1.0
 */
typealias PointerList<T> = List<Pointer<T>>

/**
 * A MutablePointerList refers to a list of mutable pointers.
 *
 * @param T The type of the value the pointers point to
 * @since 0.1.0
 * @version 0.1.0
 */
typealias MutablePointerList<T> = MutableList<Pointer<T>>

/**
 * You can create a list that points to the values of a [PointerList] with this function.
 *
 * @receiver The list of pointers
 * @return The list of values
 * @since 0.1.0
 * @version 0.1.0
 * @see PointerList
 * @see PointingList
 */
fun <T> PointerList<T>.values(): PointingList<T> = PointingList.from(this)

/**
 * You can create a list that points to the values of a [MutablePointerList] with this function.
 *
 * @receiver The list of pointers
 * @return The list of values
 * @since 0.1.0
 * @version 0.1.0
 * @see MutablePointerList
 * @see MutablePointingList
 */
fun <T> MutablePointerList<T>.values(): MutablePointingList<T> = MutablePointingList.from(this)

/**
 * A PointingList refers to a list of values that are pointed to by pointers.
 *
 * @param T The type of the value the pointers point to
 * @since 0.1.0
 * @version 0.1.0
 */
interface PointingList<T> : List<T> {

    /**
     * The list of pointers
     */
    val pointers: List<Pointer<T>>

    /**
     * The implementation of the [PointingList].
     * @param T The type of the value the pointers point to
     * @since 0.1.0
     * @version 0.1.0
     * @see PointingList
     */
    private class Impl<T>(

        /**
         * The list of pointers
         */
        override val pointers: List<Pointer<T>>

    ) : PointingList<T> {

        /**
         * Returns the element at the specified index in the list.
         */
        override val size: Int
            get() = pointers.size

        /**
         * Returns the element at the specified index in the list.
         */
        override fun get(index: Int): T {
            return pointers[index].value
        }

        /**
         * Returns `true` if the list is empty (contains no elements), `false` otherwise.
         */
        override fun isEmpty(): Boolean {
            return pointers.isEmpty()
        }

        /**
         * Returns an iterator over the elements of this list in proper sequence.
         */
        override fun iterator(): Iterator<T> {
            val iterator = pointers.iterator()
            return object : Iterator<T> {
                override fun hasNext(): Boolean = iterator.hasNext()
                override fun next(): T = iterator.next().value
            }
        }

        /**
         * Returns a list iterator over the elements in this list (in proper sequence).
         */
        override fun listIterator(): ListIterator<T> {
            return listIterator(0)
        }

        /**
         * Returns a list iterator over the elements in this list (in proper sequence), starting at the specified [index].
         */
        override fun listIterator(index: Int): ListIterator<T> {
            val iterator = pointers.listIterator(index)
            return object : ListIterator<T> {
                override fun hasNext(): Boolean = iterator.hasNext()
                override fun next(): T = iterator.next().value
                override fun hasPrevious(): Boolean = iterator.hasPrevious()
                override fun previous(): T = iterator.previous().value
                override fun nextIndex(): Int = iterator.nextIndex()
                override fun previousIndex(): Int = iterator.previousIndex()
            }
        }

        /**
         * Returns a view of the portion of this list between the specified [fromIndex] (inclusive) and [toIndex] (exclusive).
         * The returned list is backed by this list, so non-structural changes in the returned list are reflected in this list, and vice-versa.
         *
         * Structural changes in the base list make the behavior of the view undefined.
         */
        override fun subList(fromIndex: Int, toIndex: Int): List<T> {
            return Impl(pointers.subList(fromIndex, toIndex))
        }

        /**
         * Returns the index of the last occurrence of the specified element in the list, or -1 if the specified
         * element is not contained in the list.
         */
        override fun lastIndexOf(element: T): Int {
            return pointers.indexOfLast { it.value == element }
        }

        /**
         * Returns the index of the first occurrence of the specified element in the list, or -1 if the specified
         * element is not contained in the list.
         */
        override fun indexOf(element: T): Int {
            return pointers.indexOfFirst { it.value == element }
        }

        /**
         * Returns `true` if all elements in the specified collection are contained in this collection.
         */
        override fun containsAll(elements: Collection<T>): Boolean {
            return pointers.all { it.value in elements }
        }

        /**
         * Returns `true` if the specified element is contained in this collection.
         */
        override fun contains(element: T): Boolean {
            return pointers.any { it.value == element }
        }
    }

    companion object {

        /**
         * Create a new [PointingList] from the given list of pointers
         *
         * @param it The list of pointers
         * @return The new [PointingList]
         * @since 0.1.0
         * @version 0.1.0
         */
        fun <T> from(it: List<Pointer<T>>): PointingList<T> {
            return Impl(it)
        }
    }
}


/**
 * A MutablePointingList refers to a list of values that are pointed to by mutable pointers.
 *
 * @param T The type of the value the pointers point to
 * @since 0.1.0
 * @version 0.1.0
 */
interface MutablePointingList<T> : PointingList<T>, MutableList<T> {

    /**
     * The list of pointers
     */
    override val pointers: MutableList<Pointer<T>>

    /**
     * The implementation of the [MutablePointingList].
     * @param T The type of the value the pointers point to
     * @since 0.1.0
     * @version 0.1.0
     * @see MutablePointingList
     */
    class Impl<T>(
        /**
         * The list of pointers
         */
        override val pointers: MutablePointerList<T>
    ) : MutablePointingList<T> {
        override val size: Int
            get() = pointers.size

        override fun get(index: Int): T {
            return pointers[index].value
        }

        override fun isEmpty(): Boolean {
            return pointers.isEmpty()
        }

        override fun iterator(): MutableIterator<T> {
            val iterator = pointers.iterator()
            return object : MutableIterator<T> {
                override fun hasNext(): Boolean = iterator.hasNext()
                override fun next(): T = iterator.next().value
                override fun remove() = iterator.remove()
            }
        }

        override fun listIterator(): MutableListIterator<T> {
            return listIterator(0)
        }

        override fun listIterator(index: Int): MutableListIterator<T> {
            val iterator = pointers.listIterator(index)
            return object : MutableListIterator<T> {
                override fun add(element: T) = iterator.add(Pointer.of(element))
                override fun hasNext(): Boolean = iterator.hasNext()
                override fun next(): T = iterator.next().value
                override fun hasPrevious(): Boolean = iterator.hasPrevious()
                override fun previous(): T = iterator.previous().value
                override fun nextIndex(): Int = iterator.nextIndex()
                override fun previousIndex(): Int = iterator.previousIndex()
                override fun remove() = iterator.remove()
                override fun set(element: T) = iterator.set(Pointer.of(element))
            }
        }

        override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
            return Impl(pointers.subList(fromIndex, toIndex))
        }

        override fun clear() {
            pointers.clear()
        }

        override fun removeAt(index: Int): T {
            return pointers.removeAt(index).value
        }

        override fun set(index: Int, element: T): T {
            return pointers.set(index, Pointer.of(element)).value
        }

        override fun retainAll(elements: Collection<T>): Boolean {
            return pointers.retainAll { it.value in elements }
        }

        override fun removeAll(elements: Collection<T>): Boolean {
            return pointers.removeAll { it.value in elements }
        }

        override fun remove(element: T): Boolean {
            return pointers.removeAll { it.value == element }
        }

        override fun addAll(elements: Collection<T>): Boolean {
            return pointers.addAll(elements.map { Pointer.of(it) })
        }

        override fun addAll(index: Int, elements: Collection<T>): Boolean {
            return pointers.addAll(index, elements.map { Pointer.of(it) })
        }

        override fun add(index: Int, element: T) {
            pointers.add(index, Pointer.of(element))
        }

        override fun add(element: T): Boolean {
            return pointers.add(Pointer.of(element))
        }

        override fun lastIndexOf(element: T): Int {
            return pointers.indexOfLast { it.value == element }
        }

        override fun indexOf(element: T): Int {
            return pointers.indexOfFirst { it.value == element }
        }

        override fun containsAll(elements: Collection<T>): Boolean {
            return pointers.all { it.value in elements }
        }

        override fun contains(element: T): Boolean {
            return pointers.any { it.value == element }
        }
    }

    companion object {

        /**
         * Create a new [MutablePointingList] from the given list of pointers
         *
         * @param it The list of pointers
         * @return The new [MutablePointingList]
         * @since 0.1.0
         * @version 0.1.0
         */
        fun <T> from(it: MutablePointerList<T>): MutablePointingList<T> {
            return Impl(it)
        }
    }
}

/**
 * Make sure that the value of the pointer is not null
 * @param msg The message of the exception that will be thrown if the value is null
 * @return The pointer
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> Pointer<T?>.notNull(msg: String? = null): Pointer<T> {
    return this.transform { it ?: throw IllegalStateException(msg ?: "null value not allowed") }
}

/**
 * A register for pointers.
 * @param T The type of the value the pointers point to
 * @since 0.1.0
 * @version 0.1.0
 * @see Pointer
 * @see MutablePointer
 */
class PointerRegister<T> {

    /**
     * The list of pointers
     */
    private val pointers = mutableListOf<Pointer<T>?>()

    /**
     * The list of unused indices
     */
    private val unusedIndices = mutableListOf<Int>()

    init {
        instances.add(this)
    }

    /**
     * Register a pointer to the register (and return the index of the pointer)
     * @param pointer The pointer to register
     * @return The index of the pointer
     * @since 0.1.0
     * @version 0.1.0
     */
    fun register(pointer: Pointer<T>): Int {
        val index = unusedIndices.removeLastOrNull()
        if (index != null) pointers[index] = pointer
        else {
            pointers.add(pointer)
            return pointers.size - 1
        }
        return index
    }

    /**
     * Get the index of the given pointer
     * @param pointer The pointer
     * @return The index of the pointer or -1 if the pointer is not registered
     * @since 0.1.0
     * @version 0.1.0
     */
    fun indexOf(pointer: Pointer<Any?>): Int {
        this.pointers.forEachIndexed { index, p ->
            if (p == pointer) return index
        }
        return -1
    }

    /**
     * Unregister the pointer at the given index
     * @param index The index of the pointer
     * @since 0.1.0
     * @version 0.1.0
     */
    fun unregister(index: Int) {
        pointers[index] = null
        unusedIndices.add(index)
    }

    /**
     * Get the pointer at the given index
     * @throws IllegalStateException If the pointer is not registered
     * @param index The index of the pointer
     * @return The pointer
     * @since 0.1.0
     * @version 0.1.0
     */
    fun get(index: Int): Pointer<T> {
        return pointers[index] ?: throw IllegalStateException("pointer at index $index is null")
    }

    /**
     * Get the pointer at the given index or null if the pointer is not registered
     * @param index The index of the pointer
     * @return The pointer or null
     * @since 0.1.0
     * @version 0.1.0
     */
    fun getOrNull(index: Int): Pointer<T>? {
        return pointers[index]
    }

    /**
     * Create a late init pointer inside the register
     * @return The new pointer
     * @since 0.1.0
     * @version 0.1.0
     * @see LateInitPointer
     * @see Pointer.late
     */
    fun createPointer(): LateInitPointer<T> {
        val pointer = Pointer.late<T>()
        register(pointer)
        return pointer
    }

    /**
     * Create a late init pointer inside the register
     *
     * @param value The value the pointer should point to
     * @return The new pointer
     * @since 0.1.0
     * @version 0.1.0
     * @see LateInitPointer
     * @see Pointer.late
     */
    fun createPointer(value: T): Pointer<T> {
        val pointer = Pointer.of(value)
        register(pointer)
        return pointer
    }

    /**
     * Create a mutable pointer inside the register
     *
     * @return The new pointer
     * @since 0.1.0
     * @version 0.1.0
     * @see MutablePointer
     * @see Pointer.mutableOf
     */
    fun createMutablePointer(): LateInitMutablePointer<T> {
        val pointer = Pointer.lateMutable<T>()
        register(pointer)
        return pointer
    }

    /**
     * Create a mutable pointer inside the register
     *
     * @param value The value the pointer should point to
     * @return The new pointer
     * @since 0.1.0
     * @version 0.1.0
     * @see MutablePointer
     * @see Pointer.mutableOf
     */
    fun createMutablePointer(value: T): MutablePointer<T> {
        val pointer = Pointer.mutableOf(value)
        register(pointer)
        return pointer
    }

    /**
     * Destroy the register
     * @since 0.1.0
     * @version 0.1.0
     */
    fun destroy() {
        instances.remove(this)
    }

    /**
     * Destroy all contained pointers
     * @since 0.1.0
     * @version 0.1.0
     */
    fun destroyAllContainedPointers() {
        pointers.forEach { it?.unregister() }
    }

    companion object {
        /**
         * The list of all instances of pointer registers
         */
        val instances = mutableListOf<PointerRegister<*>>()
    }
}

/**
 * Register the pointer to the given register
 * @param register The register
 * @return The index of the pointer
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> Pointer<T>.register(register: PointerRegister<T>): Int {
    return register.register(this)
}

/**
 * Unregister the pointer from the given register
 * @param register The register
 * @since 0.1.0
 * @version 0.1.0
 */
fun <T> Pointer<T>.unregister(register: PointerRegister<T>) {
    val index = register.indexOf(this)
    if (index == -1) throw IllegalStateException("pointer is not registered")
    register.unregister(index)
}

/**
 * Close the pointer (unregister it from all registers)
 * @since 0.1.0
 * @version 0.1.0
 */
fun Pointer<*>.unregister() {
    PointerRegister.instances.forEach {
        val index = it.indexOf(this)
        if (index != -1) it.unregister(index)
    }
}

/**
 * Close the pointer (unregister it from all registers)
 * @since 0.1.0
 * @version 0.1.0
 */
fun Pointer<*>.close() {
    unregister()
}