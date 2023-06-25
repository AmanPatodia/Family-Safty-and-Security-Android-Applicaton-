package com.example.myfamilysafty

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
public abstract class MyFamilyDataBase : RoomDatabase() {

    abstract fun contactDao() : ContactDao

    companion object{

        @Volatile
        private var INSTANCE : MyFamilyDataBase?=null

        fun getDatabase(context : Context) : MyFamilyDataBase{
            INSTANCE?.let {
                return it
            }
            return synchronized(MyFamilyDataBase::class.java){
                var Instance = Room.databaseBuilder(context.applicationContext,MyFamilyDataBase::class.java,"my_family_db").build()

                INSTANCE = Instance

                Instance
            }
        }
    }
}