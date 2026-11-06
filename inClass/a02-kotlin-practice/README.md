# In-Class 02 - Kotlin Practice

## Instructions

Use this link to [Kotlin Playground](https://pl.kotl.in/bUn_-VgZd) that has comments for each problem. You can write and run your solutions in Kotlin Playground. When you are finished, use the `Copy link` to copy a link to your work, and paste it below:

**In-class 2 Solution Link:**


## Problem List

### 1. Print these messages on separate lines
```
    Use the val keyword when the value doesn't change.
    Use the var keyword when the value can change.
    When you define a function, you define the parameters that can be passed to it.
    When you call a function, you pass arguments for the parameters.
```

### 2. Uncomment the following code and fix the compile error
```kotlin
println("New chat message from a friend"}
```
### 3. Uncomment the following and fix the compile error
 
```kotlin
val discountPercentage: Int = 0
val offer: String = ""
val item = "Google Chromecast"
discountPercentage = 20
offer = "Sale - Up to $discountPercentage% discount on $item! Hurry up!"

println(offer)
```

### 4. Uncomment and fix this code so it prints:

```
The total part size is: 50
```

```kotlin
val numberOfAdults = "20"
val numberOfKids = "30"
val total = numberOfAdults + numberOfKids
println("The total party size is: $total")
```

### 5. Uncomment and fix this code so it compiles 

(note: having a fraction of a cat might not make sense)

```kotlin
val numCats: Double = 2.0
val numDogs: Int = 2
numDogs += numCats
```

### 6. After fixing number 5, write a print statement to print the number of cats and dogs using the variables, like:
```
There are 2 cats and 4 dogs
```

### 7. Write a when statement that prints whether there are more cats than dogs, more dogs than cats, are there are the same number of each.

Hint: you can simply do 
```kotlin
when {
    // cases
}
```
and compare variables for each condition

### 8. Use a when statement to determine if a character is a vowel or consonant and print the result. 

Char has methods named `lowercaseChar()` and `uppercaseChar()` you can use, or you can assume upper/lowercase.

Hint: you can create a listOf vowels

### 9. Use a for loop to print the numbers 0 to 9 in reverse order. 

Hint - there is a `downTo` keyword

### 10. Create one list with 3 of your favorite shows/movies/games/etc. and print the list

### 11. Create another list with 2 or 3 of your least favorite shows/movies/games/etc. Then, create a third list that contains everything from both lists. Print out this new list.


### 12. Remove your most and least favorite from the combined list, and print this new list. (You may need to modify what you did for 10 and 11)

**If you have time, you can attempt the following problems 13 - 15, but they won't be counted in your grade for this assignment.**

### 13. Create a string variable and set it to null, then print the string

### 14. Make a variable and set it to the length of the string from problem 13 such that if the string is null, the variable will be -1, then print the length.

### 15. Write a when statement that returns a string saying if the string is empty, the string is short, the string is medium length, or the string is long. 

Whatever you consider is short, medium, and long is fine.

## Grading and Turn-in

Use `Copy link` in Kotlin Playground, and paste the link to your work in this file. Additionally, you may copy/paste your entire file into inClass2.kt. 

You will commit the changes to the file(s), and push those commits. Verify your work appears in the browser at `github.com`. That is your submission for this assignment.

This assignment is worth **10 points**.