package com.dinokeylas.toyotafunservice

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinokeylas.toyotafunservice.bot.ChatAdapter
import com.dinokeylas.toyotafunservice.bot.ClickListener
import com.dinokeylas.toyotafunservice.bot.Message
import com.dinokeylas.toyotafunservice.bot.RecyclerTouchListener
import com.ibm.cloud.sdk.core.http.HttpMediaType
import com.ibm.cloud.sdk.core.http.ServiceCall
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.assistant.v1.Assistant
import com.ibm.watson.assistant.v2.model.CreateSessionOptions
import com.ibm.watson.assistant.v2.model.MessageInput
import com.ibm.watson.assistant.v2.model.MessageOptions
import com.ibm.watson.assistant.v2.model.SessionResponse
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType
import com.ibm.watson.speech_to_text.v1.SpeechToText
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback
import com.ibm.watson.text_to_speech.v1.TextToSpeech
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions
import kotlinx.android.synthetic.main.content_chat_room.*
import java.io.InputStream
import kotlin.collections.ArrayList

class ChatBotFragment : Fragment() {

    private val TAG = "TAGPERMISSION"
    private val RECORD_REQUEST_CODE = 101
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    private lateinit var watsonAssistant: Assistant
    private lateinit var watsonAssistantSession: SessionResponse
    private lateinit var speechService: SpeechToText
    private lateinit var textToSpeech: TextToSpeech

    private lateinit var messageArrayList: ArrayList<Message>
    private lateinit var mAdapter: ChatAdapter

    private lateinit var microphoneHelper: MicrophoneHelper
    private lateinit var capture: MicrophoneInputStream

    private var initialRequest = false
    private var permissionToRecordAccepted = false
    private var listening = false

    private val streamPlayer = StreamPlayer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_bot, container, false)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment = ChatBotFragment()
    }

    fun createServices() {
        watsonAssistant =
            Assistant("2018-11-08", IamAuthenticator(context?.getString(R.string.assistant_apikey)))
        watsonAssistant.serviceUrl = context?.getString(R.string.assistant_url)

        textToSpeech = TextToSpeech(IamAuthenticator(context?.getString(R.string.TTS_apikey)))
        textToSpeech.serviceUrl = context?.getString(R.string.TTS_url)

        speechService = SpeechToText(IamAuthenticator(context?.getString(R.string.STT_apikey)))
        speechService.serviceUrl = context?.getString(R.string.STT_url)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        messageArrayList = arrayListOf()
        mAdapter = ChatAdapter(messageArrayList as ArrayList<Message>)
        microphoneHelper = MicrophoneHelper(activity)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.setStackFromEnd(true)
        recycler_view?.setLayoutManager(layoutManager)
        recycler_view?.setItemAnimator(DefaultItemAnimator())
        recycler_view?.setAdapter(mAdapter)
        message?.setText("")
        initialRequest = true


        val permission = ContextCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.RECORD_AUDIO
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        } else {
            Log.i(TAG, "Permission to record was already granted")
        }


        recycler_view?.addOnItemTouchListener(
            RecyclerTouchListener(
                context,
                recycler_view,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                        val audioMessage = messageArrayList.get(position) as Message
                        if (audioMessage != null && !audioMessage.getMessage().isEmpty()) {
                            SayTask().execute(audioMessage.getMessage())
                        }
                    }

                    override fun onLongClick(view: View, position: Int) {
                        recordMessage()

                    }
                })
        )

        btn_send.setOnClickListener(View.OnClickListener {
            if (checkInternetConnection()) {
                sendMessage()
            }
        })

        btn_record.setOnClickListener(View.OnClickListener { recordMessage() })

        createServices()
        sendMessage()
    }

    // Speech-to-Text Record Audio permission
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> permissionToRecordAccepted =
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            RECORD_REQUEST_CODE -> {

                if (grantResults.size == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
                return
            }

            MicrophoneHelper.REQUEST_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission to record audio denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        // if (!permissionToRecordAccepted ) finish();

    }

    fun makeRequest() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            MicrophoneHelper.REQUEST_PERMISSION
        )
    }

    // Sending a message to Watson Assistant Service
    private fun sendMessage() {

        val inputmessage = message.text.toString().trim()
        if (!initialRequest) {
            val message = Message()
            message.message = inputmessage
            message.id = "1"
            messageArrayList.add(message)
        } else {
            val message = Message()
            message.message = inputmessage
            message.id = "100"
            this.initialRequest = false
            Toast.makeText(
                context,
                "Tap on the message for Voice",
                Toast.LENGTH_LONG
            ).show()

        }

        message?.setText("")
        mAdapter.notifyDataSetChanged()

/*
        val thread = Thread(Runnable{
            object : Runnable {
                override fun run() {
                    try {
                        if (watsonAssistantSession == null) {
                            val call: ServiceCall<SessionResponse> = watsonAssistant.createSession(
                                CreateSessionOptions.Builder().assistantId(context?.getString(R.string.assistant_id)).build()
                            );
                            watsonAssistantSession = call.execute().getResult()
                        }

                        val input = MessageInput.Builder()
                            .text(inputmessage)
                            .build()
                        val options = MessageOptions.Builder()
                            .assistantId(context?.getString(R.string.assistant_id))
                            .input(input)
                            .sessionId(watsonAssistantSession.sessionId)
                            .build()
                        val response = watsonAssistant.message(options).execute().result
                        Log.i(TAG, "run: " + response!!)
                        val outMessage = Message()
                        if (response != null &&
                            response.output != null &&
                            !response.output.generic.isEmpty() &&
                            "text" == response.output.generic[0].responseType()
                        ) {
                            outMessage.message = response.output.generic[0].text()
                            outMessage.id = "2"

                            messageArrayList.add(outMessage)

                            // speak the message
                            SayTask().execute(outMessage.message)

                            activity?.runOnUiThread({
                                object : Runnable {
                                    override fun run() {
                                        mAdapter.notifyDataSetChanged()
                                        if (mAdapter.itemCount > 1) {
                                            recycler_view?.getLayoutManager()!!
                                                .smoothScrollToPosition(
                                                    recycler_view,
                                                    null,
                                                    mAdapter.itemCount - 1
                                                )

                                        }
                                    }
                                }
                            })
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })

        thread.start()
*/

    }

    private inner class SayTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String {
            streamPlayer.playStream(
                textToSpeech.synthesize(
                    SynthesizeOptions.Builder()
                        .text(params[0])
                        .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                        .accept(HttpMediaType.AUDIO_WAV)
                        .build()
                ).execute().result
            )
            return "Did synthesize"
        }
    }

    private fun recordMessage() {
        if (!listening) {
            capture = microphoneHelper.getInputStream(true)
            Thread(Runnable {
                try {
                    speechService.recognizeUsingWebSocket(
                        getRecognizeOptions(capture),
                        MicrophoneRecognizeDelegate()
                    )
                } catch (e: Exception) {
                    showError(e)
                }
            }).start()
            listening = true
            Toast.makeText(context, "Listening....Click to Stop", Toast.LENGTH_LONG)
                .show()

        } else {
            try {
                microphoneHelper.closeInputStream()
                listening = false
                Toast.makeText(
                    context,
                    "Stopped Listening....Click to Start",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Check Internet Connection
     *
     * @return
     */
    private fun checkInternetConnection(): Boolean {
        // get Connectivity Manager object to check connection
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        // Check for network connections
        if (isConnected) {
            return true
        } else {
            Toast.makeText(context, " No Internet Connection available ", Toast.LENGTH_LONG).show()
            return false
        }

    }

    //Private Methods - Speech to Text
    private fun getRecognizeOptions(audio: InputStream): RecognizeOptions {
        return RecognizeOptions.Builder()
            .audio(audio)
            .contentType(ContentType.OPUS.toString())
            .model("en-US_BroadbandModel")
            .interimResults(true)
            .inactivityTimeout(2000)
            .build()
    }

    //Watson Speech to Text Methods.
    private inner class MicrophoneRecognizeDelegate : BaseRecognizeCallback() {
        override fun onTranscription(speechResults: SpeechRecognitionResults) {
            if (speechResults.results != null && !speechResults.results.isEmpty()) {
                val text = speechResults.results[0].alternatives[0].transcript
                showMicText(text)
            }
        }

        override fun onError(e: Exception) {
            showError(e)
            enableMicButton()
        }

        override fun onDisconnected() {
            enableMicButton()
        }

    }

    private fun showMicText(text: String) {
        activity?.runOnUiThread({
            object : Runnable {
                override fun run() {
                    message?.setText(text)
                }
            }
        })
    }

    private fun enableMicButton() {
        activity?.runOnUiThread({
            object : Runnable {
                override fun run() {
                    btn_record.setEnabled(true)
                }
            }
        })
    }

    private fun showError(e: Exception) {
        activity?.runOnUiThread({
            object : Runnable {
                override fun run() {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        })
    }


}
