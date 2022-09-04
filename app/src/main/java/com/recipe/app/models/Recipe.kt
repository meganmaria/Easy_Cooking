package com.recipe.app.models

class Recipe {
    var recipeName:String=""
    var recipeIngredients:String=""
    var recipeKey:String=""
    var recipeDescription:String=""
    var recipeAddedBy:String=""
    var recipeAddedByName:String=""
    var  imageurl=""
    var rating=0.0F;
    var ratings=ArrayList<Rating>()

    constructor(
        recipeName: String,
        recipeIngredients: String,
        recipeKey: String,
        recipeDescription: String,
        recipeAddedBy: String,
        recipeAddedName: String,

    ) {
        this.recipeName = recipeName
        this.recipeIngredients = recipeIngredients
        this.recipeKey = recipeKey
        this.recipeDescription = recipeDescription
        this.recipeAddedBy = recipeAddedBy
        this.recipeAddedByName=recipeAddedName

    }

    constructor()


}

