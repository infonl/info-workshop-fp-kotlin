# Exercise 2: Higher-order Functions and Composition

The last step of [exercise 1](Exercise-1.md) was already using a higher-order
function; it is namely a function using a function as parameter.

To make higher-order functions make more sense, let's look at some more 
concepts that help with the definition and use of functions as parameters.

And we will take this a step further by composing functions together into a 
string of invocations that itself is again a function.

## Function Type

Instead of defining the exact type when we define a parameter (or in another 
context) we can define a type alias for the function signature. Continuing 
from the function parameter defined in [exercise 1](Exercise-1.md), we can 
define something like this:

```kotlin
typealias BookInfoGetter<T> = (Book) -> T
```
This is a function type that accepts a `Book` and returns a templated type 
`T`, so that depends on the templated use of this type.

### Example use

So using the type definition, we can create an extension method on a 
`List<Book>` (list of books) that calculates a total:

```kotlin
fun List<Book>.total(getBookValue: BookInfoGetter<Double>): Double =
    this.fold(0.0) { total, book -> total + getBookValue(book) }
```

The above method will now allow for calculating a total over a list of books 
using a supplied function.

Can you now invoke this method, and print the total price for all books?

## Composing Functions

Composing functions is a concept where we can string functions together, so 
that you can create a new composed function (or at least logic) that is 
strictly defined by the order of functions it is composed of.

### In to Out Type

To be able to model composition efficiently, we will add a types that 
will help us. It is a simple `In to Out` conversion function type definition:
```kotlin
typealias Func<I,O> = (I) -> O
```

#### Exercise
Can you use this type definition to create a value holding a function 
definition that retrieves the price of a book?
```kotlin
val bookPrice: Func<Book,Price> = TODO()
```

And create another function value that formats a price into a string?
```kotlin
val formatPrice: Func<Price,String> = TODO()
```

### Composition

As you can see, the above two functions could be put in order to take a 
`Book` and output a formatted `Price` as a `String`.

To accommodate that sort of composition, we can create an `infix` function that 
will allow us to do that:
```kotlin
infix fun <I,M,O> Func<M,O>.after(inToMid: Func<I,M>): Func<I,O> = { i: I ->
    this(inToMid(i))
}
```

**Note on `infix`**: This keyword defines that the function you define can 
be used without parentheses nor dots, and will therefore look like so:
```kotlin
val composed = function1 after function2
```

#### Exercise
Now that we have 2 functions that can be composed nicely, how about we try 
to compose them?
```kotlin
val formatBookPrice = TODO()
```

And then make use of it to print all book prices?
```kotlin
books.forEach {
    TODO()
}
```
