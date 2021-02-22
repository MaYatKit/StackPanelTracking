package com.framecad.plum.view.drawing

import android.os.Bundle
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.framecad.plum.R
import com.framecad.plum.StackerWebView
import com.framecad.plum.data.model.SvgItem
import com.framecad.plum.databinding.ActivityStackDrawingBinding
import com.framecad.plum.utils.PreferenceUtils
import com.framecad.plum.utils.WarningDialogHelper
import com.framecad.plum.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class StackDrawingActivity : BaseActivity() {


    private lateinit var binding: ActivityStackDrawingBinding

    private lateinit var webView: WebView

    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_stack_drawing)
        binding.lifecycleOwner = this
        binding.setBackClickListener {
            finish()
        }

        intent.getParcelableExtra<SvgItem>(getString(R.string.svg_drawing_page_item))
            ?.let {
                webView = StackerWebView.getInstance(this)
                startLoad()
                initView()
                binding.titleText = it.name

                loadStack(it.projectId, it.id)
            }
    }

    private fun loadStack(projectId: Long, stackId: String) {
        val accountId = preferenceUtils.getAccountId() ?: ""
        webView.evaluateJavascript("window.state") {
            if (it == null) {
                WarningDialogHelper.create(this, resources.getString(R.string.js_error))
            } else {
                val json = JSONObject(it)
                if (json.getBoolean("loaded")) {
                    webView.evaluateJavascript("callstacker(\"$accountId;$projectId;$stackId\");") {
                        if (it == null) {
                            WarningDialogHelper.create(this, resources.getString(R.string.js_error))
                        }
                        stopLoad()
                    }

                } else {
                    if (json.isNull("error")) {
                        WarningDialogHelper.create(this, resources.getString(R.string.js_error))
                    } else {
                        WarningDialogHelper.create(this, json.getString("error"))
                    }
                }
            }

        }
    }


    private fun initView() {
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, // This will define text view width
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        webView.layoutParams = params
        binding.containerView.addView(webView)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.containerView.removeAllViews()
    }


}