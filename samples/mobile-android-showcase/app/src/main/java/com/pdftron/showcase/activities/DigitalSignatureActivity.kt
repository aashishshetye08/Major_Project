package com.pdftron.showcase.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pdftron.pdf.config.PDFViewCtrlConfig
import com.pdftron.pdf.config.ToolManagerBuilder
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.utils.AnalyticsHandlerAdapter
import com.pdftron.pdf.utils.CommonToast
import com.pdftron.pdf.utils.Utils
import com.pdftron.showcase.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_bottom_sheet.*
import java.io.File

class DigitalSignatureActivity : FeatureActivity() {

    val STORAGE_PERMISSION_REQUEST: Int = 1000
    private val DEFAULT_KEYSTORE = "https://pdftron.s3.amazonaws.com/downloads/android/pdftron.pfx"
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        sampleFileName = "digital_signature"
        super.onCreate(savedInstanceState)

        // Request storage permission so that we can copy the key file to external storage
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val externalStoragePublicDirectory =
                            File(
                                    Utils.getExternalDownloadDirectory(this),
                                    "demo_keystore_password_is_password.pfx"
                            )

                    if (!externalStoragePublicDirectory.exists()) { // need to download the keystore if it doesn't exist
                        if (hasInternetConnection(this)) {
                            compositeDisposable.add(
                                    Utils.simpleHTTPDownload(DEFAULT_KEYSTORE, externalStoragePublicDirectory)
                                            .subscribeOn(Schedulers.io())
                                            .doOnError {
                                                val runtimeException = RuntimeException(it)
                                                AnalyticsHandlerAdapter.getInstance().sendException(runtimeException)
                                                CommonToast.showText(this, R.string.dig_sig_keystore_download_error, Toast.LENGTH_LONG)
                                                throw runtimeException
                                            }
                                            .doOnSubscribe {
                                                val keystoreMsg =
                                                        String.format(resources.getString(R.string.dig_sig_default),
                                                                externalStoragePublicDirectory.absolutePath
                                                        )

                                                val preText = feature_description.text.toString()
                                                feature_description.text = ("$preText\n\n$keystoreMsg")
                                            }
                                            .subscribe()
                            )
                        } else {
                            CommonToast.showText(this, R.string.dig_sig_network, Toast.LENGTH_LONG)
                        }
                    } else {
                        val keystoreMsg =
                                String.format(resources.getString(R.string.dig_sig_default),
                                        externalStoragePublicDirectory.absolutePath
                                )
                        val preText = feature_description.text.toString()
                        feature_description.text = ("$preText\n\n$keystoreMsg")
                    }
                } else {
                    CommonToast.showText(this, R.string.dig_sig_permission, Toast.LENGTH_LONG)
                    finish()
                }
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun hasInternetConnection(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val cm = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    override fun getViewerConfig(): ViewerConfig {
        val pdfviewCtrlConfig = PDFViewCtrlConfig.getDefaultConfig(this)
        pdfviewCtrlConfig.isThumbnailUseEmbedded = false
        val toolManagerBuilder = ToolManagerBuilder.from()
                .setUseDigitalSignature(true)
        val builder = ViewerConfig.Builder()
        return builder
                .multiTabEnabled(true)
                .showCloseTabOption(false)
                .pdfViewCtrlConfig(pdfviewCtrlConfig)
                .toolManagerBuilder(toolManagerBuilder)
                .build()
    }

}