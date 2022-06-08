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

Firstly we create a method that converts a `Price` into another currency.
For that we can use a conversion map like this one:
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

// Blow it up
println(odd(100_000_001))
```

So, let's rewrite above code using `Eval`, and here's a start:
```kotlin
fun even(v: Int): Eval(Boolean) =
    Eval.always { v == 0 }.flatMap {
        TODO()
    }

```