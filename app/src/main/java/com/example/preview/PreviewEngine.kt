package com.example.preview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import com.example.domain.model.GeneratedBy
import com.example.domain.model.PreviewRequest
import com.example.domain.model.PreviewResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PreviewEngine(private val context: Context) {

    suspend fun generatePreview(request: PreviewRequest): PreviewResult = withContext(Dispatchers.IO) {
        val originalFile = File(request.originalImagePath)
        val originalBitmap = if (originalFile.exists()) {
            BitmapFactory.decodeFile(request.originalImagePath)
        } else {
            // Fallback placeholder bitmap if path not accessible
            createStudioPlaceholderBitmap()
        }

        // Apply Local Processing Studio Composite
        val previewBitmap = compositeStudioPreview(originalBitmap, request)

        // Save preview output locally
        val outputDir = File(context.filesDir, "previews").apply { mkdirs() }
        val outputFile = File(outputDir, "preview_${System.currentTimeMillis()}.jpg")
        FileOutputStream(outputFile).use { out ->
            previewBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        PreviewResult(
            originalImage = request.originalImagePath,
            selectedHairstyle = request.selectedHairstyle,
            selectedMakeup = request.selectedMakeup,
            previewImage = outputFile.absolutePath,
            generatedBy = GeneratedBy.LOCAL_PROCESSING,
            notes = "ترکیب استودیویی سالن توانا: مدل مو «${request.selectedHairstyle.nameFa}» با سبک آرایش «${request.selectedMakeup.nameFa}»",
            timestamp = System.currentTimeMillis()
        )
    }

    private fun compositeStudioPreview(src: Bitmap, request: PreviewRequest): Bitmap {
        val width = src.width.coerceAtLeast(600)
        val height = src.height.coerceAtLeast(800)

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        // 1. Draw scaled original photo
        val srcRect = Rect(0, 0, src.width, src.height)
        val dstRect = Rect(0, 0, width, height)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(src, srcRect, dstRect, paint)

        // 2. Salon Studio Vignette and Warm Tone Overlay
        val vignettePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                intArrayOf(Color.TRANSPARENT, Color.argb(40, 199, 109, 126), Color.argb(210, 30, 16, 29)),
                floatArrayOf(0f, 0.65f, 1.0f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), vignettePaint)

        // 3. Subtle golden frame border
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(160, 212, 175, 55) // Gold
            style = Paint.Style.STROKE
            strokeWidth = width * 0.012f
        }
        canvas.drawRoundRect(RectF(16f, 16f, width - 16f, height - 16f), 32f, 32f, borderPaint)

        // 4. Studio Watermark Banner at the bottom
        val bannerHeight = height * 0.22f
        val bannerTop = height - bannerHeight
        val bannerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(220, 28, 14, 27)
        }
        canvas.drawRoundRect(RectF(24f, bannerTop, width - 24f, height - 24f), 24f, 24f, bannerPaint)

        // Gold line separator
        val goldLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(212, 175, 55)
            strokeWidth = 3f
        }
        canvas.drawLine(40f, bannerTop + 8f, width - 40f, bannerTop + 8f, goldLinePaint)

        // Text Info (Persian Studio Branding & Choices)
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(255, 217, 224)
            textSize = (width * 0.038f).coerceIn(28f, 54f)
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("استودیو زیبایی توانا • پیش‌نمایش اختصاصی", width / 2f, bannerTop + (bannerHeight * 0.28f), titlePaint)

        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = (width * 0.030f).coerceIn(22f, 40f)
            textAlign = Paint.Align.CENTER
        }
        val hairText = "مدل مو: ${request.selectedHairstyle.nameFa}"
        canvas.drawText(hairText, width / 2f, bannerTop + (bannerHeight * 0.52f), subPaint)

        val makeupText = "سبک آرایش: ${request.selectedMakeup.nameFa} (${request.selectedMakeup.lipTone})"
        canvas.drawText(makeupText, width / 2f, bannerTop + (bannerHeight * 0.74f), subPaint)

        return output
    }

    private fun createStudioPlaceholderBitmap(): Bitmap {
        val width = 720
        val height = 960
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.rgb(40, 20, 36))
        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(230, 160, 180)
            textSize = 36f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Tavana Beauty Studio Client", width / 2f, height / 2f, p)
        return bitmap
    }
}
