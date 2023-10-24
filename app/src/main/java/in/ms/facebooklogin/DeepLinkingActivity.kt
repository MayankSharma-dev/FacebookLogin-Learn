package `in`.ms.facebooklogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

class DeepLinkingActivity : AppCompatActivity() {


    private lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_deep_linking)

// Initialize Firebase Auth
        auth = Firebase.auth

        callbackManager = CallbackManager.Factory.create()

        val buttonFacebookLogin: LoginButton = findViewById(R.id.login_button)

        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Toast.makeText(applicationContext, "Facebook login Cancel", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(applicationContext, "Facebook login Error", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onSuccess(result: LoginResult) {
                    Toast.makeText(applicationContext, "Facebook login Success", Toast.LENGTH_SHORT)
                        .show()

                    //
                    val graphRequest = GraphRequest.newMeRequest(result?.accessToken){ `object`, response ->
                        getFacebookData(`object`)
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,email,birthday,gender,name")
                    graphRequest.parameters = parameters
                    graphRequest.executeAsync()
                    //


                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun getFacebookData(jsonObject: JSONObject?) {

        val name = jsonObject?.getString("name")
        val birthday = jsonObject?.getString("birthday")
        val gender = jsonObject?.getString("gender")
        val email = jsonObject?.getString("email")

        Log.d(">>>>>>name",""+name)
        Log.d(">>>>>>name",""+birthday)
        Log.d(">>>>>>name",""+gender)
        Log.d(">>>>>>name",""+email)

    }

}