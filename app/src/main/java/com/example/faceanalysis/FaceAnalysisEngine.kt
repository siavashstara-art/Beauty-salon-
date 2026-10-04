package com.example.faceanalysis

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import android.media.FaceDetector
import com.example.domain.model.FaceProfile
import com.example.domain.model.FaceShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class FaceAnalysisEngine {

    suspend fun analyzeFace(bitmap: Bitmap): FaceProfile = withContext(Dispatchers.Default) {
        try {
            // Android FaceDetector requires RGB_565 and even width
            val width = if (bitmap.width % 2 == 0) bitmap.width else bitmap.width - 1
            val height = bitmap.height

            val safeBitmap = if (bitmap.config != Bitmap.Config.RGB_565 || bitmap.width != width) {
                val converted = Bitmap.createScaledBitmap(bitmap, width, height, true)
                converted.copy(Bitmap.Config.RGB_565, false)
            } else {
                bitmap
            }

            val maxFaces = 1
            val faces = arrayOfNulls<FaceDetector.Face>(maxFaces)
            val detector = FaceDetector(safeBitmap.width, safeBitmap.height, maxFaces)
            val faceCount = detector.findFaces(safeBitmap, faces)

            // Calculate lighting score & image quality
            val (lightingScore, qualityDesc) = evaluateLightingAndQuality(bitmap)

            if (faceCount <= 0 || faces[0] == null) {
                // If FaceDetector missed due to lighting or orientation, do fallback heuristic
                return@withContext FaceProfile(
                    faceDetected = false,
                    faceBounds = null,
                    lightingScore = lightingScore,
                    imageQuality = qualityDesc,
                    estimatedFaceShape = FaceShape.UNKNOWN,
                    confidence = 0.2f
                )
            }

            val detectedFace = faces[0]!!
            val midPoint = PointF()
            detectedFace.getMidPoint(midPoint)
            val eyeDist = detectedFace.eyesDistance()
            val confidence = detectedFace.confidence() // usually around 0.5 - 0.7

            // Approximate facial landmarks from eye distance and midpoint
            // Standard biometric proportions: face width ~ 2.5 * eyeDistance
            // Face height ~ 3.3 * eyeDistance
            val estFaceWidth = eyeDist * 2.5f
            val estFaceHeight = eyeDist * 3.4f

            val left = max(0f, midPoint.x - estFaceWidth / 2f)
            val top = max(0f, midPoint.y - estFaceHeight * 0.42f)
            val right = min(safeBitmap.width.toFloat(), midPoint.x + estFaceWidth / 2f)
            val bottom = min(safeBitmap.height.toFloat(), midPoint.y + estFaceHeight * 0.58f)
            val faceBounds = RectF(left, top, right, bottom)

            val leftEye = PointF(midPoint.x - eyeDist / 2f, midPoint.y)
            val rightEye = PointF(midPoint.x + eyeDist / 2f, midPoint.y)
            val nose = PointF(midPoint.x, midPoint.y + eyeDist * 0.55f)
            val mouth = PointF(midPoint.x, midPoint.y + eyeDist * 1.15f)

            // Approximate pose angle
            val poseAngle = detectedFace.pose(FaceDetector.Face.EULER_Y) * 57.2958f // rad to deg

            // Face Shape Heuristic based on ratio & proportions
            val ratio = if (estFaceWidth > 0) (faceBounds.height() / faceBounds.width()) else 1.0f

            val estimatedShape = if (confidence < 0.35f || abs(poseAngle) > 25f) {
                FaceShape.UNKNOWN
            } else {
                classifyFaceShape(ratio, lightingScore, eyeDist, faceBounds)
            }

            FaceProfile(
                faceDetected = true,
                faceBounds = faceBounds,
                faceAngle = poseAngle,
                eyeDistance = eyeDist,
                eyeLeft = leftEye,
                eyeRight = rightEye,
                mouthPosition = mouth,
                nosePosition = nose,
                faceWidth = faceBounds.width(),
                faceHeight = faceBounds.height(),
                lightingScore = lightingScore,
                imageQuality = qualityDesc,
                estimatedFaceShape = estimatedShape,
                confidence = min(0.92f, max(0.4f, confidence + 0.2f))
            )
        } catch (e: Exception) {
            FaceProfile(
                faceDetected = false,
                lightingScore = 0.5f,
                imageQuality = "نیاز به بررسی",
                estimatedFaceShape = FaceShape.UNKNOWN,
                confidence = 0f
            )
        }
    }

    private fun classifyFaceShape(
        ratio: Float,
        lightingScore: Float,
        eyeDist: Float,
        bounds: RectF
    ): FaceShape {
        // Ratio = Height / Width
        // Long/Oblong: ratio >= 1.45
        // Oval: 1.25 <= ratio < 1.45
        // Round: 1.0 <= ratio < 1.18
        // Square: 1.15 <= ratio < 1.28 with wider jawline
        // Heart: medium ratio with wider forehead
        return when {
            ratio >= 1.42f -> FaceShape.LONG
            ratio in 1.26f..1.41f -> FaceShape.OVAL
            ratio in 1.14f..1.25f -> {
                // Heart or Square heuristic
                if (eyeDist / bounds.width() > 0.42f) FaceShape.HEART else FaceShape.SQUARE
            }
            ratio < 1.14f -> FaceShape.ROUND
            else -> FaceShape.UNKNOWN
        }
    }

    private fun evaluateLightingAndQuality(bitmap: Bitmap): Pair<Float, String> {
        var totalLuminance = 0.0
        val sampleStep = 8 // Sample pixels for efficiency
        var count = 0

        for (y in 0 until bitmap.height step sampleStep) {
            for (x in 0 until bitmap.width step sampleStep) {
                val pixel = bitmap.getPixel(x, y)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                // Relative luminance
                val lum = (0.299 * r + 0.587 * g + 0.114 * b) / 255.0
                totalLuminance += lum
                count++
            }
        }

        val avgLuminance = if (count > 0) (totalLuminance / count).toFloat() else 0.5f
        val quality = when {
            avgLuminance < 0.25f -> "کم‌نور (نیاز به نور بیشتر)"
            avgLuminance > 0.85f -> "بسیار روشن (احتمال محوشدگی)"
            bitmap.width < 300 || bitmap.height < 300 -> "وضوح متوسط"
            else -> "کیفیت عالی و مناسب سالن"
        }

        return Pair(avgLuminance, quality)
    }
}
