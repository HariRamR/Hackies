package com.hari.hackies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import com.hari.hackies.R

class StoryFrag: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Hackies)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.frag_story, container, false)

        val webView = view.findViewById<WebView>(R.id.web_view_frag_story)

        val bundle = arguments
        if (bundle != null){
            val url = bundle.getString("StoryURL")
            webView.webViewClient = WebViewClient()
            webView.loadUrl(url!!)
            webView.settings.javaScriptEnabled = true
            webView.settings.setSupportZoom(true)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(webView.canGoBack()){
                        webView.goBack()
                    }else activity!!.supportFragmentManager.beginTransaction().remove(this@StoryFrag)
                }
            }
            )

        return view
    }
}