package com.fridaytech.zrxkit.sample

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fridaytech.zrxkit.sample.core.TransactionRecord
import kotlinx.android.synthetic.main.fragment_transactions.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private val transactionsAdapter = TransactionsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)

            viewModel.transactions.observe(this, Observer { txs ->
                txs?.let { transactions ->
                    transactionsAdapter.items = transactions
                    transactionsAdapter.notifyDataSetChanged()
                }
            })

            viewModel.lastBlockHeight.observe(this, Observer { height ->
                height?.let {
                    transactionsAdapter.lastBlockHeight = height
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_transactions, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactions.adapter = transactionsAdapter
        transactions.layoutManager = LinearLayoutManager(context)

        ethFilter.setOnClickListener { viewModel.filterTransactions(0) }
        wethFilter.setOnClickListener { viewModel.filterTransactions(1) }
        tokenFilter.setOnClickListener { viewModel.filterTransactions(2) }
    }
}

class TransactionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = listOf<TransactionRecord>()
    var lastBlockHeight: Long = 0

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            TransactionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransactionViewHolder -> holder.bind(items[position], itemCount - position, lastBlockHeight)
        }
    }
}

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val summary = itemView.findViewById<TextView>(R.id.summary)!!

    fun bind(tx: TransactionRecord, index: Int, lastBlockHeight: Long) {
        itemView.setBackgroundColor(if (index % 2 == 0)
            Color.parseColor("#dddddd")
        else
            Color.TRANSPARENT
        )

        val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        var value = """
            - #$index
            - Tx Hash: ${tx.transactionHash}
            - Tx Index: ${tx.transactionIndex}
            - Inter Tx Index: ${tx.interTransactionIndex}
            - Time: ${format.format(Date(tx.timestamp * 1000))}
            - From: ${tx.from.address}
            - To: ${tx.to.address}
            - Amount: ${Utils.df.format(tx.amount)}
        """

        if (lastBlockHeight > 0)
            value += "\n- Confirmations: ${tx.blockHeight?.let { lastBlockHeight - it } ?: 0}"

        summary.text = value.trimIndent()
    }
}
