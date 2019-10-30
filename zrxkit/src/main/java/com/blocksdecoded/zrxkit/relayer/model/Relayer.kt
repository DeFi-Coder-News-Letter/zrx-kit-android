package com.blocksdecoded.zrxkit.relayer.model

import com.blocksdecoded.zrxkit.model.AssetItem

data class Relayer(
    val id: Int,
    val name: String,
    val availablePairs: List<Pair<AssetItem, AssetItem>>,
    val feeRecipients: List<String>,
    val exchangeAddress: String,
    val config: RelayerConfig
)

data class RelayerConfig(
    val baseUrl: String,
    val suffix: String,
    val version: String
)
