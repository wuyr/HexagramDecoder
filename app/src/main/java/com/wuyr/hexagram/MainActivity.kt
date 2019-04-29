package com.wuyr.hexagram

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.act_main_view.*

/**
 * @author wuyr
 * @github https://github.com/wuyr/HexagramDecoder
 * @since 2019-04-28 下午6:30
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mClipboardManager: ClipboardManager
    private val mTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val content = s.toString()
            if (content.isEmpty()) {
                clearPlainText(null)
                clearCipherText(null)
                return
            }
            if (isCipherTextEditing()) {
                val result = s.toString().convertToSymbol()
                setPlainTextWithoutWatcher(if (result.isEmpty()) content.decodeHexagram() else result.decodeHexagram())
            } else {
                var result = content.encodeHexagram()
                if (isName()){
                    val temp = result.convertToText()
                    if (temp.isNotEmpty()){
                        result = temp
                    }
                }
                setCipherTextWithoutWatcher(result)
            }
        }
    }

    fun isName() = type.isChecked

    fun isCipherTextEditing() = cipherTextView.hasFocus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main_view)
        mClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        plainTextView.addTextChangedListener(mTextWatcher)
        cipherTextView.addTextChangedListener(mTextWatcher)
        type.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                convertToText()
            } else {
                convertToSymbol()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getContentFromClipboard()?.let {
            if (it.isCipherText()) {
                pasteCipherText(it)
                copyPlainText(null)
            } else {
                pastePlainText(it)
                copyCipherText(null)
            }
        }
    }

    fun clearPlainText(view: View?) {
        setPlainTextWithoutWatcher("")
    }

    fun copyPlainText(view: View?) {
        writeContentToClipboard(plainTextView.text.toString(), false)
    }

    fun pastePlainText(view: View) {
        getContentFromClipboard()?.let { pastePlainText(it) }
    }

    fun clearCipherText(view: View?) {
        setCipherTextWithoutWatcher("")
    }

    fun copyCipherText(view: View?) {
        writeContentToClipboard(cipherTextView.text.toString(), true)
    }

    fun pasteCipherText(view: View) {
        getContentFromClipboard()?.let { pasteCipherText(it) }
    }

    private fun convertToSymbol() {
        val result = cipherTextView.text.toString().convertToSymbol()
        if (result.isNotEmpty()) {
            setCipherTextWithoutWatcher(result)
        }
    }

    private fun convertToText() {
        val result = cipherTextView.text.toString().convertToText()
        if (result.isNotEmpty()) {
            setCipherTextWithoutWatcher(result)
        }
    }

    private fun pasteCipherText(content: String) {
        cipherTextView.requestFocus()
        cipherTextView.setText(content)
        cipherTextView.setSelection(content.length)
    }

    private fun pastePlainText(content: String) {
        plainTextView.requestFocus()
        plainTextView.setText(content)
        plainTextView.setSelection(content.length)
    }

    private fun setCipherTextWithoutWatcher(result: String) {
        cipherTextView.removeTextChangedListener(mTextWatcher)
        cipherTextView.setText(result)
        cipherTextView.addTextChangedListener(mTextWatcher)
    }

    private fun setPlainTextWithoutWatcher(result: String) {
        plainTextView.removeTextChangedListener(mTextWatcher)
        plainTextView.setText(result)
        plainTextView.addTextChangedListener(mTextWatcher)
    }

    private fun getContentFromClipboard(): String? {
        mClipboardManager.primaryClip?.let {
            if (it.itemCount > 0) {
                return it.getItemAt(0)?.text?.toString()
            }
        }
        return null
    }

    private fun writeContentToClipboard(content: String, isCipherText: Boolean) {
        mClipboardManager.primaryClip = ClipData.newPlainText("hexagram", content)
        Toast.makeText(this, "已复制${if (isCipherText) "密文" else "明文"}到剪贴板", Toast.LENGTH_SHORT)
            .show()
    }
}
