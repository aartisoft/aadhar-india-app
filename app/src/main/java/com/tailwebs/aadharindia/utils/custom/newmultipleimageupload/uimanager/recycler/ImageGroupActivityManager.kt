package com.tailwebs.baseproject.newimageupload.uimanager.recycler

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ViewAnimator

import com.loopeer.android.librarys.imagegroupview.uimanager.IGActivityLifecycleCallbacks
import com.loopeer.android.librarys.imagegroupview.uimanager.IGLoadHelper
import com.loopeer.android.librarys.imagegroupview.uimanager.IGRecycler
import com.loopeer.android.librarys.imagegroupview.uimanager.recycler.IGLoad
import com.loopeer.android.librarys.imagegroupview.uimanager.recycler.ImageGroupUIManager
import com.tailwebs.aadharindia.R
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.model.ImageFolder


open class ImageGroupActivityManager<T>(context: Context, igRecycler: IGRecycler<T>) : ImageGroupUIManager<T>(context, igRecycler), IGActivityLifecycleCallbacks {


    override fun onCreated() {
        setUpLoadHelper()
        initAdapter()
        setUpView()
    }



    override fun setUpLoadHelper() {
        mLoadHelper = IGLoad(mContext!!, (mContext as Activity).findViewById(R.id.view_album_animator))
    }


    override fun onDestroyed() {
        destroyAdapter()
        cleanRecyclerView()
    }


    override fun isFinishing(): Boolean {
        return (mContext as Activity).isFinishing
    }

    override fun onFolderItemSelected(imageFolder: ImageFolder?) {
        updateContentView(imageFolder)
    }



    private fun updateContentView(folder: ImageFolder?) {
        if (folder?.images?.size == 0) {
            mLoadHelper?.showEmpty()
        } else {
            mLoadHelper?.showContent()
        }
        mIGRcycler?.adapterUpdateContentView(madapter!!,folder)
        mRecyclerView?.scrollToPosition(0)
    }


}