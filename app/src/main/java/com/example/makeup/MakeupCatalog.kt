package com.example.makeup

import com.example.domain.model.FaceShape
import com.example.domain.model.MakeupCategory
import com.example.domain.model.MakeupStyle

object MakeupCatalog {
    val allMakeupStyles = listOf(
        MakeupStyle(
            id = "makeup_nude_glow",
            nameFa = "نود گِلُو و طبیعی (Nude Glow)",
            nameEn = "Natural Nude Glow",
            category = MakeupCategory.NATURAL,
            description = "پوست شیشه‌ای و مرطوب، هایلایت گونه، خط چشم محو و رژلب گوشتی براق برای زیبایی اصیل.",
            previewAsset = "ic_makeup_nude",
            lipTone = "کالباسی / گوشتی براق (Nude Gloss)",
            blushTone = "هلویی ملایم (Soft Peach)",
            eyeStyle = "سایه شاین شامپاینی با مژه تاربه‌تار",
            recommendedForFaceShapes = listOf(FaceShape.OVAL, FaceShape.ROUND, FaceShape.HEART, FaceShape.LONG, FaceShape.SQUARE)
        ),
        MakeupStyle(
            id = "makeup_classic_red",
            nameFa = "کلاسیک هالیوودی با رژ قرمز (Classic Red Lip)",
            nameEn = "Old Hollywood Classic",
            category = MakeupCategory.CLASSIC,
            description = "خط چشم گربه‌ای مشکی دقیق، پوست مات مخملی و رژلب قرمز کلاسیک الهام‌گرفته از جذابیت جاودان.",
            previewAsset = "ic_makeup_classic",
            lipTone = "قرمز مخملی مات (Classic Ruby Red)",
            blushTone = "برنز ملایم (Sculpted Bronze)",
            eyeStyle = "خط چشم کشیده بالدار با خط مژه پرپشت",
            recommendedForFaceShapes = listOf(FaceShape.OVAL, FaceShape.SQUARE, FaceShape.HEART)
        ),
        MakeupStyle(
            id = "makeup_soft_glam",
            nameFa = "سافت گِلَم مجلسی (Soft Glam)",
            nameEn = "Soft Glam Party",
            category = MakeupCategory.PARTY,
            description = "کانتورینگ حرفه‌ای گونه و فک، سایه کاراملی اسموکی با پیگمنت‌های درخشان و مژه اسپایک.",
            previewAsset = "ic_makeup_glam",
            lipTone = "قهوه‌ای نود با خط لب شکلاتی (90s Nude)",
            blushTone = "رز برنز (Rose Bronze)",
            eyeStyle = "سایه اسموکی بادامی با گوشه چشم روشن",
            recommendedForFaceShapes = listOf(FaceShape.ROUND, FaceShape.OVAL, FaceShape.SQUARE, FaceShape.LONG)
        ),
        MakeupStyle(
            id = "makeup_royal_bridal",
            nameFa = "میکاپ سلطنتی عروس (Royal Bridal)",
            nameEn = "Royal Bridal Luxury",
            category = MakeupCategory.BRIDAL,
            description = "پوست ۲۴ ساعته بی‌نقص، هایلایتر مرواریدی، سایه لایت کرم پودری و رژلب صورتی رزی شیک و ماندگار.",
            previewAsset = "ic_makeup_bridal",
            lipTone = "رُز ملیح با لایه‌ای از گلاس شاین دار",
            blushTone = "صورتی مرجانی ظریف (Coral Rose)",
            eyeStyle = "سایه سایه‌روشن نچرال با مژه مگنتی ابریشمی",
            recommendedForFaceShapes = listOf(FaceShape.OVAL, FaceShape.HEART, FaceShape.ROUND)
        ),
        MakeupStyle(
            id = "makeup_french_minimal",
            nameFa = "مینیمال روزمره فرانسوی (Clean Girl Everyday)",
            nameEn = "Minimal Clean Girl",
            category = MakeupCategory.EVERYDAY,
            description = "ابروهای لیفت شده صابونی، بی بی کرم سبک، تینت گونه و لب یکدست برای جلوه طبیعی و بانشاط.",
            previewAsset = "ic_makeup_everyday",
            lipTone = "تینت لب توت فرنگی طبیعی (Berry Tint)",
            blushTone = "تینت هلویی طبیعی",
            eyeStyle = "ریمل تک بدون سایه و با طراوت",
            recommendedForFaceShapes = listOf(FaceShape.OVAL, FaceShape.ROUND, FaceShape.LONG, FaceShape.HEART, FaceShape.SQUARE)
        ),
        MakeupStyle(
            id = "makeup_modern_graphic",
            nameFa = "مدرن گرافیکی و فشن (Graphic Trend)",
            nameEn = "Modern Graphic Chic",
            category = MakeupCategory.MODERN,
            description = "طراحی خط چشم‌های فشن گرافیکی، بازی با سایه‌های ترند و پوست شاداب به سبک فشن‌شوهای میلان.",
            previewAsset = "ic_makeup_modern",
            lipTone = "کاراملی شاین یا لب مات خنثی",
            blushTone = "هلویی زاویه‌دار بالای شقیقه‌ها",
            eyeStyle = "خط چشم دوبل گرافیکی معاصر",
            recommendedForFaceShapes = listOf(FaceShape.OVAL, FaceShape.SQUARE, FaceShape.HEART)
        )
    )
}
