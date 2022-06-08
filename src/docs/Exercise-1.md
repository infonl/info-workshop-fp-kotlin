# Exercise 1: Introduction to Functions

So, as an introduction to functions in Kotlin, we will have a look at how they
are syntactically created and used.

## Function Syntax

In this first step, we will create a function that makes use of some existing
code.

### Base type
If we look in the the [Books.kt](src/main/kotlin/Books.kt) file, we can see a 
definition of a type named `Book`; it will be the type we will use within this 
exercise.

There is also a list of instances of books, that we then can use when we want
to invoke functions.

### Create a function
Now we can have a look at creating some functions that use the `Book` type and 
extract some information from it.

#### Create a file
Firstly we need a file to build the code in, so let's create a file ( I call it 
`Excercise1.kt`).

In this file we will create a main method, so that we can easily run our code:
```kotlin
fun main() {
    // here comes the code we want to run
}
```

#### Write a function
Now for a function, this will be up to you. The general syntax will be 
something like so:
```kotlin
fun someMethodUsingABook(b: Book): String {
    return b.name
}

// simplified 
fun someOtherMethodUsingBook(b: Book): String = b.name

// without explicit type
fun someSimplerMethodUsingBook(b: Book) = b.name

// OR as an extension method on type Book
fun Book.someMethod() = b.name
```

#### Trying it out
To test if the function works, you will have to invoke it. There are a number of 
`Book` instances, so we can use one of them to test the function.

We do that by adding a call in the `main` function, and printing the result:
```kotlin
fun main() {
    println("My method result = ${someSimplerMethodUsingBook(books[0])}")    
}
```

Refer to the main [readme](Readme.md) section `Testing your code` for details 
on how you can execute your code exactly.

#### Function values
Continuing on from the function you have defined, we can start with more 
functional programming concepts. We are going to create a value holding a 
function, and make use of it.

So firstly we will create a value that holds a function; for instance:
```kotlin
val bookWeightFun = fun(book: Book) = book.weight
```

A value holding a function can be invoked similarly to a normal function, ie:
```kotlin
println("${bookWeightFun(books[0])}")
```

And it can be passed to another function, if it matches the function parameter
definition ofcourse:
```kotlin
fun printBookInfo(book: Book, getInfo: (Book) -> Any) = println("Info = ${getInfo(book)}")
```
And this can then be invoked as such:
```kotlin
printBookInfo(books[2],bookWeightFun)
```

#### Taking it further
Can you create some more functions that match the criteria for the 
`printBookInfo` function?

Or how about we look at looping over the books and printing info for all of 
them?

## Kotlin build-in functional concepts
Within kotlin there are a number of concepts that are considered functional, 
so we should have a quick look at some of these.

### Apply and Let
All Kotlin classes will export methods `let` and `apply`. These methods can 
be used on any instance to convert it to another type using `let`, or modify 
the object using `apply`.

### Mappers et.al.
Collections within Kotlin have a number of methods for mapping, filtering, 
sorting, etc. These methods all work similarly, as that they take a 
collection and return a new collection that has been modified according to 
the method and passed function.

#### Exercise
Let's take the `books` collection and return only the top 2 most recent English 
language books ordered by the number of pages, and print them to the 
standard output.

```kotlin
    books.filter { TODO() }
        .sortedBy { TODO() } 
        .take(2) 
        .sortedBy { TODO() }
        .forEach { 
            println("$it")
        }
```

### Tail recursion
Recursion is a common practice when a collection of items is processed, and 
in Kotlin we should tell the compiler explicitly to use it.

Here's just an example:
```kotlin
fun <T> Collection<T>.powerset(): Set<Set<T>> = 
    powerset(this, setOf(emptySet()))

private tailrec fun <T> powerset(left: Collection<T>, acc: Set<Set<T>>): Set<Set<T>> =
    if (left.isEmpty()) acc
    else powerset(left.drop(1), acc + acc.map { it + left.first() })
```

#### Exercise
We can try to apply this concept to the collection of books, to create a set of 
sets by author.

```kotlin
fun Collection<Book>.booksByAuthor(): Set<Set<Book>> {
    /**
     * Internal function that uses tail-recursion for memory efficiency
     * @param left what is left to split by author
     * @param authors the authors already processed
     * @param acc accumulator for tail recursion
     * @return a set of sets grouped by author
     */
    tailrec fun byAuthor(left: Collection<Book>, authors: Set<String>, acc: Set<Set<Book>>): Set<Set<Book>> =
        if (left.isEmpty()) acc
        else TODO()

    /**
     * Start internal tail recursive function with:
     * left = the set of Books
     * authors = an empty set, no authors have been processed as yet
     * acc = an empty result set
     */
    return byAuthor(this.toSet(), emptySet(), setOf(emptySet()))
}
```

