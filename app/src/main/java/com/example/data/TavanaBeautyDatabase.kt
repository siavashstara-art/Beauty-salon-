package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.CustomerDao
import com.example.data.dao.SalonProfileDao
import com.example.data.dao.SavedPreviewDao
import com.example.data.dao.VisitDao
import com.example.data.entity.CustomerEntity
import com.example.data.entity.SalonProfileEntity
import com.example.data.entity.SavedPreviewEntity
import com.example.data.entity.VisitEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Database(
    entities = [
        CustomerEntity::class,
        SalonProfileEntity::class,
        VisitEntity::class,
        SavedPreviewEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TavanaBeautyDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun salonProfileDao(): SalonProfileDao
    abstract fun visitDao(): VisitDao
    abstract fun savedPreviewDao(): SavedPreviewDao

    companion object {
        @Volatile
        private var INSTANCE: TavanaBeautyDatabase? = null

        fun getDatabase(context: Context): TavanaBeautyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TavanaBeautyDatabase::class.java,
                    "tavana_beauty_studio.db"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed default salon profile and starter clients for realistic salon operation
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getDatabase(context)
                            seedInitialData(database)
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedInitialData(database: TavanaBeautyDatabase) {
            // 1. Seed Salon Profile
            database.salonProfileDao().insertOrUpdate(
                SalonProfileEntity(
                    id = 1L,
                    salonName = "استودیو زیبایی توانا",
                    managerName = "سرکار خانم توانا",
                    city = "تهران",
                    phone = "02126200000",
                    whatsapp = "+989121234567",
                    address = "تهران، زعفرانیه، تقاطع مقدس اردبیلی، پلاک ۱۴",
                    logo = ""
                )
            )

            // 2. Seed starter clients with realistic salon visit dates
            val now = System.currentTimeMillis()
            val c1Id = database.customerDao().insertCustomer(
                CustomerEntity(
                    name = "مریم صبوری",
                    phone = "09121112233",
                    notes = "پوست حساس، ترجیح رنگ موهای دودی و زیتونی",
                    lastVisit = now - TimeUnit.DAYS.toMillis(42L), // Inactive -> for recovery!
                    preferredHairstyle = "مدل باب کلاسیک (French Bob)",
                    preferredMakeup = "نود گِلُو و طبیعی (Nude Glow)",
                    totalVisits = 3
                )
            )

            val c2Id = database.customerDao().insertCustomer(
                CustomerEntity(
                    name = "سارا علیزاده",
                    phone = "09354445566",
                    notes = "علاقه‌مند به شینیون‌های خطی و سبک‌های مدرن",
                    lastVisit = now - TimeUnit.DAYS.toMillis(8L), // Active
                    preferredHairstyle = "مواج هالیوودی رمانتیک (Hollywood Waves)",
                    preferredMakeup = "سافت گِلَم مجلسی (Soft Glam)",
                    totalVisits = 5
                )
            )

            val c3Id = database.customerDao().insertCustomer(
                CustomerEntity(
                    name = "نیلوفر کاظمی",
                    phone = "09197778899",
                    notes = "عروس پاییز، رزرو مشاوره پکیج VIP",
                    lastVisit = now - TimeUnit.DAYS.toMillis(50L), // Inactive -> for recovery!
                    preferredHairstyle = "شینیون اروپایی خطی (Bridal Updo)",
                    preferredMakeup = "میکاپ سلطنتی عروس (Royal Bridal)",
                    totalVisits = 2
                )
            )

            // Seed sample visits
            database.visitDao().insertVisit(
                VisitEntity(
                    customerId = c1Id,
                    visitDate = now - TimeUnit.DAYS.toMillis(42L),
                    serviceProvided = "کوپ ژورنالی و براشینگ مجلسی",
                    hairstyleChosen = "مدل باب فرانسوی",
                    makeupChosen = "نود گلو",
                    notes = "رضایت کامل از کوتاهی، پیشنهاد دوره ترمیم ۴۰ روزه",
                    nextSuggestedVisit = now + TimeUnit.DAYS.toMillis(5L)
                )
            )

            database.visitDao().insertVisit(
                VisitEntity(
                    customerId = c2Id,
                    visitDate = now - TimeUnit.DAYS.toMillis(8L),
                    serviceProvided = "براشینگ هالیوودی و میکاپ مجلسی",
                    hairstyleChosen = "موج هالیوودی",
                    makeupChosen = "سافت گلم",
                    notes = "استایل برای جشن عقد",
                    nextSuggestedVisit = now + TimeUnit.DAYS.toMillis(30L)
                )
            )

            database.visitDao().insertVisit(
                VisitEntity(
                    customerId = c3Id,
                    visitDate = now - TimeUnit.DAYS.toMillis(50L),
                    serviceProvided = "تست اولیه شینیون و گریم عروس",
                    hairstyleChosen = "شینیون کلاسیک",
                    makeupChosen = "میکاپ رویال",
                    notes = "پکیج تست با موفقیت انجام شد، نیاز به هماهنگی فیشیال نهایی",
                    nextSuggestedVisit = now + TimeUnit.DAYS.toMillis(10L)
                )
            )
        }
    }
}
