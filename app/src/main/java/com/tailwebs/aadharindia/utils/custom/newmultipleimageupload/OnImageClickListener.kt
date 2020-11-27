package com.loopeer.android.librarys.imagegroupview

import android.view.View
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.model.SquareImage



interface OnImageClickListener {
    fun onImageClick(clickImage: View, squareImage: SquareImage)
}