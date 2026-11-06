/* 
    Name: 
    In-Class Activity
    Kotlin Practice
*/

fun main() {

    // 1. Print these messages on separate lines
    /*
    Use the val keyword when the value doesn't change.
    Use the var keyword when the value can change.
    When you define a function, you define the parameters that can be passed to it.
    When you call a function, you pass arguments for the parameters.
    */
    println("Use the val keyword when the value doesn't change.")
    println("Use the var keyword when the value can change.")
    println("When you define a function, you define the parameters that can be passed to it.")
    println("When you call a function, you pass arguments for the parameters.")

    // 2. Uncomment the following code and fix the compile error
    println("New chat message from a friend")

    // 3. Uncomment the following and fix the compile error

    var discountPercentage: Int = 0
    var offer: String = ""
    val item = "Google Chromecast"
    discountPercentage = 20
    offer = "Sale - Up to $discountPercentage% discount on $item! Hurry up!"
    println(offer)


    // 4. Fix this code so it prints:
    // The total part size is: 50

    val numberOfAdults = 20
    val numberOfKids = 30
    val total = numberOfAdults + numberOfKids
    println("The total party size is: $total")


    // 5. Fix this code so it compiles

    val numCats: Int = 2
    var numDogs: Int = 2
    numDogs += numCats


    // 6. After fixing number 5, write a print statement to print the number
    // of cats and dogs using the variables, like:
    // There are 2 cats and 4 dogs
    println("There are $numCats cats $numDogs dogs.")

    // 7. Write a when statement to make a string val or var and have it say
    // whether there are more cats than dogs, more dogs than cats, are there
    // are the same number of each
    val more = when {
        numCats > numDogs -> "There are more cats than dogs"
        numDogs > numCats -> "There are more dogs than cats"
        else -> "There are the same number of dogs and cats"
    }
    println(more)

    // 8. Use when to determine if a character is a vowel or consonant and
    // print the result. Char has methods named lowerCaseChar() and
    // upperCaseChar() you can use
    val myChar = 'E'
    val result = when (myChar.lowercaseChar()) {
        in listOf('a','e','i','o','u') -> "vowel"
        else -> "consonant"
    }
    println(result)

    // 9. Use a for loop to print the numbers 0 to 9 in reverse order. Hint -
    // there is a downTo keyword
    for (num in 9 downTo 0) {
        print("$num ")
    }
    println()

    // 10. Create one list with 3 of your favorite shows/movies/games/etc. and
    // print the list
    val faves = mutableListOf<String>("Wilco", "Beatles", "The Doors")
    println(faves)
    
    // 11. Create another list with 2 or 3 of your least favorite shows/movies/
    // /games/etc. and create one list that contains everything from both lists.
    // Print out this new list.
    val hates = mutableListOf<String>("Taylor Swift", "Backstreet Boys")
    val comboed = (faves + hates).toMutableList()
    println(comboed)

    // 12. Remove your most and least favorite from the combined list, and print
    // this new list. You may need to revisit the lists from problems 10 and 11
    comboed.remove("Taylor Swift")
    comboed.remove("Beatles")
    println(comboed)

    // 13. Create a string variable and set it to null, then print the string
    var nullStr: String? = null
    println(nullStr)

    // 14. Make a variable and set it to the length of the string such that if
    // the string from 13. is null, the variable will be -1, then print the length
    val strLen = nullStr?.length ?: -1
    println(strLen)

    // 15. Write a when statement that returns a string saying if the string
    // is empty, the string is short, the string is medium length, or the string
    // is long. It's up to you as to what short, medium, and long strings are
    val strSize = when (strLen) {
        -1 -> "No string"
        0 -> "String is empty"
        in 1..3 -> "String is short"
        in 4..7 -> "String is medium"
        else -> "String is long"
    }
    println(strSize)
}