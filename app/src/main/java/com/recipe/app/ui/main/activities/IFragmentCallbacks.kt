package com.recipe.app.ui.main.activities

interface  IFragmentCallbacks {
    fun onFilterSelected(o:Int)

}


interface  IFragmentProfileChooseCallback {
    fun   chooseImage();
    fun removeImage();
}