# Workshop Functional Programming in Kotlin Exercises

This project contains the basis for starting with this workshop.

There is also a [presentation](src/docs/Presentation.md).

## Setup

### Pull repo
Firtly pull the content of this repository to your local machine.

### Build
Then run the following command on your local machine to check if you're ready 
to go.
```shell
./gradlew build
```

### Issues
So, if the above command doesn't work, then there is most likely something 
missing. Please check the installation instructions for 
[Kotlin](https://kotlinlang.org).

## Exercises

[Exercise 1](src/docs/Exercise-1.md) introduces the syntax of functions in 
Kotlin

[Exercise 2](src/docs/Exercise-2.md) goes into the higher-order functions and composition

[Exercise 3](src/docs/Exercise-3.md) introduces Arrow

## Testing your code

So there are a few ways that you can test your code:
- using your IDE
- building a jar, and running it
- using the Kotlin REPL

### Run code from IDE

Some, if not all nowadays, IDEs allow you run code directly from within the 
workspace. For instance the IntelliJ IDEA shows a play button next to any code
that it knows how to execute ( `main` methods for instance ). So, that allows
you to easilly test what you have created.

### Build a jar and running it

The simplest way to create a jar for testing, is by using the command-line.

To create a jar, make sure you know the files that you want to include. For 
instance to build a jar and run it for [exercise 1](Exercise-1.md), use this command;
```shell
# use the kotlin compiler to build files Books.kt and Exercise1.kt into a Jar
kotlinc src/main/kotlin/Books.kt src/main/kotlin/Exercise1.kt -include-runtime -d exercise1.jar

# execute the jar, and see the result
java -jar exercise1.jar
```

### Kotlin REPL

The Kotlin REPL allows you to load files and invoke functions directly. This is 
an effective way for testing your code without having to rebuild the jar. For
example running a `main` method for [exercise 1](Exercise-1.md) can be done
like so:
```shell
# ensure you are in the src/main/kotlin dir
cd src/main/kotlin
# start the Kotlin REPL
kotlinc-jvm
# within the Kotlin REPL, load required files, and execute main function
>>> :load Books.kt
>>> :load Exercise1.kt
>>> main()
```
Make sure you reload the files that have changed if you have made changes and 
want to test again!