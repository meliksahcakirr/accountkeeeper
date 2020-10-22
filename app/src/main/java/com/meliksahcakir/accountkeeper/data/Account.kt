package com.meliksahcakir.accountkeeper.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "accounts")
data class Account(
    @ColumnInfo(name = "accountName")
    var accountName: String = "",
    @ColumnInfo(name = "accountNumber")
    var accountNumber: String = "",
    @ColumnInfo(name = "accountDescription")
    var accountDescription: String = "",
    @ColumnInfo(name = "personalAccount")
    var personalAccount: Boolean = true,
    @ColumnInfo(name = "global")
    var global: Boolean = false,
    @ColumnInfo(name = "accountType")
    var accountType: Int = BANK_ACCOUNT,
    @ColumnInfo(name = "userId")
    var userId: String = "",
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "accountId")
    var accountId: String = UUID.randomUUID().toString()

) {
    companion object {
        const val BANK_ACCOUNT = 0
        const val CRYPTO = 1
        const val EMAIL = 2
        const val PHONE = 3
        const val LOCATION = 4
        const val OTHER = 5
    }
}