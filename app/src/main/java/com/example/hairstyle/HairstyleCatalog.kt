package com.example.hairstyle

import com.example.domain.model.FaceShape
import com.example.domain.model.Hairstyle
import com.example.domain.model.HairstyleCategory

object HairstyleCatalog {
    val allHairstyles = listOf(
        Hairstyle(
            id = "hair_bob_classic",
            nameFa = "مدل باب کلاسیک (French Bob)",
            nameEn = "Classic French Bob",
            category = HairstyleCategory.BOB,
            length = "کوتاه تا خط چانه",
            suitableFaceShapes = listOf(FaceShape.OVAL, FaceShape.HEART, FaceShape.LONG),
            description = "برشی شیک و مدرن تا زیر گوش یا خط فک، ایجاد کادر ظریف دور چهره و برجسته‌سازی استخوان گونه.",
            previewAsset = "ic_hair_bob",
            tipsFa = "برای چهره‌های کشیده یا قلبی عالی است زیرا عرض چهره را متعادل می‌کند."
        ),
        Hairstyle(
            id = "hair_layered_long",
            nameFa = "لایه‌ای پرحجم با لایت (Layered Volume)",
            nameEn = "Long Textured Layers",
            category = HairstyleCategory.LAYERED,
            length = "بلند تا زیر شانه",
            suitableFaceShapes = listOf(FaceShape.ROUND, FaceShape.SQUARE, FaceShape.OVAL),
            description = "لایه‌های نامنظم با حجم دهی طبیعی دور فک، باریک‌تر نشان دادن صورت‌های گرد و مربعی.",
            previewAsset = "ic_hair_layered",
            tipsFa = "خط فک قوی و زوایای تند صورت‌های مربعی را تلطیف می‌کند."
        ),
        Hairstyle(
            id = "hair_waves_hollywood",
            nameFa = "مواج هالیوودی رمانتیک (Hollywood Waves)",
            nameEn = "Romantic Hollywood Waves",
            category = HairstyleCategory.CURLY,
            length = "بلند مجلسی",
            suitableFaceShapes = listOf(FaceShape.OVAL, FaceShape.SQUARE, FaceShape.HEART, FaceShape.ROUND),
            description = "موج‌های یکدست و براق کلاسیک، ایده‌آل برای مراسم، جشن‌ها و مجالس شب با جلوه درخشان.",
            previewAsset = "ic_hair_waves",
            tipsFa = "به چهره نشاط و جلوه لوکس می‌بخشد و تقارن چهره را تقویت می‌کند."
        ),
        Hairstyle(
            id = "hair_bridal_updo",
            nameFa = "شینیون اروپایی خطی (Bridal Updo)",
            nameEn = "Elegant Bridal Chignon",
            category = HairstyleCategory.BRIDAL,
            length = "شینیون جمع",
            suitableFaceShapes = listOf(FaceShape.OVAL, FaceShape.ROUND, FaceShape.HEART),
            description = "شینیون پایین با بافت‌های خطی و لطیف، کشیدگی گردن و استخوان ترقوه را بسیار جذاب نشان می‌دهد.",
            previewAsset = "ic_hair_bridal",
            tipsFa = "بهترین انتخاب برای عروس‌های خاص با تاچ رمانتیک و تاج ظریف."
        ),
        Hairstyle(
            id = "hair_curtain_bangs_lob",
            nameFa = "لاب متوسط با چتری پرده‌ای (Curtain Bangs Lob)",
            nameEn = "Curtain Bangs Long Bob",
            category = HairstyleCategory.BOB,
            length = "متوسط تا روی شانه",
            suitableFaceShapes = listOf(FaceShape.ROUND, FaceShape.LONG, FaceShape.SQUARE, FaceShape.OVAL),
            description = "چتری‌های پرده‌ای که از دو طرف پیشانی سرازیر می‌شوند، پیشانی و گونه را قاب می‌گیرند.",
            previewAsset = "ic_hair_curtain",
            tipsFa = "بهترین گزینه برای افرادی که تغییر ملایم بدون کوتاه کردن زیاد مو می‌خواهند."
        ),
        Hairstyle(
            id = "hair_pixie_textured",
            nameFa = "پیکسی مدرن حجیم (Textured Pixie)",
            nameEn = "Modern Textured Pixie",
            category = HairstyleCategory.SHORT,
            length = "کوتاه ژورنالی",
            suitableFaceShapes = listOf(FaceShape.OVAL, FaceShape.HEART),
            description = "کوتاهی جسورانه با چتری تکه‌ای و حجم در قسمت بالایی سر، چشم‌ها را به کانون توجه تبدیل می‌کند.",
            previewAsset = "ic_hair_pixie",
            tipsFa = "مناسب برای خانم‌های با اعتماد به نفس و علاقه‌مند به استایل‌های شیک و راحت."
        ),
        Hairstyle(
            id = "hair_sleek_straight",
            nameFa = "لخت شلاقی ابریشمی (Sleek Straight)",
            nameEn = "Silk Sleek Straight",
            category = HairstyleCategory.STRAIGHT,
            length = "بلند صاف",
            suitableFaceShapes = listOf(FaceShape.ROUND, FaceShape.OVAL),
            description = "صافی ژاپنی/کراتین با براقیت آیینه ای، خطوط عمودی که صورت را باریک‌تر و کشیده‌تر جلوه می‌دهد.",
            previewAsset = "ic_hair_straight",
            tipsFa = "خطوط عمودی صاف باعث کم‌عرض‌تر دیده شدن چهره گرد می‌شود."
        ),
        Hairstyle(
            id = "hair_high_ponytail",
            nameFa = "دم اسبی لیفت شده مجلسی (Snatched High Pony)",
            nameEn = "Snatched High Ponytail",
            category = HairstyleCategory.UPDO,
            length = "دم اسبی بالا",
            suitableFaceShapes = listOf(FaceShape.ROUND, FaceShape.OVAL, FaceShape.SQUARE),
            description = "بستن موها در بالاترین نقطه سر با اثر کشیدگی چشم و گونه (لیفتینگ طبیعی چهره).",
            previewAsset = "ic_hair_pony",
            tipsFa = "چهره را شاداب، لیفت و جوان‌تر نشان می‌دهد."
        )
    )
}
