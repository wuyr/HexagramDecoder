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
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mClipboardManager: ClipboardManager
    private val mTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val content = s.toString()
            if (content.isEmpty()) {
                setPlainTextWithoutWatcher("")
                setCipherTextWithoutWatcher("")
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
        setOnClickListener(clearPlain, copyPlain, pastePlain, clearCipher, copyCipher, pasteCipher)

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
        if (contentFromClipboard.isCipherText()) {
            pasteCipherText(contentFromClipboard)
            writeContentToClipboard(plainTextView.text.toString(), false)
        } else {
            pastePlainText(contentFromClipboard)
            writeContentToClipboard(cipherTextView.text.toString(), true)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.clearPlain -> {
                setPlainTextWithoutWatcher("")
            }
            R.id.copyPlain -> {
                writeContentToClipboard(plainTextView.text.toString(), false)
            }
            R.id.pastePlain -> {
                pastePlainText(contentFromClipboard)
            }
            R.id.clearCipher -> {
                setCipherTextWithoutWatcher("")
            }
            R.id.copyCipher -> {
                writeContentToClipboard(cipherTextView.text.toString(), true)
            }
            R.id.pasteCipher -> {
                pasteCipherText(contentFromClipboard)
            }
            else -> {
            }
        }
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

    private fun pasteCipherText(content: String?) {
        content ?: return
        cipherTextView.requestFocus()
        cipherTextView.setText(content)
        cipherTextView.setSelection(content.length)
    }

    private fun pastePlainText(content: String?) {
        content ?: return
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

    private fun setOnClickListener(vararg ids: View?) {
        ids.filterNotNull().forEach {
            it.setOnClickListener(this)
        }
    }

    private val contentFromClipboard: String?
        get() {
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
