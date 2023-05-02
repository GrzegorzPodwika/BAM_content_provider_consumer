package com.example.bam_content_provider_consumer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bam_content_provider_consumer.databinding.ItemCreditCardBinding

class CreditCardsAdapter :
    ListAdapter<CreditCard, CreditCardsAdapter.CreditCardViewHolder>(CreditCardDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val binding = ItemCreditCardBinding.inflate(LayoutInflater.from(parent.context))

        return CreditCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CreditCardViewHolder(private val binding: ItemCreditCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(creditCard: CreditCard) {
            binding.apply {
                textViewCardNumber.text = creditCard.uniqueNumber
            }
        }
    }

    object CreditCardDiffUtil : DiffUtil.ItemCallback<CreditCard>() {
        override fun areItemsTheSame(oldItem: CreditCard, newItem: CreditCard) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: CreditCard, newItem: CreditCard) =
            oldItem == newItem

    }
}