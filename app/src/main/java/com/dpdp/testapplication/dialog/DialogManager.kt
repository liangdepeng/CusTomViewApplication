package com.dpdp.testapplication.dialog

import android.app.Activity
import android.view.View
import com.dpdp.testapplication.base.BaseActivity
import com.dpdp.testapplication.R
import android.widget.TextView
import com.dpdp.testapplication.BaseApp
import com.dpdp.testapplication.databinding.DialogTipsBinding
import java.util.concurrent.ConcurrentLinkedDeque

class DialogManager {

    companion object {

        var showingDialog: BaseDialog? = null
        private var loadingDialog: BaseDialog? = null
        private val dialogQueue = ConcurrentLinkedDeque<BaseDialog>()

        fun showLoadingDialog(loadingTxt: String? = null) {
            val topActivity = BaseActivity.getTopActivity() ?: return
            if (loadingDialog == null) {
                loadingDialog = BaseDialog.Builder(topActivity)
                    .setOutSideCanCancel(false)
                    .setBackCancelable(false)
                    .setDimAmount(0.7f)
                    .setDialogLayout(R.layout.dialog_loading_layout) { contentView, dialog ->
                        contentView.findViewById<TextView>(R.id.loading_tv).text = loadingTxt
                    }.build()
            }
            loadingDialog?.show()
        }

        fun dismissLoadingDialog() {
            loadingDialog?.cancel()
            loadingDialog = null
        }


        fun showTipsDialog(
            activity: Activity,
            title: String?,
            content: String?,
            isAddDialogQueue: Boolean = true
        ) {
            val dialog = BaseDialog.Builder(activity)
                .setDialogWidth(dp2px(260f))
                .setOutSideCanCancel(true)
                .setOutSideCanClick(false)
                .setDialogLayout(R.layout.dialog_tips, object : BaseDialog.IViewBindCallBack {
                    override fun OnBindView(contentView: View?, dialog: BaseDialog?) {
                        val binding = DialogTipsBinding.bind(contentView!!)
                        binding.title.text = title
                        binding.content.text = content
                        binding.btn.setOnClickListener {
                            dialog?.dismiss()
                        }
                    }
                }).build()
            showDialog(dialog, isAddDialogQueue)
        }


        // 显示Dialog 调用这个方法 会自动插入显示队列
        private fun showDialog(dialog: BaseDialog?, isAddDialogQueue: Boolean) {
            if (isAddDialogQueue) {
                addDialogQueue(dialog)
            } else {
                dialog?.show()
            }
        }

        private fun addDialogQueue(dialog: BaseDialog?) {
            if (dialog == null)
                return
            if (showingDialog == null) {
                dialog.show()
                showingDialog = dialog
            } else {
                dialogQueue.add(dialog)
            }
        }

        // dismiss 调用此方法 来显示队列中下一个dialog
        fun notifyDialogQueueDismiss() {
            if (showingDialog == null || showingDialog?.isShowing == false) {
                showingDialog = dialogQueue.poll()
                showingDialog?.delayShow(1000)
            }
        }

        private fun dp2px(dpValue: Float): Int {
            val scale = BaseApp.globalContext.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }

}