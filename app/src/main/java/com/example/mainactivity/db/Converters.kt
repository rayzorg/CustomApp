package com.example.mainactivity.db

import androidx.room.TypeConverter
import com.example.mainactivity.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }
    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}
