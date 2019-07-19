package com.blocksdecoded.zrxkit.relayer

import com.blocksdecoded.zrxkit.relayer.model.AssetPair
import com.blocksdecoded.zrxkit.relayer.remote.OrderBookResponse
import com.blocksdecoded.zrxkit.model.SignedOrder
import com.blocksdecoded.zrxkit.relayer.model.Relayer
import io.reactivex.Flowable

interface IRelayerManager {
    val availableRelayers: List<Relayer>

    fun getAssetPairs(relayerId: Int): Flowable<List<AssetPair>>

    fun getOrderbook(relayerId: Int, base: String, quote: String): Flowable<OrderBookResponse>

    fun postOrder(relayerId: Int, order: SignedOrder): Flowable<Unit>

    fun getOrders(relayerId: Int, makerAddress: String, makerAsset: String, takerAsset: String): Flowable<OrderBookResponse>
}