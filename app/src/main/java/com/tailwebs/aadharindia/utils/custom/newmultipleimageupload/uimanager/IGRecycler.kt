package com.loopeer.android.librarys.imagegroupview.uimanager


import com.loopeer.android.librarys.imagegroupview.uimanager.recycler.IGUIPattern
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.adapter.RecyclerViewAdapter
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.model.ImageFolder

interface IGRecycler<T>:IGUIPattern{
    fun createRecyclerViewAdapter():RecyclerViewAdapter<T>
    fun adapterUpdateContentView(recyclerViewAdapter: RecyclerViewAdapter<*>, folder: ImageFolder?)
}
