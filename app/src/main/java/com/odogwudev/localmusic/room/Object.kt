package com.odogwudev.localmusic.room

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.odogwudev.localmusic.utils.Helpers

open class Object {
    @PrimaryKey(autoGenerate = true)
    var mId: Int? = null

    @ColumnInfo(name = "create_time")
    var mCreateTime: String? = null

    @ColumnInfo(name = "update_time")
    var mUpdateTime: String? = null

    constructor(updateTime: String) {
        mCreateTime = Helpers.getTimeToDay()
        mUpdateTime = updateTime
    }

    constructor()
}