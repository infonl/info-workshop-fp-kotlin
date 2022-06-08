# Exercise 3: Introducing Arrow

[Arrow](https://arrow-kt.io/docs/quickstart/) is a library that adds types that
make it easier to implementation functional patterns.

In this exercise we will introduce some of the core Arrow types and we will 
look at error handling the functional way.

## Either
`sealed class Either<out A, out B>`

Per the definition; either is a wrapper around 2 types. By convention the left
side holds a possible _exception_ and the right side the wrapped value.

For instance:
```kotlin
fun divide(v: Int, d: Int): Either<ArithmeticException, Double> =
    if (d == 0) Either.Left(ArithmeticException("Divide by zero"))
    else Either.Right(v.toDouble() / d)
```

In the above code, we have a method that has a fully defined path of execution
without a possible side effect where an exception creates a short-cut out of
the main flow.

### Exercise 
So, for a small exercise using `Either` we can create some methods that will 
showcase exception handling.

Firstly we need some way of converting a currency into 
another currency. For that we can use a conversion map like this one:
```kotlin
val conversionMap = mapOf<Pair<String,String>,Double>(
    ("£" to "$") to 1.25,
    ("€" to "$") to 1.07,
    ("£" to "€") to 0.83
)
```

So using the above table, let's create a `convertPrice` method in a couple 
of steps and setup exception handling in the process.

#### Basic functions
We will build the logic in a few basic functions, later we will introduce 
the exception handling logic

Create the base conversion function first:
```kotlin
/**
 * This should throw an exception if there is no conversion rate available 
 * for the used currency
 */
fun convertPrice(price: Price, currency: String): Price = TODO()
```

Also grab the function we created to format the price in [exercise 2]
(Exercise-2.md), and rewrite it a little:
```kotlin
fun formatPrice(price: Price): String = TODO() // Copy the logic
```

Lastly we will introduce a price parser method, that can be used to build a 
`Price` from a string. This function will allow us to investigate a flow where
there are multiple possible exceptions that can be thrown.
```kotlin
fun parsePrice(from: String): Price = TODO()
```

Calling these in order might look like so:
```kotlin
val price = parsePrice("1.11€")
    .let { convertPrice(it, "$") }
    .let { formatPrice(it) }
    
println("Parsed, converted price: $price")
```

This chain of calls can bomb out on any exception, and do try that out!

#### Introduce Either

Now we are going to change the above functions to handle the exceptions so 
that it is clear it can be passed out of them.

```kotlin
fun formatPrice(price: Price): Either<IllegalArgumentException,String> = TODO()

fun parsePrice(from: String): Either<ParseException,Price> = TODO()

fun convertPrice(price: Price, currency: String): 
        Either<IllegalArgumentException,Price> = TODO()
```

So where these methods would have thrown an exception, now it should return 
the exception wrapped using `Either.Left(...)`. 

And for a normal return value wrap it in `Either.Right(...)`.

These are specific classes that we can use the Kotlin type-matching using a 
`when` statement, ie:
```kotlin
when(e) {
    is Either.Right -> TODO() // Success
    is Either.Left -> TODO() // Failure
}
```

The chaining of the methods can now be done using the standard `Either` 
methods `flatMap` and `map`:
```kotlin
val price = parsePrice("1.11€")
    .flatMap { convertPrice(it, "$") }
    .flatMap { formatPrice(it) }

println("Parsed, converted price: $price")
```

And as a last step, we could output a message depending on the type with 
type-matching (continuing from above)
```kotlin
when(price) {
    is Either.Right -> println("Parsed, converted price: ${price.value}")
    is Either.Left -> println("Failed to parse price from: $priceString. Failure: ${price.left()}") // Failure
}
```

## Validated
`sealed class Validated<out E, out A>`

`Validated` works very similar to `Either` but the difference is that in general 
type `Validated` is used to accumulate errors, while `Either` is used to 
short-circuit a computation upon the first error.

### Exercise
We can take the above created method and replace `Either` with `Validated` 
as a small exercise.

## Eval
`sealed class Eval<out A>`

Eval is a monad which controls evaluation of a value or a computation that 
produces a value.

Three basic evaluation strategies:
- Now: evaluated immediately
- Later: evaluated once when value is needed
- Always: evaluated every time value is needed

The `Later` and `Always` are both lazy strategies while `Now` is eager. `Later` 
and `Always` are distinguished from each other only by memoization: once 
evaluated `Later` will save the value to be returned immediately if it is 
needed again. `Always` will run its computation every time.

It is not generally good style to pattern-match on Eval instances. Rather, use 
`.map` and `.flatMap` to chain computation, and use `.value` to get the result 
when needed. It is also not good style to create `Eval` instances whose 
computation involves calling `.value` on another `Eval` instance – this can 
defeat the trampolining and lead to stack overflows.

### Exercise

To get a feel for reasons for using `Eval` and delayed execution, let's 
create a long-running function that will eventually cause a StackOverflowError.

Here is a naive implementation to determine if a number is odd or even:
```kotlin
/**
 * Returns true if the given value is even
 */
fun even(v: Int): Boolean =
    if (v == 0) true
    else odd(v - 1)

/**
 * Returns true if the given value is odd
 */
fun odd(v: Int): Boolean =
    if (v == 0) false
    else even(v - 1)

fun main() {
    // Blow it up
    println(odd(100_000_001))
}
```

So, let's rewrite above code using `Eval`, and here's a start:
```kotlin
fun even(v: Int): Eval(Boolean) =
    Eval.always { v == 0 }.flatMap {
        TODO()
    }

fun main() {
    // Smooth like butter
    println(odd(100_000_001).value())
}
```

## Effect
`interface Effect<R, A>`

`Effect` is a `suspend` function that encapsulates the resulting value `A` and 
an exceptional result `R`.

To write an `Effect` we will use the constructor method `effect` to 
encapsulate the logic.

The DSL for building effect also has function `shift` for _shifting out of 
the normal flow_ and returning the exceptional result.
It also contains validator functions like `ensure(condition) { .. }` that, in 
case the condition is not satisfied, will shift the result from the lambda.

### Exercise
To have a look at the way that `Effect` can be used, we create a small 
function that reads a file.

Take the following code, and let's build on it:
```kotlin
object InvalidPath

fun readFile(path: String): Effect<InvalidPath,Unit> = effect {
    if (path.isBlank()) shift(InvalidPath)
    else Unit
}
```

Now we can improve on this by using some `ensure` methods.
```kotlin
fun readFile(path: String): Effect<InvalidPath,Unit> = effect {
    ensure(path.isNotBlank()) { InvalidPath }
    Unit
}
```

You could also change the function to accept a nullable value `path: String?
` and add another `ensure` to make sure that the value is actually not null.


Next step is to actually read the content of a file. Reading a file can 
cause other exceptional circumstances, so we can model those other 
situations like so:
```kotlin
sealed class FileError {
    object InvalidPath: FileError()
    object FileNotFound: FileError()
    object SecurityError: FileError()
}
```

And we have to ensure we can capture the file content. For that we use a 
simple class that encapuslates the content as a list of strings:
```kotlin
class Content(val body: List<String>)
```

So continuing from the `readFile` function we should expand it with actual 
`File(path).readLines()` capability:
```kotlin
fun readFile(path: String): Effect<FileError,Content> = effect {
    ensure(path.isNotBlank()) { FileError.InvalidPath }
    try {
        File(path).readLines()
    } catch (e: FileNotFoundException) {
        TODO()
    }
}
```

And to invoke this method, and you should try that for valid and invalid 
paths, we can for instance take the resulting effect and convert it to a 
`Either` like so (note that main is now also a `suspend` function):
```kotlin
suspend fun main() {
    val result = readFile("gradle.properties").toEither()
    when(result) {
        TODO()
    }
}
```

