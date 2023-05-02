package com.example.bam_content_provider_consumer

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import com.example.bam_content_provider_consumer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var layout: ActivityMainBinding
    private val adapter = CreditCardsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(layout.root)

        setUpLayout()
    }

    private fun setUpLayout() {
        layout.apply {
            recyclerViewCreditCards.adapter = adapter
            buttonFetchCreditCards.setOnClickListener {
                closeKeyboard()
                fetchUserCreditCards()
            }
            editTextLogin.doOnTextChanged { _, _, _, _ ->
                refreshButtonState()
            }
            editTextPassword.doOnTextChanged { _, _, _, _ ->
                refreshButtonState()
            }
        }
    }

    private fun refreshButtonState() {
        layout.buttonFetchCreditCards.isEnabled = !layout.editTextLogin.text.isNullOrEmpty() && !layout.editTextPassword.text.isNullOrEmpty()
    }

    private fun closeKeyboard() {
        currentFocus?.let {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(it.windowToken, 0)
            it.clearFocus()
        }
    }

    private fun fetchUserCreditCards() {
        val selectionArgs = arrayOf(
            layout.editTextLogin.text.toString(),
            layout.editTextPassword.text.toString()
        )

        val fetchedCards = mutableListOf<CreditCard>()
        val cursor = contentResolver.query(
            contentUri,
            projection,
            selectionClause,
            selectionArgs,
            null
        )
        cursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val cardId = cursor.getString(0).toLong()
                    val userOwnerId = cursor.getString(1).toLong()
                    val creditCardNumber = cursor.getString(2)

                    fetchedCards.add(CreditCard(cardId, userOwnerId, creditCardNumber))
                } while (cursor.moveToNext())
            }
        } ?: showToast("There is no user with provided credentials.")

        adapter.submitList(fetchedCards.toList())
    }

    private companion object {
        private const val providerName = "com.grzegorzpodwika.projectbam.authorities.CREDIT_CARDS"
        private const val tableName = "credit_card"
        val contentUri: Uri = Uri.parse("content://$providerName/$tableName")

        val projection = arrayOf("*")
        const val selectionClause = "login = ? AND password = ?"
    }
}